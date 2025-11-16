package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.terminals.Identifier;

public interface IdentifierVisitor<TArg, TResult> {

	TResult visitIdentifier(Identifier ast, TArg arg);

}
