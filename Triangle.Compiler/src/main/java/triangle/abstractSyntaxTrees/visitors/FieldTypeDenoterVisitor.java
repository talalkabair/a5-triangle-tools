package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.types.MultipleFieldTypeDenoter;
import triangle.abstractSyntaxTrees.types.SingleFieldTypeDenoter;

public interface FieldTypeDenoterVisitor<TArg, TResult> {

	TResult visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, TArg arg);

	TResult visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, TArg arg);

}
