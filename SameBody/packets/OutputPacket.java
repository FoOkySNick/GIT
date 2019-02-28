package packets;

public class OutputPacket implements ISerializable {
    public String result;

    public String command;

    public boolean hasFile;

    public long fileLength;

    @Override
    public String getCommand() {
        return command;
    }
}
