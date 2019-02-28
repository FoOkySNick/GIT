package packets;

public class RevertInputPacket implements InputPacket {
    public int id;
    public String version;
    public boolean flag;

    @Override
    public String getCommand() {
        return "Revert";
    }

    @Override
    public boolean hasFile() {
        return false;
    }

    @Override
    public long fileLength() {
        return 0;
    }
}
