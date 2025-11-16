/*
 * RepeatCommand.java
 *
 * Implements the 'repeat ... until <expression>' loop.
 * The body executes at least once before the condition is checked.
 *
 * Syntax example:
 *   repeat
 *     <command-sequence>
 *   until <boolean-expression>
 */


package triangle.abstractSyntaxTrees.commands;

import triangle.abstractSyntaxTrees.expressions.Expression;
import triangle.abstractSyntaxTrees.visitors.CommandVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class RepeatCommand extends Command {
    public Command C;
    public Expression E;

    public RepeatCommand(Command cAST, Expression eAST, SourcePosition position) {
        super(position);
        this.C = cAST;
        this.E = eAST;
    }

    @Override
    public <TArg, TResult> TResult visit(CommandVisitor<TArg, TResult> v, TArg o) {
        return v.visitRepeatCommand(this, o);
    }
}
