package packets;

public class CloneInputPacket implements InputPacket {
    public String repoName;
    public int id;

    @Override
    public String getCommand() {
        return "Clone";
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
