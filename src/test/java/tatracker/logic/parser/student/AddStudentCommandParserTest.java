package tatracker.logic.parser.student;

import static tatracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tatracker.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static tatracker.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static tatracker.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static tatracker.logic.commands.CommandTestUtil.INVALID_MATRIC_DESC;
import static tatracker.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tatracker.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static tatracker.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tatracker.logic.commands.CommandTestUtil.MATRIC_DESC_AMY;
import static tatracker.logic.commands.CommandTestUtil.MATRIC_DESC_BOB;
import static tatracker.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tatracker.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static tatracker.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static tatracker.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static tatracker.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static tatracker.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tatracker.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static tatracker.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static tatracker.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tatracker.logic.commands.CommandTestUtil.VALID_MATRIC_BOB;
import static tatracker.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tatracker.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tatracker.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static tatracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static tatracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tatracker.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tatracker.testutil.TypicalStudents.AMY;
import static tatracker.testutil.TypicalStudents.BOB;

import org.junit.jupiter.api.Test;

import tatracker.logic.commands.student.AddStudentCommand;
import tatracker.model.student.Email;
import tatracker.model.student.Matric;
import tatracker.model.student.Name;
import tatracker.model.student.Phone;
import tatracker.model.student.Student;
import tatracker.model.tag.Tag;
import tatracker.testutil.StudentStringBuilder;

public class AddStudentCommandParserTest {
    private AddStudentCommandParser parser = new AddStudentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentStringBuilder(BOB)
                .withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + MATRIC_DESC_BOB + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudent));

        // multiple names - last name accepted
        assertParseSuccess(parser,
                NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + MATRIC_DESC_BOB + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudent));

        // multiple phones - last phone accepted
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + MATRIC_DESC_BOB + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudent));

        // multiple emails - last email accepted
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                        + MATRIC_DESC_BOB + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudent));

        // multiple matric numbers - last matric number accepted
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_AMY
                        + MATRIC_DESC_BOB + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudent));

        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new StudentStringBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new StudentStringBuilder(AMY).withTags().build();
        assertParseSuccess(parser,
                NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_DESC_AMY,
                new AddStudentCommand(expectedStudent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + MATRIC_DESC_BOB,
                expectedMessage);

        // missing matric prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_MATRIC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + MATRIC_DESC_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + MATRIC_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + MATRIC_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Email.MESSAGE_CONSTRAINTS);

        // invalid matric
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_MATRIC_DESC
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Matric.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB
                        + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + MATRIC_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE));
    }
}
