package id.avecraft.messagelib;

import org.bukkit.plugin.Plugin;
import java.io.File;

public class AveMessageLib {
    public static AveMessageBuilder createBuilder(Plugin plugin) {
        File newFile = new File(plugin.getDataFolder() + "/message.yml");
        return new AveMessageBuilder(newFile, "<%>");
    }
}
