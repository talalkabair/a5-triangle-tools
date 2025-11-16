package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.actuals.EmptyActualParameterSequence;
import triangle.abstractSyntaxTrees.actuals.MultipleActualParameterSequence;
import triangle.abstractSyntaxTrees.actuals.SingleActualParameterSequence;

public interface ActualParameterSequenceVisitor<TArg, TResult> {

	TResult visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, TArg arg);

	TResult visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, TArg arg);

	TResult visitSingleActualParameterSequence(SingleActualParameterSequence ast, TArg arg);

}
