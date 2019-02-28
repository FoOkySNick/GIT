package commands;

import client.ClientINFO;
import packets.AddInputPacket;
import packets.InputPacket;
import packets.OutputPacket;

import java.io.File;


public class Add extends ICommand {
    private String[] args;

    public Add(String command){
        this.args = command.split(" ");
    }

    @Override
    public InputPacket formPacket() {
        ClientINFO client = ClientINFO.getInstance();
        AddInputPacket packet = new AddInputPacket();
        packet.id = client.getId();
        packet.repoName = args[1];
        return packet;
    }

    @Override
    public String execute(OutputPacket packet) {
        if (packet.result.equals("Ok, Fine!"))
            return String.format("%s Your repo \"%s\" was created", packet.result, args[1]);
        else return packet.result;
    }

    @Override
    public File getArchive() {
        return null;
    }
}
