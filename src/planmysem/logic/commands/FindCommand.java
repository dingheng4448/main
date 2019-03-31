//@@author marcus-pzj
package planmysem.logic.commands;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import planmysem.common.Messages;
import planmysem.common.Utils;
import planmysem.logic.CommandHistory;
import planmysem.model.Model;
import planmysem.model.semester.Day;
import planmysem.model.semester.WeightedName;
import planmysem.model.slot.Slot;

/**
 * Finds all slots in planner whose name contains the argument keyword.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_WORD_SHORT = "f";
    private static final String MESSAGE_SUCCESS = "%1$s Slots found.\n%2$s";
    private static final String MESSAGE_SUCCESS_NONE = "0 Slots listed.\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds all slots whose name "
            + "contains the specified keywords (case-sensitive).\n\t"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n\t"
            + "Example: " + COMMAND_WORD + "n/CS";

    private final String keyword;
    private final boolean isFindByName;

    private int minDistance = Integer.MAX_VALUE;

    private Queue<WeightedName> weightedNames = new PriorityQueue<>(new Comparator<>() {
        @Override
        public int compare(WeightedName p1, WeightedName p2) {
            String n1 = p1.getName();
            String n2 = p2.getName();
            int d1 = p1.getDist();
            int d2 = p2.getDist();

            if (d1 != d2) {
                return d1 - d2;
            } else {
                return n1.compareTo(n2);
            }
        }
    });

    private Set<WeightedName> selectedNames = new TreeSet<>(new Comparator<WeightedName>() {
        @Override
        public int compare(WeightedName p1, WeightedName p2) {
            String n1 = p1.getName();
            String n2 = p2.getName();
            int d1 = p1.getDist();
            int d2 = p2.getDist();

            if (d1 != d2) {
                return d1 - d2;
            } else {
                return n1.compareTo(n2);
            }
        }
    });

    public FindCommand(String name, String tag) {
        this.keyword = (name == null) ? tag.trim() : name.trim();
        this.isFindByName = (name != null);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory commandHistory) {
        for (Map.Entry<LocalDate, Day> entry : model.getDays().entrySet()) {
            for (Slot slot : entry.getValue().getSlots()) {
                if (isFindByName) {
                    generateDiscoveredNames(keyword, slot.getName());
                } else {
                    Set<String> tagSet = slot.getTags();
                    for (String tag : tagSet) {
                        generateDiscoveredNames(keyword, tag);
                    }
                }
            }
        }

        if (weightedNames.isEmpty()) {
            return new CommandResult(MESSAGE_SUCCESS_NONE);
        }

        while (!weightedNames.isEmpty() && weightedNames.peek().getDist() < minDistance + 2) {
            selectedNames.add(weightedNames.poll());
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, selectedNames.size(),
                Messages.craftSelectedMessagePair(selectedNames)));
    }

    /**
    * If a slot entry is found, calculates the Levenshtein Distance between the name and the keyword.
    * Updates the weightedNames PQ with the new WeightedName pair containing the name and its weight.
    */
    private void generateDiscoveredNames(String keyword, String compareString) {
        if (compareString == null) {
            return;
        }

        int dist = Utils.getLevenshteinDistance(keyword, compareString);
        if (dist < minDistance) {
            minDistance = dist;
        }
        //System.out.println(compareString + " vs key: " + keyword + " dist: " + dist);
        WeightedName distNamePair = new WeightedName(compareString, dist);

        for (WeightedName weightName : weightedNames) {
            if (weightName.getName().equalsIgnoreCase(distNamePair.getName())) {
                return;
            }
        }
        weightedNames.add(distNamePair);
    }
}