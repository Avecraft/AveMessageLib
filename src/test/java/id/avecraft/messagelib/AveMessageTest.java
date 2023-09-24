package id.avecraft.messagelib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AveMessageTest {

    private static final AveMessage MESSAGE_0 = new AveMessage("message-0", "Message 1");
    private static final AveMessage MESSAGE_1 = new AveMessage("message-1", "Message <%>", "data");
    private static final AveMessage MESSAGE_2 = new AveMessage("message-2", "Message <%> tested", "data");
    private static final AveMessage MESSAGE_3 = new AveMessage("message-3", "Message <%>, data <%>.", "data1", "data2");

    AveMessageBuilder createBuilder() {
        var builder = AveMessageLib.createBuilder(createPlugin());
        builder.register(MESSAGE_0)
                .register(MESSAGE_1)
                .register(MESSAGE_2)
                .register(MESSAGE_3);
        return builder;
    }

    @Test
    void createBuilderTest(){
        var builder = createBuilder();
        Assertions.assertNotNull(builder);
    }

    @Test
    void buildTest() throws IOException {
        var builder = createBuilder();
        builder.file.delete();
        builder.build();
        String str = Files.readString(builder.file.toPath());
        Assertions.assertEquals(str.trim(), "message-0: Message 1\n" +
                "message-1: Message %data%\n" +
                "message-2: Message %data% tested\n" +
                "message-3: Message %data1%, data %data2%."
        );
    }

    @Test
    void sameMessagePathCheckTest() {
        var builder = createBuilder();
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            builder.register(new AveMessage("message-0", "Message 1"));
        });
        Assertions.assertTrue(exception.getMessage().contains("You already registered a message with path: \"message-0\""));
    }

    @Test
    void sameParamMessageCheckTest(){
        var builder = createBuilder();
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            builder.register(new AveMessage("message-4", "Message 4", "data", "data"));
        });
        Assertions.assertTrue(exception.getMessage().contains("You already use a param \"data\" in path"));
    }

    @Test
    void parseTest() throws IOException {
        var builder = createBuilder();
        builder.file.delete();
        var messages = builder.build();
        Assertions.assertEquals(messages.parse(MESSAGE_0.path), "Message 1");
        Assertions.assertEquals(messages.parse(MESSAGE_1.path, 2), "Message 2");
        Assertions.assertEquals(messages.parse(MESSAGE_2.path,3), "Message 3 tested");
        Assertions.assertEquals(messages.parse(MESSAGE_3.path, 4, "stress"), "Message 4, data stress.");
    }

    PluginMock createPlugin(){
        File runTestFolder = Paths.get("runTest").toFile();
        if(!runTestFolder.exists()){
            runTestFolder.mkdir();
        }
        return new PluginMock(runTestFolder);
    }

}
