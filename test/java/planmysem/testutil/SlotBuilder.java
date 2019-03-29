package planmysem.testutil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import planmysem.common.Utils;
import planmysem.model.recurrence.Recurrence;
import planmysem.model.slot.Slot;

/**
 * A utility class to generate test data.
 */
public class SlotBuilder{

    public Slot slotOne() {
        String name = "CS2113T Tutorial";
        String location = "COM2 04-11";
        String description = "Topic: Sequence Diagram";
        LocalTime startTime = LocalTime.parse("08:00");
        LocalTime endTime = LocalTime.parse("09:00");
        Set<String> tags = new HashSet<>(Arrays.asList( "CS2113T", "Tutorial"));
        return new Slot(name, location, description, startTime, endTime, tags);
    }

    public Recurrence recurrenceOne() {
        return new Recurrence(
                new HashSet<>(Arrays.asList("CS2113T", "Tutorial")),
                LocalDate.of(2019, 2, 1)
        );
    }

    /**
     * Generates a valid slot using the given seed.
     * Running this function with the same parameter values guarantees the returned slot will have the same state.
     * Each unique seed will generate a unique slot object.
     *
     * @param seed used to generate the person data field values
     */
    public Slot generateSlot(int seed) throws Exception {
        return new Slot(
                "slot " + seed,
                "location " + Math.abs(seed),
                "description " + Math.abs(seed),
                LocalTime.parse("00:00"),
                LocalTime.parse("00:00"),
                new HashSet<>(Arrays.asList("tag" + Math.abs(seed), "tag" + Math.abs(seed + 1)))
        );
    }

    /** Generates the correct add command based on the person given */
    String generateAddCommand(Slot s, LocalDate date, String recurrence) {
        StringJoiner cmd = new StringJoiner(" ");

        cmd.add("add");

        cmd.add("n/" + s.getName());
        cmd.add("d/" + Utils.parseDate(date));
        cmd.add("st/" + s.getStartTime());
        cmd.add("et/" + s.getDuration());
        if (s.getLocation() != null) {
            cmd.add("l/" + s.getLocation());
        }
        if (s.getDescription() != null) {
            cmd.add("des/" + s.getDescription());
        }

        Set<String> tags = s.getTags();
        if (tags != null) {
            for(String tag : tags){
                cmd.add("t/" + tag);
            }
        }

        cmd.add(recurrence);

        return cmd.toString();
    }

    /** Generates the correct add command based on the person given */
    String generateAddCommand(Slot s, int day, String recurrence) {
        StringJoiner cmd = new StringJoiner(" ");

        cmd.add("add");

        cmd.add("n/" + s.getName());
        cmd.add("d/" + day);
        cmd.add("st/" + s.getStartTime());
        cmd.add("et/" + s.getDuration());
        if (s.getLocation() != null) {
            cmd.add("l/" + s.getLocation());
        }
        if (s.getDescription() != null) {
            cmd.add("des/" + s.getDescription());
        }

        Set<String> tags = s.getTags();
        if (tags != null) {
            for(String tag : tags){
                cmd.add("t/" + tag);
            }
        }

        cmd.add(recurrence);

        return cmd.toString();
    }

    /** Generates the correct delete command based on tags */
    String generateDeleteCommand(Set<String> tags) {
        StringJoiner cmd = new StringJoiner(" ");

        cmd.add("delete");

        if (tags != null) {
            for(String tag : tags){
                cmd.add("t/" + tag);
            }
        }

        return cmd.toString();
    }

    /** Generates the correct delete command based on the slot. */
    String generateDeleteCommand(Slot slot) {
        StringJoiner cmd = new StringJoiner(" ");

        cmd.add("delete");

        Set<String> tags = slot.getTags();
        if (tags != null) {
            for(String tag : tags){
                cmd.add("t/" + tag);
            }
        }

        return cmd.toString();
    }
}