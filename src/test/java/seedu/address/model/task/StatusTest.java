package seedu.address.model.task;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class StatusTest {

    @Test
    public void statusValues() {
        Status[] expectedValues = {Status.IN_PROGRESS, Status.COMPLETED, Status.OVERDUE};
        assertArrayEquals(expectedValues, Status.values());
    }

    @Test
    public void isValidStatus() {
        //null status value
        assertFalse(Status.isValidStatus(null));

        //invalid status value
        assertFalse(Status.isValidStatus("work in progress"));
        assertFalse(Status.isValidStatus("finished"));
        assertFalse(Status.isValidStatus("overdueee"));

        //valid status value
        assertTrue(Status.isValidStatus("IN PROGRESS"));
        assertTrue(Status.isValidStatus("COMPLETED"));
        assertTrue(Status.isValidStatus("OVERDUE"));
    }

    @Test
    public void getStatusFromValue() {
        //invalid status value
        Assert.assertThrows(IllegalArgumentException.class, () -> Status.getStatusFromValue("in progress"));
        Assert.assertThrows(IllegalArgumentException.class, () -> Status.getStatusFromValue("hello"));

        //valid status value
        assertEquals(Status.getStatusFromValue("IN PROGRESS"), Status.IN_PROGRESS);
        assertEquals(Status.getStatusFromValue("COMPLETED"), Status.COMPLETED);
        assertEquals(Status.getStatusFromValue("OVERDUE"), Status.OVERDUE);
    }

    @Test
    public void toStringEquals() {
        assertEquals(Status.IN_PROGRESS.toString(), "IN PROGRESS");
        assertEquals(Status.COMPLETED.toString(), "COMPLETED");
        assertEquals(Status.OVERDUE.toString(), "OVERDUE");
    }
}
