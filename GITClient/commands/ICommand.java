package commands;

import packets.InputPacket;
import packets.OutputPacket;

import java.io.File;

public abstract class ICommand {
    byte[] file;
    //COMMAND should take one parameter - String[] arg (Client queryLine.split(" "))
//    This method should form an AddInputPacket for Server
    public abstract InputPacket formPacket();
//    This method should handle the response from Server
    public abstract String execute(OutputPacket packet);

    public abstract File getArchive();

    public void setFile(byte[] file){
        this.file = file;
    }
}
