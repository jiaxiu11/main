package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Label in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidLabelName(String)}
 */
public class Label {

    public static final String MESSAGE_LABEL_CONSTRAINTS = "Labels names should be alphanumeric";
    public static final String LABEL_VALIDATION_REGEX = "\\p{Alnum}+";

    public final String labelName;

    /**
     * Constructs a {@code Label}.
     *
     * @param labelName A valid label name.
     */
    public Label(String labelName) {
        requireNonNull(labelName);
        checkArgument(isValidLabelName(labelName), MESSAGE_LABEL_CONSTRAINTS);
        this.labelName = labelName;
    }

    /**
     * Returns true if a given string is a valid label name.
     */
    public static boolean isValidLabelName(String test) {
        return test.matches(LABEL_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Label // instanceof handles nulls
                && labelName.equals(((Label) other).labelName)); // state check
    }

    @Override
    public int hashCode() {
        return labelName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + labelName + ']';
    }

}
