package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.types.AnyTypeDenoter;
import triangle.abstractSyntaxTrees.types.ArrayTypeDenoter;
import triangle.abstractSyntaxTrees.types.BoolTypeDenoter;
import triangle.abstractSyntaxTrees.types.CharTypeDenoter;
import triangle.abstractSyntaxTrees.types.ErrorTypeDenoter;
import triangle.abstractSyntaxTrees.types.IntTypeDenoter;
import triangle.abstractSyntaxTrees.types.RecordTypeDenoter;
import triangle.abstractSyntaxTrees.types.SimpleTypeDenoter;

public interface TypeDenoterVisitor<TArg, TResult> extends FieldTypeDenoterVisitor<TArg, TResult> {

	TResult visitAnyTypeDenoter(AnyTypeDenoter ast, TArg arg);

	TResult visitArrayTypeDenoter(ArrayTypeDenoter ast, TArg arg);

	TResult visitBoolTypeDenoter(BoolTypeDenoter ast, TArg arg);

	TResult visitCharTypeDenoter(CharTypeDenoter ast, TArg arg);

	TResult visitErrorTypeDenoter(ErrorTypeDenoter ast, TArg arg);

	TResult visitSimpleTypeDenoter(SimpleTypeDenoter ast, TArg arg);

	TResult visitIntTypeDenoter(IntTypeDenoter ast, TArg arg);

	TResult visitRecordTypeDenoter(RecordTypeDenoter ast, TArg arg);

}
