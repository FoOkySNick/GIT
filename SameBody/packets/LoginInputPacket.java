package packets;

public class LoginInputPacket implements InputPacket {
    public String login;
    public String password;

    @Override
    public String getCommand() {
        return "Login";
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