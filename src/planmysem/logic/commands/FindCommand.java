//@@author marcus-pzj
package planmysem.logic.commands;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javafx.util.Pair;
import planmysem.common.Messages;
import planmysem.logic.CommandHistory;
import planmysem.model.Model;
import planmysem.model.semester.Day;
import planmysem.model.semester.ReadOnlyDay;
import planmysem.model.slot.ReadOnlySlot;
import planmysem.model.slot.Slot;


/**
 * Finds all slots in planner whose name contains the argument keyword.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_WORD_SHORT = "f";
    private static final String MESSAGE_SUCCESS = "%1$s Slots listed.\n%2$s";
    private static final String MESSAGE_SUCCESS_NONE = "0 Slots listed.\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds all slots whose name "
            + "contains the specified keywords (case-sensitive).\n\t"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n\t"
            + "Example: " + COMMAND_WORD + "n/CS";

    private final String keyword;
    private final boolean isFindByName;

    public FindCommand(String name, String tag) {
        this.keyword = (name == null) ? tag.trim() : name.trim();
        this.isFindByName = (name != null);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory commandHistory) {
        Map<LocalDate, Pair<ReadOnlyDay, ReadOnlySlot>> selectedSlots = new TreeMap<>();

        for (Map.Entry<LocalDate, Day> entry : model.getDays().entrySet()) {
            for (Slot slot : entry.getValue().getSlots()) {
                if (isFindByName) {
                    if (Pattern.matches(".*" + keyword + ".*", slot.getName())) {
                        selectedSlots.put(entry.getKey(), new Pair<>(entry.getValue(), slot));
                    }
                } else {
                    Set<String> tagSet = slot.getTags();
                    for (String tag : tagSet) {
                        if (Pattern.matches(".*" + keyword + ".*", tag)) {
                            selectedSlots.put(entry.getKey(), new Pair<>(entry.getValue(), slot));
                        }
                    }
                }
            }
        }

        if (selectedSlots.isEmpty()) {
            return new CommandResult(MESSAGE_SUCCESS_NONE);
        }
        model.setLastShownList(selectedSlots);

        return new CommandResult(String.format(MESSAGE_SUCCESS, selectedSlots.size(),
                Messages.craftSelectedMessage(selectedSlots)));
    }
}
