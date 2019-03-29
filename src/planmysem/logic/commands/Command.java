package planmysem.logic.commands;

import planmysem.logic.CommandHistory;
import planmysem.logic.commands.exceptions.CommandException;
import planmysem.model.Model;

/**
 * Represents an executable command.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param history {@code CommandHistory} which the command should operate on.
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute(Model model, CommandHistory history) throws CommandException;

}
