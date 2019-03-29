package planmysem.logic.parser;

import static planmysem.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static planmysem.logic.parser.CommandParserTestUtil.assertParseFailure;
import static planmysem.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import planmysem.common.Clock;
import planmysem.logic.commands.EditCommand;

public class EditCommandParserTest {
    private EditCommandParser parser = new EditCommandParser();

    @Before
    public void setup() {
        Clock.set("2019-01-13T10:00:00Z");
    }

    @Test
    public void parse_validTags_success() {
        // single tag
        assertParseSuccess(parser,
                "t/CS2113T nl/COM2 04-01",
                new EditCommand(
                        null,
                        null,
                        -1,
                        "COM2 04-01",
                        null,
                        new HashSet<>(Arrays.asList("CS2113T")),
                        new HashSet<>()
                )
        );

        // multiple tags
        assertParseSuccess(parser,
                "t/CS2113T t/Tutorial nst/08:00 net/09:00 t/Hard",
                new EditCommand(
                        null,
                        LocalTime.of(8,0),
                        60,
                        null,
                        null,
                        new HashSet<>(Arrays.asList("CS2113T", "Tutorial", "Hard")),
                        new HashSet<>()
                )
        );

        // multiple tags all input
        assertParseSuccess(parser,
                "nn/CS2113T nst/08:00 net/75 ndes/So tough nt/new tag t/CS2113T t/Tutorial nl/COM2 04-01 t/Hard",
                new EditCommand(
                        "CS2113T",
                        LocalTime.of(8, 0),
                        75,
                        "COM2 04-01",
                        "So tough",
                        new HashSet<>(Arrays.asList("CS2113T", "Tutorial", "Hard")),
                        new HashSet<>(Arrays.asList("new tag"))
                )
        );
    }

    @Test
    public void parse_validIndex_success() {
        assertParseSuccess(parser,
                "1 nl/COM2 04-01",
                new EditCommand(
                        1,
                        null,
                        null,
                        null,
                        -1,
                        "COM2 04-01",
                        null,
                        new HashSet<>()
                )
        );

        assertParseSuccess(parser,
                "100 nl/COM2 04-01",
                new EditCommand(
                        100,
                        null,
                        null,
                        null,
                        -1,
                        "COM2 04-01",
                        null,
                        new HashSet<>()
                )
        );
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                "0",
                expectedMessage
        );

        assertParseFailure(parser,
                "-1",
                expectedMessage
        );
    }

    @Test
    public void parse_noIndexNoTag_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                "nl/COM2 04-01",
                expectedMessage
        );

        assertParseFailure(parser,
                "",
                expectedMessage
        );
    }

    @Test
    public void parse_InvalidStartTime_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                "1 nst/25:00",
                expectedMessage
        );
    }

    @Test
    public void parse_InvalidEndTime_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                "1 net/25:00",
                expectedMessage
        );
    }
}
