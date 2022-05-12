package duedate;

public class TurnaroundTimeValidator {

    public void validate(int turnaroundHours) {
        if (turnaroundHours < 1) {
            throw new IllegalArgumentException(String.format(
                    "Turnaround time %d is not a positive integer", turnaroundHours
            ));
        }
    }
}
