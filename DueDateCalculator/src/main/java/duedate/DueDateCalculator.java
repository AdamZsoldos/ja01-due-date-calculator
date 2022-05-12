package duedate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DueDateCalculator {

    public static final List<DayOfWeek> WORKING_DAYS = List.of(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

    public static final LocalTime WORKING_HOURS_START = LocalTime.parse("09:00");

    public static final LocalTime WORKING_HOURS_END = LocalTime.parse("17:00");

    private static final int WORKING_HOURS_PER_DAY = WORKING_HOURS_END.getHour() - WORKING_HOURS_START.getHour();

    public LocalDateTime calculateDueDate(LocalDateTime submissionDate, int turnaroundHours) {
        new SubmissionDateValidator().validate(submissionDate);
        new TurnaroundTimeValidator().validate(turnaroundHours);
        LocalDateTime due = submissionDate;
        for (int hours = turnaroundHours; hours > 0; hours--) {
            due = addOneHour(due);
        }
        return due;
    }

    private LocalDateTime addOneHour(LocalDateTime due) {
        LocalDateTime result = due.plusHours(1);
        if (result.toLocalTime().isAfter(WORKING_HOURS_END)) {
            result = result.minusHours(WORKING_HOURS_PER_DAY);
            result = addOneDay(result);
        }
        return result;
    }

    private LocalDateTime addOneDay(LocalDateTime due) {
        LocalDateTime result = due.plusDays(1);
        while (!WORKING_DAYS.contains(result.getDayOfWeek())) {
            result = result.plusDays(1);
        }
        return result;
    }
}
