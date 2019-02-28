package commands;

import client.ClientINFO;
import packets.InputPacket;
import packets.LogInputPacket;
import packets.OutputPacket;

import java.io.File;

public class Log extends ICommand {

    public Log(String command){ }

    @Override
    public InputPacket formPacket() {
        ClientINFO client = ClientINFO.getInstance();
        LogInputPacket packet = new LogInputPacket();
        packet.id = client.getId();
        return packet;
    }

    @Override
    public String execute(OutputPacket packet) {
        ClientINFO client = ClientINFO.getInstance();
        if (packet.result.length() != 0)
            return client.getLogin() + ":\n" + packet.result;
        else
            return client.getLogin() + " hasn't commited anything yet!";
    }

    @Override
    public File getArchive() {
        return null;
    }
}
