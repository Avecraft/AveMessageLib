package id.avecraft.messagelib;

public class AveMessage {
    public final String path;
    public final String message;

    public final String[] params;

    public AveMessage(String path, String message, String ...params) {
        this.path = path;
        this.message = message;
        this.params = params;
    }
}
