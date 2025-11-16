package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.Program;

public interface ProgramVisitor<TArg, TResult> {

	TResult visitProgram(Program ast, TArg arg);

}
