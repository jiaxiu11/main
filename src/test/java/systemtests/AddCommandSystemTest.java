package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LABEL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_VALUE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LABEL_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.LABEL_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_VALUE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_VALUE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_VALUE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LABEL;
import static seedu.address.testutil.TypicalTasks.A_TASK;
import static seedu.address.testutil.TypicalTasks.C_TASK;
import static seedu.address.testutil.TypicalTasks.H_TASK;
import static seedu.address.testutil.TypicalTasks.I_TASK;
import static seedu.address.testutil.TypicalTasks.KEYWORD_MATCHING_TUTORIAL;
import static seedu.address.testutil.TypicalTasks.Y_TASK;
import static seedu.address.testutil.TypicalTasks.Z_TASK;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Label;
import seedu.address.model.task.Description;
import seedu.address.model.task.DueDate;
import seedu.address.model.task.Name;
import seedu.address.model.task.PriorityValue;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TaskUtil;

public class AddCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a task without labels to a non-empty task manager,
         * command with leading spaces and trailing spaces
         * -> added
         */
        Task toAdd = Y_TASK;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " "
                + PRIORITY_VALUE_DESC_AMY + "   " + DESCRIPTION_DESC_AMY + "   " + LABEL_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addTask(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a task with all fields same as another task in the task manager except name -> added */
        toAdd = new TaskBuilder(Y_TASK).withName(VALID_NAME_BOB).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_AMY
                + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY
                + LABEL_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task with all fields same as another task in the task manager except phone and email
         * -> added
         */
        toAdd = new TaskBuilder(Y_TASK).withDueDate(VALID_DUEDATE_BOB)
                .withPriorityValue(VALID_PRIORITY_VALUE_BOB).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty task manager -> added */
        deleteAllPersons();
        assertCommandSuccess(A_TASK);

        /* Case: add a task with labels, command with parameters in random order -> added */
        toAdd = Z_TASK;
        command = AddCommand.COMMAND_WORD + LABEL_DESC_FRIEND + PHONE_DESC_BOB + DESCRIPTION_DESC_BOB + NAME_DESC_BOB
                + LABEL_DESC_HUSBAND + PRIORITY_VALUE_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task, missing labels -> added */
        assertCommandSuccess(H_TASK);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the task list before adding -> added */
        showPersonsWithName(KEYWORD_MATCHING_TUTORIAL);
        assertCommandSuccess(I_TASK);

        /* ------------------------ Perform add operation while a task card is selected --------------------------- */

        /* Case: selects first card in the task list, add a task -> added, card selection remains unchanged */
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(C_TASK);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate task -> rejected */
        command = TaskUtil.getAddCommand(H_TASK);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different phone -> rejected */
        toAdd = new TaskBuilder(H_TASK).withDueDate(VALID_DUEDATE_BOB).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different email -> rejected */
        toAdd = new TaskBuilder(H_TASK).withPriorityValue(VALID_PRIORITY_VALUE_BOB).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different address -> rejected */
        toAdd = new TaskBuilder(H_TASK).withDescription(VALID_DESCRIPTION_BOB).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different labels -> rejected */
        command = TaskUtil.getAddCommand(H_TASK) + " " + PREFIX_LABEL.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + TaskUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_AMY
                + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PHONE_DESC
                + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, DueDate.MESSAGE_DUEDATE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + INVALID_PRIORITY_VALUE_DESC + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, PriorityValue.MESSAGE_PRIORITY_VALUE_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + PRIORITY_VALUE_DESC_AMY + INVALID_DESCRIPTION_DESC;
        assertCommandFailure(command, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        /* Case: invalid label -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY
                + INVALID_LABEL_DESC;
        assertCommandFailure(command, Label.MESSAGE_LABEL_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Task toAdd) {
        assertCommandSuccess(TaskUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Task)}. Executes {@code command}
     * instead.
     *
     * @see AddCommandSystemTest#assertCommandSuccess(Task)
     */
    private void assertCommandSuccess(String command, Task toAdd) {
        Model expectedModel = getModel();
        expectedModel.addTask(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddCommandSystemTest#assertCommandSuccess(String, Task)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
