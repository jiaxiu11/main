package seedu.address.model.task;

/**
 * Represents a Task's status in the task manager.
 * There are two possible status: IN_PROGRESS and COMPLETED.
 */
public enum Status {
    IN_PROGRESS("IN PROGRESS"), COMPLETED("COMPLETED"), OVERDUE("OVERDUE");

    public static final String MESSAGE_STATUS_CONSTRAINTS =
            "Status should only have the value IN PROGRESS, COMPLETED or OVERDUE";
    private String statusValue;

    /**
     * Constructs a {@code Status}.
     *
     * @param statusValue A valid statusValue.
     */
    Status(String statusValue) {
        this.statusValue = statusValue;
    }

    /**
     * Returns true if a given string is a valid Status value.
     */
    public static boolean isValidStatus(String value) {
        try {
            return value.equals("IN PROGRESS") || value.equals("COMPLETED") || value.equals("OVERDUE");
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Returns the corresponding status object of {@param value}.
     */
    public static Status getStatusFromValue(String value) {
        if (!isValidStatus(value)) {
            throw new IllegalArgumentException();
        }
        return value.equals("IN PROGRESS") ? Status.IN_PROGRESS : Status.valueOf(value);
    }

    @Override
    public String toString() {
        return statusValue;
    }

}
