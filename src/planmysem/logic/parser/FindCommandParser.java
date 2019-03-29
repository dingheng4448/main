package planmysem.logic.parser;

import static planmysem.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static planmysem.common.Messages.MESSAGE_INVALID_MULTIPLE_PARAMS;

import java.util.HashMap;
import java.util.Set;

import planmysem.logic.commands.FindCommand;
import planmysem.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public FindCommand parse(String args) throws ParseException {
        HashMap<String, Set<String>> arguments = getParametersWithArguments(args);
        String name = getFirstInSet(arguments.get(PREFIX_NAME));
        String tag = getFirstInSet(arguments.get(PREFIX_TAG));
        if (name == null && tag == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        } else if (name != null && tag != null) {
            throw new ParseException(String.format(MESSAGE_INVALID_MULTIPLE_PARAMS, FindCommand.MESSAGE_USAGE));

        }
        return new FindCommand(name, tag);
    }
}
