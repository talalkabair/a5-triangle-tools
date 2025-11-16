package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.terminals.Operator;

public interface OperatorVisitor<TArg, TResult> {

	TResult visitOperator(Operator ast, TArg arg);

}
