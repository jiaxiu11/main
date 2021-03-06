package seedu.address.logic.parser;

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
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_VALUE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_VALUE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LABEL_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LABEL_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_VALUE_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTasks.Y_TASK;
import static seedu.address.testutil.TypicalTasks.Z_TASK;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.tag.Label;
import seedu.address.model.task.Description;
import seedu.address.model.task.DueDate;
import seedu.address.model.task.Name;
import seedu.address.model.task.PriorityValue;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Task expectedTask = new TaskBuilder(Z_TASK).withLabels(VALID_LABEL_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_AMY + PRIORITY_VALUE_DESC_BOB
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_AMY
                + DESCRIPTION_DESC_BOB + LABEL_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple labels - all accepted
        Task expectedTaskMultipleLabels = new TaskBuilder(Z_TASK).withLabels(VALID_LABEL_FRIEND, VALID_LABEL_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB
                + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND, new AddCommand(expectedTaskMultipleLabels));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero labels
        Task expectedTask = new TaskBuilder(Y_TASK).withLabels().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + PRIORITY_VALUE_DESC_AMY + DESCRIPTION_DESC_AMY,
                new AddCommand(expectedTask));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_DUEDATE_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_PRIORITY_VALUE_BOB + DESCRIPTION_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + VALID_DESCRIPTION_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_DUEDATE_BOB
                        + VALID_PRIORITY_VALUE_BOB + VALID_DESCRIPTION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB
                + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB
                + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND, DueDate.MESSAGE_DUEDATE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_PRIORITY_VALUE_DESC + DESCRIPTION_DESC_BOB
                + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND, PriorityValue.MESSAGE_PRIORITY_VALUE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + INVALID_DESCRIPTION_DESC
                + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // invalid label
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB + DESCRIPTION_DESC_BOB
                + INVALID_LABEL_DESC + VALID_LABEL_FRIEND, Label.MESSAGE_LABEL_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB
                        + PRIORITY_VALUE_DESC_BOB + INVALID_DESCRIPTION_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + PRIORITY_VALUE_DESC_BOB
                        + DESCRIPTION_DESC_BOB + LABEL_DESC_HUSBAND + LABEL_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
