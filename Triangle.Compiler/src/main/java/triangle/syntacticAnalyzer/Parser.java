/*
 * Parser.java – Clean Code Refactor (2025 Student B)
 *
 * Implements DRY and Meaningful Naming principles to improve readability and
 * maintainability while preserving original compiler functionality.
 *
 * Author: Student B (2025)
 * Based on Triangle Compiler by D.A. Watt & D.F. Brown.
 */

package triangle.syntacticAnalyzer;

import triangle.ErrorReporter;
import triangle.abstractSyntaxTrees.Program;
import triangle.abstractSyntaxTrees.actuals.*;
import triangle.abstractSyntaxTrees.aggregates.*;
import triangle.abstractSyntaxTrees.commands.*;
import triangle.abstractSyntaxTrees.declarations.*;
import triangle.abstractSyntaxTrees.expressions.*;
import triangle.abstractSyntaxTrees.formals.*;
import triangle.abstractSyntaxTrees.terminals.*;
import triangle.abstractSyntaxTrees.types.*;
import triangle.abstractSyntaxTrees.vnames.*;

public class Parser {

	private final Scanner lexicalAnalyser;
	private final ErrorReporter errorReporter;
	private Token currentToken;
	private SourcePosition previousTokenPosition;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public Parser(Scanner lexer, ErrorReporter reporter) {
		this.lexicalAnalyser = lexer;
		this.errorReporter = reporter;
		this.previousTokenPosition = new SourcePosition();
	}

	// -------------------------------------------------------------------------
	// Utility & Error Handling
	// -------------------------------------------------------------------------

	private void expect(Token.Kind expectedKind) throws SyntaxError {
		if (currentToken.kind == expectedKind) {
			acceptIt();
		} else {
			syntacticError("Expected " + expectedKind + " but found " + currentToken.kind,
					currentToken.spelling);
		}
	}

