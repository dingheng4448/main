package planmysem.model;

import java.time.LocalDate;
import java.util.HashMap;

import planmysem.model.semester.Day;

/**
 * Unmodifiable view of a Planner
 */
public interface ReadOnlyPlanner {

    /**
     * Returns an unmodifiable view of all days.
     */
    HashMap<LocalDate, Day> getDays();

}
