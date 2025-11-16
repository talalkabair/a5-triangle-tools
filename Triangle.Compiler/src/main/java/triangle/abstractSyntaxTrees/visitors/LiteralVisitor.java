package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.terminals.CharacterLiteral;
import triangle.abstractSyntaxTrees.terminals.IntegerLiteral;

public interface LiteralVisitor<TArg, TResult> {

	TResult visitCharacterLiteral(CharacterLiteral ast, TArg arg);

	TResult visitIntegerLiteral(IntegerLiteral ast, TArg arg);

}
