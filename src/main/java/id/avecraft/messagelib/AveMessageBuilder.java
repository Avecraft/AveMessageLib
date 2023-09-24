package id.avecraft.messagelib;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class AveMessageBuilder {
    protected final String PLACEHOLDER;
    protected final File file;

    public AveMessageBuilder(File file, String placeholder) {
        this.file = file;
        this.PLACEHOLDER = placeholder;
    }

    Map<String, AveMessage> messages = new LinkedHashMap<>();

    public AveMessageBuilder register(AveMessage aveMessage) {
        if (messages.containsKey(aveMessage.path)) {
            throw new IllegalArgumentException("You already registered a message with path: \"" + aveMessage.path + "\"");
        }
        messages.put(aveMessage.path, aveMessage);
        HashSet<String> checkSameParams = new HashSet<>();
        if(aveMessage.params != null){
            for(var param : aveMessage.params){
                if(checkSameParams.contains(param)){
                    throw new IllegalArgumentException("You already use a param \"" + param + "\" in path ");
                } else {
                    checkSameParams.add(param);
                }
            }
        }
        return this;
    }

    public AveMessages build() throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }
        return new AveMessages(this);
    }

}
