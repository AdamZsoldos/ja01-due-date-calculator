package duedate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SubmissionDateValidator {

    public void validate(LocalDateTime submission) {
        validateDay(submission.toLocalDate());
        validateTime(submission.toLocalTime());
    }

    private void validateDay(LocalDate submissionDate) {
        DayOfWeek submissionDay = submissionDate.getDayOfWeek();
        if (!DueDateCalculator.WORKING_DAYS.contains(submissionDay)) {
            throw new IllegalArgumentException(String.format(
                    "Submission date of %s (%s) outside of working days; working days are %s",
                    submissionDate,
                    submissionDay,
                    DueDateCalculator.WORKING_DAYS
            ));
        }
    }

    private void validateTime(LocalTime submissionTime) {
        if (submissionTime.isBefore(DueDateCalculator.WORKING_HOURS_START) ||
                submissionTime.isAfter(DueDateCalculator.WORKING_HOURS_END)) {
            throw new IllegalArgumentException(String.format(
                    "Submission time of %s outside of working hours; working hours are %s to %s",
                    submissionTime,
                    DueDateCalculator.WORKING_HOURS_START,
                    DueDateCalculator.WORKING_HOURS_END
            ));
        }
    }
}
