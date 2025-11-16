package triangle.abstractSyntaxTrees.declarations;

import triangle.abstractSyntaxTrees.formals.FormalParameterSequence;
import triangle.abstractSyntaxTrees.types.TypeDenoter;

public interface FunctionDeclaration {

	FormalParameterSequence getFormals();

	TypeDenoter getType();
	
}
