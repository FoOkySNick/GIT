package packets;

public class CommitInputPacket implements InputPacket {
    public int id;

    public boolean hasFile;

    public long fileLength;

    public String[] toDelete;

    @Override
    public String getCommand() {
        return "Commit";
    }

    @Override
    public boolean hasFile() {
        return this.hasFile;
    }

    @Override
    public long fileLength() {
        return this.fileLength;
    }
}