	void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
		SourcePosition pos = currentToken.position;
		errorReporter.reportError(messageTemplate, tokenQuoted, pos);
		throw new SyntaxError();
	}

	void accept(Token.Kind tokenExpected) throws SyntaxError {
		if (currentToken.kind == tokenExpected) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else {
			syntacticError("\"%\" expected here", Token.spell(tokenExpected));
		}
	}

	void acceptIt() {
		previousTokenPosition = currentToken.position;
		currentToken = lexicalAnalyser.scan();
	}

	void start(SourcePosition position) { position.start = currentToken.position.start; }
	void finish(SourcePosition position) { position.finish = previousTokenPosition.finish; }

	// -------------------------------------------------------------------------
	// PROGRAM
	// -------------------------------------------------------------------------
	public Program parseProgram() {
		Program programAST = null;
		previousTokenPosition.start = 0;
		previousTokenPosition.finish = 0;
		currentToken = lexicalAnalyser.scan();

		try {
			Command cAST;
			Declaration dAST = null;
			SourcePosition pos = new SourcePosition();
			start(pos);

			// Allow top-level declarations before commands
			if (currentToken.kind == Token.Kind.CONST ||
					currentToken.kind == Token.Kind.VAR ||
					currentToken.kind == Token.Kind.TYPE ||
					currentToken.kind == Token.Kind.PROC ||
					currentToken.kind == Token.Kind.FUNC) {

				dAST = parseDeclaration();

				// Optional semicolon after declaration
				if (currentToken.kind == Token.Kind.SEMICOLON)
					acceptIt();

				// If next token begins a command, parse it
				if (currentToken.kind != Token.Kind.EOT)
					cAST = parseCommand();
				else
					cAST = new EmptyCommand(pos);

				finish(pos);
				cAST = new LetCommand(dAST, cAST, pos);
			} else {
				// Otherwise start with a normal command
				cAST = parseCommand();
			}

			programAST = new Program(cAST, previousTokenPosition);

			if (currentToken.kind != Token.Kind.EOT)
				syntacticError("\"%\" not expected after end of program", currentToken.spelling);

		} catch (SyntaxError s) {
			return null;
		}

		return programAST;
	}


	// -------------------------------------------------------------------------
	// COMMANDS
	// -------------------------------------------------------------------------
	private Command parseBasicCommand() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		Command cmd;

		Identifier id = parseIdentifier();
		if (currentToken.kind == Token.Kind.LPAREN) {
			acceptIt();
			ActualParameterSequence aps = parseActualParameterSequence();
			expect(Token.Kind.RPAREN);
			finish(pos);
			cmd = new CallCommand(id, aps, pos);
		} else {
			Vname v = parseRestOfVname(id);
			expect(Token.Kind.BECOMES);
			Expression e = parseExpression();
			finish(pos);
			cmd = new AssignCommand(v, e, pos);
		}
		return cmd;
	}

	Command parseCommand() throws SyntaxError {
		Command commandAST;
		SourcePosition pos = new SourcePosition();
		start(pos);
		commandAST = parseSingleCommand();

		while (currentToken.kind == Token.Kind.SEMICOLON) {
			acceptIt();
			Command c2 = parseSingleCommand();
			finish(pos);
			commandAST = new SequentialCommand(commandAST, c2, pos);
		}
		return commandAST;
	}

	Command parseSingleCommand() throws SyntaxError {
		Command commandAST;
		SourcePosition pos = new SourcePosition();
		start(pos);

		switch (currentToken.kind) {
			case IDENTIFIER:
				commandAST = parseBasicCommand();
				break;

			case BEGIN:
				acceptIt();
				commandAST = parseCommand();
				expect(Token.Kind.END);
				break;

			case LET:
				acceptIt();
				Declaration d = parseDeclaration();
				expect(Token.Kind.IN);
				Command c = parseSingleCommand();
				finish(pos);
				commandAST = new LetCommand(d, c, pos);
				break;

			case IF:
				acceptIt();
				Expression e = parseExpression();
				expect(Token.Kind.THEN);
				Command c1 = parseSingleCommand();
				expect(Token.Kind.ELSE);
				Command c2 = parseSingleCommand();
				finish(pos);
				commandAST = new IfCommand(e, c1, c2, pos);
				break;

			case WHILE:
				acceptIt();
				Expression cond = parseExpression();
				expect(Token.Kind.DO);
				Command body = parseSingleCommand();
				finish(pos);
				commandAST = new WhileCommand(cond, body, pos);
				break;

			case REPEAT:
				acceptIt(); // consume 'repeat'
				Command bodyCmd = parseCommand(); // body of loop
				expect(Token.Kind.UNTIL); // consume 'until'
				Expression untilExpr = parseExpression(); // condition
				finish(pos);
				commandAST = new RepeatCommand(bodyCmd, untilExpr, pos);
				break;

			case SEMICOLON:
			case END:
			case ELSE:
			case IN:
			case EOT:
				finish(pos);
				commandAST = new EmptyCommand(pos);
				break;

			default:
				syntacticError("\"%\" cannot start a command", currentToken.spelling);
				commandAST = new EmptyCommand(pos);
				break;
		}
		return commandAST;
	}

	// -------------------------------------------------------------------------
	// EXPRESSIONS
	// -------------------------------------------------------------------------
	Expression parseExpression() throws SyntaxError {
		Expression expr;
		SourcePosition pos = new SourcePosition();
		start(pos);

		switch (currentToken.kind) {
			case LET:
				acceptIt();
				Declaration d = parseDeclaration();
				expect(Token.Kind.IN);
				Expression e1 = parseExpression();
				finish(pos);
				expr = new LetExpression(d, e1, pos);
				break;

			case IF:
				acceptIt();
				Expression e2 = parseExpression();
				expect(Token.Kind.THEN);
				Expression e3 = parseExpression();
				expect(Token.Kind.ELSE);
				Expression e4 = parseExpression();
				finish(pos);
				expr = new IfExpression(e2, e3, e4, pos);
				break;

			default:
				expr = parseSecondaryExpression();
				break;
		}
		return expr;
	}

	Expression parseSecondaryExpression() throws SyntaxError {
		Expression expr;
		SourcePosition pos = new SourcePosition();
		start(pos);

		expr = parsePrimaryExpression();
		while (currentToken.kind == Token.Kind.OPERATOR) {
			Operator op = parseOperator();
			Expression e2 = parsePrimaryExpression();
			expr = new BinaryExpression(expr, op, e2, pos);
		}
		return expr;
	}

	Expression parsePrimaryExpression() throws SyntaxError {
		Expression expr;
		SourcePosition pos = new SourcePosition();
		start(pos);

		switch (currentToken.kind) {
			case INTLITERAL:
				IntegerLiteral il = parseIntegerLiteral();
				finish(pos);
				expr = new IntegerExpression(il, pos);
				break;

			case CHARLITERAL:
				CharacterLiteral cl = parseCharacterLiteral();
				finish(pos);
				expr = new CharacterExpression(cl, pos);
				break;

			case LBRACKET:
				acceptIt();
				ArrayAggregate aa = parseArrayAggregate();
				expect(Token.Kind.RBRACKET);
				finish(pos);
				expr = new ArrayExpression(aa, pos);
				break;

			case LCURLY:
				acceptIt();
				RecordAggregate ra = parseRecordAggregate();
				expect(Token.Kind.RCURLY);
				finish(pos);
				expr = new RecordExpression(ra, pos);
				break;

			case IDENTIFIER:
				Identifier i = parseIdentifier();
				if (currentToken.kind == Token.Kind.LPAREN) {
					acceptIt();
					ActualParameterSequence aps = parseActualParameterSequence();
					expect(Token.Kind.RPAREN);
					finish(pos);
					expr = new CallExpression(i, aps, pos);
				} else {
					Vname v = parseRestOfVname(i);
					finish(pos);
					expr = new VnameExpression(v, pos);
				}
				break;

			case OPERATOR:
				Operator op = parseOperator();
				Expression e = parsePrimaryExpression();
				finish(pos);
				expr = new UnaryExpression(op, e, pos);
				break;

			case LPAREN:
				acceptIt();
				expr = parseExpression();
				expect(Token.Kind.RPAREN);
				break;

			default:
				syntacticError("\"%\" cannot start an expression", currentToken.spelling);
				expr = new IntegerExpression(new IntegerLiteral("0", pos), pos);
				break;
		}
		return expr;
	}

	// -------------------------------------------------------------------------
	// AGGREGATES (RECORDS & ARRAYS)
	// -------------------------------------------------------------------------
	RecordAggregate parseRecordAggregate() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		Identifier i = parseIdentifier();
		expect(Token.Kind.IS);
		Expression e = parseExpression();

		RecordAggregate agg;
		if (currentToken.kind == Token.Kind.COMMA) {
			acceptIt();
			RecordAggregate next = parseRecordAggregate();
			finish(pos);
			agg = new MultipleRecordAggregate(i, e, next, pos);
		} else {
			finish(pos);
			agg = new SingleRecordAggregate(i, e, pos);
		}
		return agg;
	}

	ArrayAggregate parseArrayAggregate() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		Expression e = parseExpression();
		ArrayAggregate agg;
		if (currentToken.kind == Token.Kind.COMMA) {
			acceptIt();
			ArrayAggregate next = parseArrayAggregate();
			finish(pos);
			agg = new MultipleArrayAggregate(e, next, pos);
		} else {
			finish(pos);
			agg = new SingleArrayAggregate(e, pos);
		}
		return agg;
	}

	// -------------------------------------------------------------------------
	// VALUE OR VARIABLE NAMES
	// -------------------------------------------------------------------------
	Vname parseVname() throws SyntaxError {
		Identifier i = parseIdentifier();
		return parseRestOfVname(i);
	}

	Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
		SourcePosition vpos = identifierAST.getPosition();
		Vname v = new SimpleVname(identifierAST, vpos);

		while (currentToken.kind == Token.Kind.DOT || currentToken.kind == Token.Kind.LBRACKET) {
			if (currentToken.kind == Token.Kind.DOT) {
				acceptIt();
				Identifier i = parseIdentifier();
				v = new DotVname(v, i, vpos);
			} else {
				acceptIt();
				Expression e = parseExpression();
				expect(Token.Kind.RBRACKET);
				finish(vpos);
				v = new SubscriptVname(v, e, vpos);
			}
		}
		return v;
	}

	// -------------------------------------------------------------------------
	// DECLARATIONS
	// -------------------------------------------------------------------------
	Declaration parseDeclaration() throws SyntaxError {
		Declaration decl;
		SourcePosition pos = new SourcePosition();
		start(pos);
		decl = parseSingleDeclaration();
		while (currentToken.kind == Token.Kind.SEMICOLON) {
			acceptIt();
			Declaration next = parseSingleDeclaration();
			finish(pos);
			decl = new SequentialDeclaration(decl, next, pos);
		}
		return decl;
	}

	Declaration parseSingleDeclaration() throws SyntaxError {
		Declaration decl;
		SourcePosition pos = new SourcePosition();
		start(pos);

		switch (currentToken.kind) {
			case CONST:
				acceptIt();
				Identifier i = parseIdentifier();
				expect(Token.Kind.IS);
				Expression e = parseExpression();
				finish(pos);
				decl = new ConstDeclaration(i, e, pos);
				break;

			case VAR:
				acceptIt();
				Identifier id = parseIdentifier();
				expect(Token.Kind.COLON);
				TypeDenoter t = parseTypeDenoter();
				finish(pos);
				decl = new VarDeclaration(id, t, pos);
				break;

			case PROC:
				acceptIt();
				Identifier pid = parseIdentifier();
				expect(Token.Kind.LPAREN);
				FormalParameterSequence fps = parseFormalParameterSequence();
				expect(Token.Kind.RPAREN);
				expect(Token.Kind.IS);
				Command c = parseSingleCommand();
				finish(pos);
				decl = new ProcDeclaration(pid, fps, c, pos);
				break;

			case FUNC:
				acceptIt();
				Identifier fid = parseIdentifier();
				expect(Token.Kind.LPAREN);
				FormalParameterSequence fseq = parseFormalParameterSequence();
				expect(Token.Kind.RPAREN);
				expect(Token.Kind.COLON);
				TypeDenoter rtype = parseTypeDenoter();
				expect(Token.Kind.IS);
				Expression body = parseExpression();
				finish(pos);
				decl = new FuncDeclaration(fid, fseq, rtype, body, pos);
				break;

			case TYPE:
				acceptIt();
				Identifier tid = parseIdentifier();
				expect(Token.Kind.IS);
				TypeDenoter td = parseTypeDenoter();
				finish(pos);
				decl = new TypeDeclaration(tid, td, pos);
				break;

			default:
				syntacticError("\"%\" cannot start a declaration", currentToken.spelling);
				decl = new ConstDeclaration(new Identifier("error", pos),
						new IntegerExpression(new IntegerLiteral("0", pos), pos), pos);
				break;
		}
		return decl;
	}

	// -------------------------------------------------------------------------
	// FORMAL PARAMETERS
	// -------------------------------------------------------------------------
	FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
		if (currentToken.kind == Token.Kind.RPAREN)
			return new EmptyFormalParameterSequence(currentToken.position);
		return parseProperFormalParameterSequence();
	}

	FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		FormalParameter fp = parseFormalParameter();
		if (currentToken.kind == Token.Kind.COMMA) {
			acceptIt();
			FormalParameterSequence fps = parseProperFormalParameterSequence();
			finish(pos);
			return new MultipleFormalParameterSequence(fp, fps, pos);
		} else {
			finish(pos);
			return new SingleFormalParameterSequence(fp, pos);
		}
	}

	FormalParameter parseFormalParameter() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		FormalParameter formal;

		switch (currentToken.kind) {
			case IDENTIFIER:
				Identifier id = parseIdentifier();
				expect(Token.Kind.COLON);
				TypeDenoter t = parseTypeDenoter();
				finish(pos);
				formal = new ConstFormalParameter(id, t, pos);
				break;

			case VAR:
				acceptIt();
				Identifier vid = parseIdentifier();
				expect(Token.Kind.COLON);
				TypeDenoter vt = parseTypeDenoter();
				finish(pos);
				formal = new VarFormalParameter(vid, vt, pos);
				break;

			case PROC:
				acceptIt();
				Identifier pid = parseIdentifier();
				expect(Token.Kind.LPAREN);
				FormalParameterSequence fps = parseFormalParameterSequence();
				expect(Token.Kind.RPAREN);
				finish(pos);
				formal = new ProcFormalParameter(pid, fps, pos);
				break;

			case FUNC:
				acceptIt();
				Identifier fid = parseIdentifier();
				expect(Token.Kind.LPAREN);
				FormalParameterSequence fseq = parseFormalParameterSequence();
				expect(Token.Kind.RPAREN);
				expect(Token.Kind.COLON);
				TypeDenoter rt = parseTypeDenoter();
				finish(pos);
				formal = new FuncFormalParameter(fid, fseq, rt, pos);
				break;

			default:
				syntacticError("\"%\" cannot start a formal parameter", currentToken.spelling);
				formal = null;
				break;
		}
		return formal;
	}

	// -------------------------------------------------------------------------
	// ACTUAL PARAMETERS
	// -------------------------------------------------------------------------
	ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
		ActualParameterSequence actualsAST;
		SourcePosition pos = new SourcePosition();
		start(pos);

		if (currentToken.kind == Token.Kind.RPAREN) {
			finish(pos);
			actualsAST = new EmptyActualParameterSequence(pos);
		} else {
			actualsAST = parseProperActualParameterSequence();
		}
		return actualsAST;
	}

	ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		ActualParameter ap = parseActualParameter();
		ActualParameterSequence actuals;

		if (currentToken.kind == Token.Kind.COMMA) {
			acceptIt();
			ActualParameterSequence aps = parseProperActualParameterSequence();
			finish(pos);
			actuals = new MultipleActualParameterSequence(ap, aps, pos);
		} else {
			finish(pos);
			actuals = new SingleActualParameterSequence(ap, pos);
		}
		return actuals;
	}

	ActualParameter parseActualParameter() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		ActualParameter actual;

		switch (currentToken.kind) {
			case IDENTIFIER:
			case INTLITERAL:
			case CHARLITERAL:
			case OPERATOR:
			case LET:
			case IF:
			case LPAREN:
			case LBRACKET:
			case LCURLY:
				Expression e = parseExpression();
				finish(pos);
				actual = new ConstActualParameter(e, pos);
				break;

			case VAR:
				acceptIt();
				Vname v = parseVname();
				finish(pos);
				actual = new VarActualParameter(v, pos);
				break;

			case PROC:
				acceptIt();
				Identifier pid = parseIdentifier();
				finish(pos);
				actual = new ProcActualParameter(pid, pos);
				break;

			case FUNC:
				acceptIt();
				Identifier fid = parseIdentifier();
				finish(pos);
				actual = new FuncActualParameter(fid, pos);
				break;

			default:
				syntacticError("\"%\" cannot start an actual parameter", currentToken.spelling);
				actual = new ConstActualParameter(new IntegerExpression(new IntegerLiteral("0", pos), pos), pos);
				break;
		}
		return actual;
	}

	// -------------------------------------------------------------------------
	// TYPE DENOTERS
	// -------------------------------------------------------------------------
	TypeDenoter parseTypeDenoter() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		TypeDenoter type;

		switch (currentToken.kind) {
			case IDENTIFIER:
				Identifier i = parseIdentifier();
				finish(pos);
				type = new SimpleTypeDenoter(i, pos);
				break;

			case ARRAY:
				acceptIt();
				IntegerLiteral il = parseIntegerLiteral();
				expect(Token.Kind.OF);
				TypeDenoter t = parseTypeDenoter();
				finish(pos);
				type = new ArrayTypeDenoter(il, t, pos);
				break;

			case RECORD:
				acceptIt();
				FieldTypeDenoter f = parseFieldTypeDenoter();
				expect(Token.Kind.END);
				finish(pos);
				type = new RecordTypeDenoter(f, pos);
				break;

			default:
				syntacticError("\"%\" cannot start a type denoter", currentToken.spelling);
				type = new SimpleTypeDenoter(new Identifier("error", pos), pos);
				break;
		}
		return type;
	}

	FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
		SourcePosition pos = new SourcePosition();
		start(pos);
		Identifier i = parseIdentifier();
		expect(Token.Kind.COLON);
		TypeDenoter t = parseTypeDenoter();

		FieldTypeDenoter field;
		if (currentToken.kind == Token.Kind.COMMA) {
			acceptIt();
			FieldTypeDenoter next = parseFieldTypeDenoter();
			finish(pos);
			field = new MultipleFieldTypeDenoter(i, t, next, pos);
		} else {
			finish(pos);
			field = new SingleFieldTypeDenoter(i, t, pos);
		}
		return field;
	}

	// -------------------------------------------------------------------------
	// TERMINALS
	// -------------------------------------------------------------------------
	IntegerLiteral parseIntegerLiteral() throws SyntaxError {
		if (currentToken.kind != Token.Kind.INTLITERAL)
			syntacticError("integer literal expected here", "");
		IntegerLiteral IL = new IntegerLiteral(currentToken.spelling, currentToken.position);
		acceptIt();
		return IL;
	}

	CharacterLiteral parseCharacterLiteral() throws SyntaxError {
		if (currentToken.kind != Token.Kind.CHARLITERAL)
			syntacticError("character literal expected here", "");
		CharacterLiteral CL = new CharacterLiteral(currentToken.spelling, currentToken.position);
		acceptIt();
		return CL;
	}

	Identifier parseIdentifier() throws SyntaxError {
		if (currentToken.kind != Token.Kind.IDENTIFIER)
			syntacticError("identifier expected here", "");
		Identifier I = new Identifier(currentToken.spelling, currentToken.position);
		acceptIt();
		return I;
	}

	Operator parseOperator() throws SyntaxError {
		if (currentToken.kind != Token.Kind.OPERATOR)
			syntacticError("operator expected here", "");
		Operator O = new Operator(currentToken.spelling, currentToken.position);
		acceptIt();
		return O;
	}
}
