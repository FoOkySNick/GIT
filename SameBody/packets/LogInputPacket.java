package packets;

public class LogInputPacket implements InputPacket {
    public int id;

    @Override
    public String getCommand() {
        return "Log";
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
