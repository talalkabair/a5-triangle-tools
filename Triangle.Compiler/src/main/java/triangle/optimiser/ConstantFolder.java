package triangle.optimiser;

import triangle.abstractSyntaxTrees.*;
import triangle.abstractSyntaxTrees.commands.*;
import triangle.abstractSyntaxTrees.declarations.*;
import triangle.abstractSyntaxTrees.expressions.*;
import triangle.abstractSyntaxTrees.terminals.*;
import triangle.abstractSyntaxTrees.visitors.*;
import triangle.abstractSyntaxTrees.vnames.*;
import triangle.abstractSyntaxTrees.aggregates.*;
import triangle.abstractSyntaxTrees.actuals.*;
import triangle.abstractSyntaxTrees.formals.*;
import triangle.abstractSyntaxTrees.types.*;
import triangle.abstractSyntaxTrees.commands.RepeatCommand;
import triangle.abstractSyntaxTrees.AbstractSyntaxTree;



public class ConstantFolder implements
		ActualParameterVisitor<Void, AbstractSyntaxTree>,
		ActualParameterSequenceVisitor<Void, AbstractSyntaxTree>,
		ArrayAggregateVisitor<Void, AbstractSyntaxTree>,
		CommandVisitor<Void, AbstractSyntaxTree>,
		DeclarationVisitor<Void, AbstractSyntaxTree>,
		ExpressionVisitor<Void, AbstractSyntaxTree>,
		FormalParameterSequenceVisitor<Void, AbstractSyntaxTree>,
		IdentifierVisitor<Void, AbstractSyntaxTree>,
		LiteralVisitor<Void, AbstractSyntaxTree>,
		OperatorVisitor<Void, AbstractSyntaxTree>,
		ProgramVisitor<Void, AbstractSyntaxTree>,
		RecordAggregateVisitor<Void, AbstractSyntaxTree>,
		TypeDenoterVisitor<Void, AbstractSyntaxTree>,
		VnameVisitor<Void, AbstractSyntaxTree> {

	public ConstantFolder() { }

	// === Entry Point ===
	public void optimise(Program program) {
		if (program != null)
			program.visit(this);
	}

	// === Folding Logic ===
	private AbstractSyntaxTree foldBinaryExpression(Expression left, Expression right, Operator op) {
		try {
			if (left instanceof IntegerExpression && right instanceof IntegerExpression) {
				int v1 = Integer.parseInt(((IntegerExpression) left).IL.spelling);
				int v2 = Integer.parseInt(((IntegerExpression) right).IL.spelling);
				int result = 0;
				boolean isInt = true;
				boolean boolResult = false;

				switch (op.spelling) {
					case "+": result = v1 + v2; break;
					case "-": result = v1 - v2; break;
					case "*": result = v1 * v2; break;
					case "/": result = (v2 != 0) ? v1 / v2 : 0; break;
					case "<":  isInt = false; boolResult = v1 < v2; break;
					case "<=": isInt = false; boolResult = v1 <= v2; break;
					case ">":  isInt = false; boolResult = v1 > v2; break;
					case ">=": isInt = false; boolResult = v1 >= v2; break;
					case "=":  isInt = false; boolResult = v1 == v2; break;
					case "\\=": isInt = false; boolResult = v1 != v2; break;
					default: return null;
				}

				if (isInt)
					return new IntegerExpression(new IntegerLiteral(String.valueOf(result), left.getPosition()), left.getPosition());
				else
					return new CharacterExpression(new CharacterLiteral(String.valueOf(boolResult), left.getPosition()), left.getPosition());
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	// === Visitor Implementations ===

	@Override
	public AbstractSyntaxTree visitProgram(Program ast, Void arg) {
		if (ast.C != null) ast.C.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitEmptyCommand(EmptyCommand ast, Void arg) {
		// Nothing to fold in an empty command — simply return null
		return null;
	}

	@Override
	public AbstractSyntaxTree visitBinaryExpression(BinaryExpression ast, Void arg) {
		AbstractSyntaxTree left = ast.E1.visit(this);
		AbstractSyntaxTree right = ast.E2.visit(this);
		ast.O.visit(this);

		if (left instanceof Expression && right instanceof Expression) {
			AbstractSyntaxTree folded = foldBinaryExpression((Expression) left, (Expression) right, ast.O);
			if (folded != null) return folded;
		}

		if (left != null) ast.E1 = (Expression) left;
		if (right != null) ast.E2 = (Expression) right;
		return null;
	}

	@Override
	public AbstractSyntaxTree visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Void arg) {
		if (ast.ARG != null) ast.ARG.visit(this);
		if (ast.O != null) ast.O.visit(this);
		if (ast.RES != null) ast.RES.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitUnaryExpression(UnaryExpression ast, Void arg) {
		AbstractSyntaxTree folded = ast.E.visit(this);
		if (folded instanceof IntegerExpression && ast.O.spelling.equals("-")) {
			int val = Integer.parseInt(((IntegerExpression) folded).IL.spelling);
			return new IntegerExpression(new IntegerLiteral(String.valueOf(-val), ast.getPosition()), ast.getPosition());
		}
		if (folded != null) ast.E = (Expression) folded;
		return null;
	}

	@Override
	public AbstractSyntaxTree visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Void arg) {
		if (ast.ARG1 != null) ast.ARG1.visit(this);
		if (ast.ARG2 != null) ast.ARG2.visit(this);
		if (ast.O != null) ast.O.visit(this);
		if (ast.RES != null) ast.RES.visit(this);
		return null;
	}


	@Override
	public AbstractSyntaxTree visitIntegerExpression(IntegerExpression ast, Void arg) {
		return ast;
	}

	@Override
	public AbstractSyntaxTree visitRepeatCommand(RepeatCommand ast, Void arg) {
		if (ast.C != null) ast.C.visit(this, null);
		if (ast.E != null) ast.E.visit(this, null);
		return ast;
	}


	@Override
	public AbstractSyntaxTree visitCharacterExpression(CharacterExpression ast, Void arg) {
		return ast;
	}

	@Override
	public AbstractSyntaxTree visitCallExpression(CallExpression ast, Void arg) {
		if (ast.I != null) ast.I.visit(this);
		if (ast.APS != null) ast.APS.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitVnameExpression(VnameExpression ast, Void arg) {
		if (ast.V != null) ast.V.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitAssignCommand(AssignCommand ast, Void arg) {
		AbstractSyntaxTree folded = ast.E.visit(this);
		if (folded != null) ast.E = (Expression) folded;
		ast.V.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitIfCommand(IfCommand ast, Void arg) {
		AbstractSyntaxTree cond = ast.E.visit(this);
		if (cond != null) ast.E = (Expression) cond;
		if (ast.C1 != null) ast.C1.visit(this);
		if (ast.C2 != null) ast.C2.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitWhileCommand(WhileCommand ast, Void arg) {
		AbstractSyntaxTree cond = ast.E.visit(this);
		if (cond != null) ast.E = (Expression) cond;
		if (ast.C != null) ast.C.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitSequentialCommand(SequentialCommand ast, Void arg) {
		if (ast.C1 != null) ast.C1.visit(this);
		if (ast.C2 != null) ast.C2.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitCallCommand(CallCommand ast, Void arg) {
		if (ast.I != null) ast.I.visit(this);
		if (ast.APS != null) ast.APS.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitConstDeclaration(ConstDeclaration ast, Void arg) {
		if (ast.E != null) ast.E.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitLetCommand(LetCommand ast, Void arg) {
		if (ast.D != null) ast.D.visit(this);
		if (ast.C != null) ast.C.visit(this);
		return null;
	}

	@Override
	public AbstractSyntaxTree visitLetExpression(LetExpression ast, Void arg) {
		if (ast.D != null) ast.D.visit(this);
		if (ast.E != null) ast.E.visit(this);
		return null;
	}

	@Override public AbstractSyntaxTree visitIdentifier(Identifier ast, Void arg) { return null; }
	@Override public AbstractSyntaxTree visitOperator(Operator ast, Void arg) { return null; }
	@Override public AbstractSyntaxTree visitIntegerLiteral(IntegerLiteral ast, Void arg) { return ast; }
	@Override public AbstractSyntaxTree visitCharacterLiteral(CharacterLiteral ast, Void arg) { return ast; }
	@Override public AbstractSyntaxTree visitVarActualParameter(VarActualParameter ast, Void arg) { if (ast.V != null) ast.V.visit(this); return null; }

	// ==== Empty/default stubs for all required visitors ====
	@Override public AbstractSyntaxTree visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSingleActualParameterSequence(SingleActualParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitConstActualParameter(ConstActualParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitFuncActualParameter(FuncActualParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitProcActualParameter(ProcActualParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitAnyTypeDenoter(AnyTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitArrayTypeDenoter(ArrayTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitBoolTypeDenoter(BoolTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitCharTypeDenoter(CharTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitErrorTypeDenoter(ErrorTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitIntTypeDenoter(IntTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSimpleTypeDenoter(SimpleTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitRecordTypeDenoter(RecordTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitArrayExpression(ArrayExpression ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitRecordExpression(RecordExpression ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitEmptyExpression(EmptyExpression ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitIfExpression(IfExpression ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSequentialDeclaration(SequentialDeclaration ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitVarDeclaration(VarDeclaration ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitFuncDeclaration(FuncDeclaration ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitProcDeclaration(ProcDeclaration ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitTypeDeclaration(TypeDeclaration ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitDotVname(DotVname ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSimpleVname(SimpleVname ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSubscriptVname(SubscriptVname ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitMultipleArrayAggregate(MultipleArrayAggregate ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSingleArrayAggregate(SingleArrayAggregate ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitMultipleRecordAggregate(MultipleRecordAggregate ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSingleRecordAggregate(SingleRecordAggregate ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitConstFormalParameter(ConstFormalParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitVarFormalParameter(VarFormalParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitProcFormalParameter(ProcFormalParameter ast, Void arg){return null;}
	@Override public AbstractSyntaxTree visitFuncFormalParameter(FuncFormalParameter ast, Void arg){return null;}
}
