package id.avecraft.messagelib;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Objects;

public class AveMessages {
    private final AveMessageBuilder builder;
    private FileConfiguration config;
    public AveMessages(AveMessageBuilder builder) throws IOException {
        this.builder = builder;
        reload();
        save();
    }

    void reload(){
        config = YamlConfiguration.loadConfiguration(builder.file);
        config.options().copyDefaults(true);
        for(var message: builder.messages.values()) {
            String messageTemplate = message.message;
            if(message.params != null){
                for(var param: message.params){
                    messageTemplate = messageTemplate.replaceFirst(builder.PLACEHOLDER, createPlaceholder(param));
                }
            }
            config.addDefault(message.path, messageTemplate);
        }
    }

    String parse(String path, Object ...params){
        final var aveMessage = builder.messages.get(path);
        var message = config.getString(path);
        for (int i = 0; i < aveMessage.params.length; i++) {
            final var param = aveMessage.params[i];
            final var data = params[i];
            final var placeholder = createPlaceholder(param);
            message = Objects.requireNonNull(message).replaceAll(placeholder, String.valueOf(data));
        }
        return message;
    }

    void save() throws IOException {
        config.save(builder.file);
    }

    private String createPlaceholder(String str){
        return "%" + str + "%";
    }
}
