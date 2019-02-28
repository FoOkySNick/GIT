package packets;

public class AddInputPacket implements InputPacket {
    public String repoName;
    public int id;

    @Override
    public String getCommand() {
        return "Add";
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