package duedate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DueDateCalculatorTest {

    DueDateCalculator calculator = new DueDateCalculator();

    @DisplayName("Due date/time tests")
    @ParameterizedTest(name = "Submission date: {0}; turnaround: {1}; due: {2}")
    @MethodSource("getDueDateValues")
    void testDueDate(LocalDateTime submission, int turnaroundHours, LocalDateTime due) {
        assertThat(calculator.calculateDueDate(submission, turnaroundHours))
                .isEqualTo(due);
    }

    static Stream<Arguments> getDueDateValues() {
        return Stream.of(
                arguments(LocalDateTime.parse("2022-05-12T10:12"), 6, LocalDateTime.parse("2022-05-12T16:12")),
                arguments(LocalDateTime.parse("2022-05-12T10:12"), 7, LocalDateTime.parse("2022-05-13T09:12")),
                arguments(LocalDateTime.parse("2022-05-12T10:12"), 8, LocalDateTime.parse("2022-05-13T10:12")),
                arguments(LocalDateTime.parse("2022-05-13T16:12"), 1, LocalDateTime.parse("2022-05-16T09:12")),
                arguments(LocalDateTime.parse("2022-05-12T10:12"), 24, LocalDateTime.parse("2022-05-17T10:12")),
                arguments(LocalDateTime.parse("2022-05-27T09:00"), 8, LocalDateTime.parse("2022-05-27T17:00")),
                arguments(LocalDateTime.parse("2022-05-27T09:00"), 40, LocalDateTime.parse("2022-06-02T17:00")),
                arguments(LocalDateTime.parse("2022-05-30T09:00"), 40, LocalDateTime.parse("2022-06-03T17:00")),
                arguments(LocalDateTime.parse("2022-05-30T09:01"), 40, LocalDateTime.parse("2022-06-06T09:01")),
                arguments(LocalDateTime.parse("2022-05-30T17:00"), 40, LocalDateTime.parse("2022-06-06T17:00"))
        );
    }

    @DisplayName("Submission date/time given outside of working hours")
    @ParameterizedTest(name = "Submission date: {0}")
    @MethodSource("getNonWorkingHourValues")
    void testSubmissionOutsideWorkingHours(LocalDateTime submission, String message) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculator.calculateDueDate(submission, 8))
                .withMessage(message);
    }

    static Stream<Arguments> getNonWorkingHourValues() {
        return Stream.of(
                arguments(LocalDateTime.parse("2022-05-12T08:59"),
                        "Submission time of 08:59 outside of working hours; working hours are 09:00 to 17:00"),
                arguments(LocalDateTime.parse("2022-05-12T17:01"),
                        "Submission time of 17:01 outside of working hours; working hours are 09:00 to 17:00"),
                arguments(LocalDateTime.parse("2022-05-28T10:12"),
                        "Submission date of 2022-05-28 (SATURDAY) outside of working days; working days are " +
                                "[MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY]"),
                arguments(LocalDateTime.parse("2022-05-29T10:12"),
                        "Submission date of 2022-05-29 (SUNDAY) outside of working days; working days are " +
                                "[MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY]")
        );
    }

    @DisplayName("Invalid turnaround time given")
    @ParameterizedTest(name = "Turnaround hours: {0}")
    @MethodSource("getInvalidTurnaroundHours")
    void testInvalidTurnaroundHours(int turnaroundHours, String message) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculator.calculateDueDate(LocalDateTime.parse("2022-05-12T10:12"), turnaroundHours))
                .withMessage(message);
    }

    static Stream<Arguments> getInvalidTurnaroundHours() {
        return Stream.of(
                arguments(0, "Turnaround time 0 is not a positive integer"),
                arguments(-1, "Turnaround time -1 is not a positive integer")
        );
    }
}
