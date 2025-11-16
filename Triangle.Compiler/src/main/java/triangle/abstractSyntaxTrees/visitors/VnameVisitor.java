package triangle.abstractSyntaxTrees.visitors;

import triangle.abstractSyntaxTrees.vnames.DotVname;
import triangle.abstractSyntaxTrees.vnames.SimpleVname;
import triangle.abstractSyntaxTrees.vnames.SubscriptVname;

public interface VnameVisitor<TArg, TResult> {

	TResult visitDotVname(DotVname ast, TArg arg);

	TResult visitSimpleVname(SimpleVname ast, TArg arg);

	TResult visitSubscriptVname(SubscriptVname ast, TArg arg);

}
