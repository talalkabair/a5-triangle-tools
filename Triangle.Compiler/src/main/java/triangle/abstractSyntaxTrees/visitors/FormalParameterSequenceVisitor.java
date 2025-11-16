package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.formals.EmptyFormalParameterSequence;
import triangle.abstractSyntaxTrees.formals.MultipleFormalParameterSequence;
import triangle.abstractSyntaxTrees.formals.SingleFormalParameterSequence;

public interface FormalParameterSequenceVisitor<TArg, TResult> {

	TResult visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, TArg arg);

	TResult visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, TArg arg);

	TResult visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, TArg arg);

}
