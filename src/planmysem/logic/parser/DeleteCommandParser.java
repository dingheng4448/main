package planmysem.logic.parser;

import static planmysem.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashMap;
import java.util.Set;

import planmysem.common.Utils;
import planmysem.logic.commands.DeleteCommand;
import planmysem.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public DeleteCommand parse(String args) throws ParseException {
        HashMap<String, Set<String>> arguments = getParametersWithArguments(args);
        String stringIndex = getStartingArgument(args);
        int index = Utils.parseInteger(stringIndex);
        Set<String> tags = arguments.get(PREFIX_TAG);

        if ((index <= 0 && tags == null) || (index > 0 && tags != null)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (index == -1) {
            return new DeleteCommand(tags);
        } else {
            return new DeleteCommand(index);
        }
    }
}
