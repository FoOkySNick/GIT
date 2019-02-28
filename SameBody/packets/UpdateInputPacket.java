package packets;

public class UpdateInputPacket implements InputPacket {
    public int id;

    @Override
    public String getCommand() {
        return "Update";
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
