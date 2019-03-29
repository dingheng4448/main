package planmysem.logic.commands;

import planmysem.logic.CommandHistory;
import planmysem.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Exits the program.\n\t"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_EXIT_ACKNOWEDGEMENT = "Exiting PlanMySem as requested ...";

    @Override
    public CommandResult execute(Model model, CommandHistory commandHistory) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWEDGEMENT);
    }

}
