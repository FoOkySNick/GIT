package commands;

import packets.AddInputPacket;
import packets.ErrorPacket;
import packets.OutputPacket;
import providers.DataProvider;
import packets.ISerializable;
import server.Config;
import server.Logs;

import java.io.File;


public class Add extends ICommand {
    private String repoName;
    private int id;

    public Add(AddInputPacket packet){
        this.repoName = packet.repoName;
        this.id = packet.id;
    }

    @Override
    public ISerializable execute() {
        try {
            if (!Config.getInstance().users.contains(id)){
                ErrorPacket error = new ErrorPacket();
                error.error = "Authorization failed! Please, authorise!";
                error.errorNumber = 0;
                return error;
            }

            File file = DataProvider.getRepo(id, repoName);

            if (file.exists()) {
                OutputPacket packet = new OutputPacket();
                packet.result = "Already exists!";
                packet.hasFile = false;
                packet.command = "Add";
                packet.fileLength = 0;
                return packet;
            } else {
                if (file.mkdirs()) {
                    Config config = Config.getInstance();
                    Logs.history.put(id, "Add");
                    config.repo.put(file.getName(), id);
                    OutputPacket packet = new OutputPacket();
                    packet.result = "Ok, Fine!";
                    packet.command = "Add";
                    packet.hasFile = false;
                    packet.fileLength = 0;
                    return packet;
                } else {
                    OutputPacket packet = new OutputPacket();
                    packet.result = "Server couldn't create Your repository!";
                    packet.hasFile = false;
                    packet.command = "Add";
                    packet.fileLength = 0;
                    return packet;
                }
            }
        } catch (Exception e){
            ErrorPacket error = new ErrorPacket();
            error.error = "Some unexpected Exception was occurred while handling Your query!";
            error.errorNumber = 4;
            return error;
        }
    }

    @Override
    public File getArchive() {
        return null;
    }
}
