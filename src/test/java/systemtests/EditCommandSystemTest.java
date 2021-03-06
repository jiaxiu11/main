package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LABEL_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_VALUE_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LABEL;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalTasks.KEYWORD_MATCHING_TUTORIAL;
import static seedu.address.testutil.TypicalTasks.Y_TASK;
import static seedu.address.testutil.TypicalTasks.Z_TASK;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
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

public class EditCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_TASK;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + PRIORITY_VALUE_DESC_BOB + "  " + DESCRIPTION_DESC_BOB + " "
                + LABEL_DESC_HUSBAND + " ";
        Task editedPerson = new TaskBuilder(Z_TASK).withLabels(VALID_LABEL_HUSBAND).build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: undo editing the last task in the list -> last task restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last task in the list -> last task edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateTask(
                getModel().getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()), editedPerson);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a task with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        assertCommandSuccess(command, index, Z_TASK);

        /* Case: edit a task with new values same as another task's values but with different name -> edited */
        assertTrue(getModel().getTaskManager().getTaskList().contains(Z_TASK));
        index = INDEX_SECOND_TASK;
        assertNotEquals(getModel().getFilteredTaskList().get(index.getZeroBased()), Z_TASK);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        editedPerson = new TaskBuilder(Z_TASK).withName(VALID_NAME_AMY).build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: edit a task with new values same as another task's values but with different phone and email
         * -> edited
         */
        index = INDEX_SECOND_TASK;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_AMY
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        editedPerson = new TaskBuilder(Z_TASK).withDueDate(VALID_DUEDATE_AMY)
                .withPriorityValue(VALID_PRIORITY_VALUE_AMY)
                .build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: clear labels -> cleared */
        index = INDEX_FIRST_TASK;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_LABEL.getPrefix();
        Task personToEdit = getModel().getFilteredTaskList().get(index.getZeroBased());
        editedPerson = new TaskBuilder(personToEdit).withLabels().build();
        assertCommandSuccess(command, index, editedPerson);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered task list, edit index within bounds of task manager and task list -> edited */
        showPersonsWithName(KEYWORD_MATCHING_TUTORIAL);
        index = INDEX_FIRST_TASK;
        assertTrue(index.getZeroBased() < getModel().getFilteredTaskList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        personToEdit = getModel().getFilteredTaskList().get(index.getZeroBased());
        editedPerson = new TaskBuilder(personToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: filtered task list, edit index within bounds of task manager but out of bounds of task list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_TUTORIAL);
        int invalidIndex = getModel().getTaskManager().getTaskList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a task card is selected -------------------------- */

        /* Case: selects first card in the task list, edit a task -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllPersons();
        index = INDEX_FIRST_TASK;
        selectPerson(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY
                + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_AMY
                + DESCRIPTION_DESC_AMY + LABEL_DESC_FRIEND;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new task's name
        assertCommandSuccess(command, index, Y_TASK, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredTaskList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_TASK.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_TASK.getOneBased() + INVALID_PHONE_DESC,
                DueDate.MESSAGE_DUEDATE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_TASK.getOneBased() + INVALID_PRIORITY_VALUE_DESC,
                PriorityValue.MESSAGE_PRIORITY_VALUE_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_TASK.getOneBased() + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        /* Case: invalid label -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_TASK.getOneBased() + INVALID_LABEL_DESC,
                Label.MESSAGE_LABEL_CONSTRAINTS);

        /* Case: edit a task with new values same as another task's values -> rejected */
        executeCommand(TaskUtil.getAddCommand(Z_TASK));
        assertTrue(getModel().getTaskManager().getTaskList().contains(Z_TASK));
        index = INDEX_FIRST_TASK;
        assertFalse(getModel().getFilteredTaskList().get(index.getZeroBased()).equals(Z_TASK));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: edit a task with new values same as another task's values but with different labels -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: edit a task with new values same as another task's values but with different address -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_AMY + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: edit a task with new values same as another task's values but with different phone -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: edit a task with new values same as another task's values but with different email -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB
                + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_AMY
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND + LABEL_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Task, Index)} except that
     * the browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Task, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Task editedPerson) {
        assertCommandSuccess(command, toEdit, editedPerson, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the task at index {@code toEdit} being
     * updated to values specified {@code editedPerson}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Task editedPerson,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateTask(expectedModel.getFilteredTaskList().get(toEdit.getZeroBased()), editedPerson);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
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
