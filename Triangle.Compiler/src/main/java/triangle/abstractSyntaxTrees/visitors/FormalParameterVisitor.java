package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.formals.ConstFormalParameter;
import triangle.abstractSyntaxTrees.formals.FuncFormalParameter;
import triangle.abstractSyntaxTrees.formals.ProcFormalParameter;
import triangle.abstractSyntaxTrees.formals.VarFormalParameter;

public interface FormalParameterVisitor<TArg, TResult> {

	TResult visitConstFormalParameter(ConstFormalParameter ast, TArg arg);

	TResult visitFuncFormalParameter(FuncFormalParameter ast, TArg arg);

	TResult visitProcFormalParameter(ProcFormalParameter ast, TArg arg);

	TResult visitVarFormalParameter(VarFormalParameter ast, TArg arg);

}
