package planmysem.logic.commands;

import java.time.LocalDate;
import java.util.HashMap;

import planmysem.logic.CommandHistory;
import planmysem.model.Model;
import planmysem.model.semester.Day;
import planmysem.model.semester.Semester;

/**
 * Adds a person to the address book.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";
    public static final String COMMAND_WORD_SHORT = "v";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View month/week/day view or all details of planner."
            + "\n\tParameters: "
            + "\n\t\tMandatory: [viewType] [specifier]"
            + "\n\tExample 1: " + COMMAND_WORD
            + " month"
            + "\n\tExample 2: " + COMMAND_WORD
            + " week 7"
            + "\n\tExample 3: " + COMMAND_WORD
            + " week recess"
            + "\n\tExample 4: " + COMMAND_WORD
            + " day 01/03/2019"
            + "\n\tExample 5: " + COMMAND_WORD
            + " all";

    private final String viewArgs;

    public ViewCommand(String viewArgs) {
        this.viewArgs = viewArgs;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory commandHistory) {
        String viewType;
        //String viewSpecifier;
        final Semester currentSemester = model.getPlanner().getSemester();
        String output = null;

        if ("all".equals(viewArgs)) {
            //TODO: print all planner details
            output = "all";
        } else if ("month".equals(viewArgs)) {
            output = displayMonthView(currentSemester);
        } else {
            viewType = viewArgs.split(" ")[0];
            //viewSpecifier = viewArgs.split(" ")[1];

            switch (viewType) {
            case "month":
                //TODO: month view
                break;
            case "week":
                //TODO: week view
                break;
            case "day":
                //TODO: day view
                break;
            default:
                break;
            }
        }

        return new CommandResult(output);
    }

    /**
     * Display all months for the semester.
     */
    private String displayMonthView(Semester currentSemester) {
        HashMap<LocalDate, Day> allDays = currentSemester.getDays();
        LocalDate semesterStartDate = currentSemester.getStartDate();
        LocalDate semesterEndDate = currentSemester.getEndDate();
        int year = semesterStartDate.getYear();
        LocalDate firstDayOfMonth = semesterStartDate.withDayOfMonth(1);
        int spaces = firstDayOfMonth.getDayOfWeek().getValue();
        int lastMonthOfSem = semesterEndDate.getMonthValue();
        StringBuilder sb = new StringBuilder();

        String[] months = {"", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

        int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        for (int m = 1; m <= lastMonthOfSem; m++) {
            // Set number of days in February to 29 if it is a leap year.
            if ((((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) && m == 2) {
                days[m] = 29;
            }

            // Print calendar header.
            sb.append("          " + months[m] + " " + year + "\n");
            sb.append("_____________________________________\n");
            sb.append("   Sun  Mon Tue   Wed Thu   Fri  Sat\n");

            // Print spaces required for the start of a month.
            spaces = (days[m - 1] + spaces) % 7;
            for (int i = 0; i < spaces; i++) {
                sb.append("     ");
            }
            // Print the days in the month.
            for (int i = 1; i <= days[m]; i++) {
                sb.append(String.format("  %3d", i));
                if (((i + spaces) % 7 == 0)) {
                    Day tempDay = allDays.get(LocalDate.of(year, m, i));
                    String weekType = "";
                    if (tempDay != null) {
                        weekType = tempDay.getType();
                    }
                    sb.append("   | " + weekType + "\n");
                }
                if (i == days[m]) {
                    LocalDate tempDate = LocalDate.of(year, m, i);
                    Day tempDay = allDays.get(tempDate);
                    String weekType = "";
                    int extraSpaces = 6 - (tempDate.getDayOfWeek().getValue() % 7);
                    for (int j = 0; j < extraSpaces; j++) {
                        sb.append("     ");
                    }
                    if (tempDay != null) {
                        weekType = tempDay.getType();
                    }
                    sb.append("   | " + weekType + "\n");
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
