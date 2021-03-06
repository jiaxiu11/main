package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LABEL_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LABEL_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_VALUE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_VALUE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_IN_PROGRESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.TaskManager;
import seedu.address.model.task.Status;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {

    public static final Task A_TASK = new TaskBuilder().withName("Address CS2103 email")
            .withDescription("Reply to Damith").withPriorityValue("1")
            .withDueDate("01-12-18")
            .withLabels("friends").build();
    public static final Task B_TASK = new TaskBuilder().withName("Build addressbook for tutorial")
            .withDescription("Press 'Build' in IntelliJ")
            .withPriorityValue("2").withDueDate("02-12-18 1330")
            .withLabels("owesMoney", "friends").withStatus(Status.IN_PROGRESS).build();
    public static final Task C_TASK = new TaskBuilder().withName("Cancel medical appointment").withDueDate("03-12-18")
            .withPriorityValue("3").withDescription("Call number +6562353535")
            .withStatus(Status.IN_PROGRESS).build();
    public static final Task D_TASK = new TaskBuilder().withName("Do CS2106 tutorial").withDueDate("04-12-18")
            .withPriorityValue("4").withDescription("Preferably before it").withLabels("friends").build();
    public static final Task E_TASK = new TaskBuilder().withName("Edit research report").withDueDate("05-12-18")
            .withPriorityValue("5").withDescription("Add more words")
            .withStatus(Status.COMPLETED).build();
    public static final Task F_TASK = new TaskBuilder().withName("Find suitable reagents for experiment")
            .withDueDate("06-12-2018").withPriorityValue("6").withDescription("Missing spice and everything nice")
            .withStatus(Status.IN_PROGRESS).build();
    public static final Task G_TASK = new TaskBuilder().withName("Grade assignments").withDueDate("07-12-18")
            .withPriorityValue("7").withDescription("Then enter grades into IVLE").build();

    // Manually added
    public static final Task H_TASK = new TaskBuilder().withName("Hack Website").withDueDate("08-12-18 0900")
            .withPriorityValue("8").withDescription("Step 2) ??? Step 3) PROFIT!!!").build();
    public static final Task I_TASK = new TaskBuilder().withName("Investigate Murder").withDueDate("09-12-18")
            .withPriorityValue("9").withDescription("Destroy all evidence pointing to myself").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task Y_TASK = new TaskBuilder().withName(VALID_NAME_AMY).withDueDate(VALID_DUEDATE_AMY)
            .withPriorityValue(VALID_PRIORITY_VALUE_AMY)
            .withDescription(VALID_DESCRIPTION_AMY)
            .withLabels(VALID_LABEL_FRIEND)
            .withStatus(VALID_STATUS_IN_PROGRESS).build();
    public static final Task Z_TASK = new TaskBuilder().withName(VALID_NAME_BOB)
            .withDueDate(VALID_DUEDATE_BOB)
            .withPriorityValue(VALID_PRIORITY_VALUE_BOB)
            .withDescription(VALID_DESCRIPTION_BOB)
            .withLabels(VALID_LABEL_HUSBAND, VALID_LABEL_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_TUTORIAL = "tutorial"; // A keyword that matches tutorial

    private TypicalTasks() {
    } // prevents instantiation

    /**
     * Returns an {@code TaskManager} with all the typical tasks.
     */
    public static TaskManager getTypicalTaskManager() {
        TaskManager tm = new TaskManager();
        for (Task task : getTypicalTasks()) {
            tm.addTask(task);
        }
        return tm;
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(A_TASK, B_TASK, C_TASK, D_TASK, E_TASK, F_TASK, G_TASK));
    }
}
