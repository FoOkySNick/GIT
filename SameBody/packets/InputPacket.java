package packets;

public interface InputPacket extends ISerializable {
    boolean hasFile();

    long fileLength();
}
