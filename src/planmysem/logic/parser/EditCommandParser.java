package planmysem.logic.parser;

import static planmysem.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Set;

import planmysem.common.Utils;
import planmysem.logic.commands.EditCommand;
import planmysem.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public EditCommand parse(String args) throws ParseException {
        HashMap<String, Set<String>> arguments = getParametersWithArguments(args);
        String stringIndex = getStartingArgument(args);
        int index = Utils.parseInteger(stringIndex);
        Set<String> tags = arguments.get(PREFIX_TAG);

        if ((index <= 0 && tags == null) || (index >= 0 && tags != null)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        String nst = getFirstInSet(arguments.get(PREFIX_NEW_START_TIME));
        LocalTime startTime = null;
        if (nst != null) {
            startTime = Utils.parseTime(nst);
            if (startTime == null) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
        }

        // determine if "end time" is a duration or time
        String net = getFirstInSet(arguments.get(PREFIX_NEW_END_TIME));
        int duration = -1;
        if (net != null) {
            duration = Utils.parseInteger(net);
            if (duration == -1) {
                LocalTime endTime = Utils.parseTime(net);
                if (endTime == null) {
                    throw new ParseException(String.format(
                            MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
                } else {
                    duration = Utils.getDuration(startTime, endTime);
                }
            }
        }

        // The following are not mandatory and can be null
        String name = getFirstInSet(arguments.get(PREFIX_NEW_NAME));
        String location = getFirstInSet(arguments.get(PREFIX_NEW_LOCATION));
        String description = getFirstInSet(arguments.get(PREFIX_NEW_DESCRIPTION));
        Set<String> newTags = arguments.get(PREFIX_NEW_TAG);

        if (index == -1) {
            return new EditCommand(name, startTime, duration, location, description, tags, newTags);
        } else {
            String nd = getFirstInSet(arguments.get(PREFIX_NEW_DATE));
            LocalDate date = Utils.parseDate(nd);

            return new EditCommand(index, name, date, startTime, duration, location, description, newTags);
        }
    }
}
