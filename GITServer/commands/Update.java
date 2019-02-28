package commands;

import packets.ErrorPacket;
import packets.OutputPacket;
import packets.UpdateInputPacket;
import providers.DataProvider;
import providers.ProviderException;
import packets.ISerializable;
import server.Config;

import java.io.File;
import java.io.IOException;


public class Update extends ICommand {
    private int id;
    private File result;

    public Update(UpdateInputPacket packet){
        this.id = packet.id;
    }
    @Override
    public ISerializable execute() {
        Config config = Config.getInstance();
        if (!(config.users.contains(id) || config.currentRepo.containsKey(id))){
            ErrorPacket error = new ErrorPacket();
            error.error = "Authorization failed! Please, authorise!";
            error.errorNumber = 0;
            return error;
        }

        try {
            File result = DataProvider.getLatestVersionArchiveById(id);
            if (result != null) {
                OutputPacket packet = new OutputPacket();
                packet.command = "Update";
                packet.hasFile = result.length() != 0;
                packet.fileLength = result.length();
                packet.result = "Ok, Fine!";
                this.result = result;
                return packet;
            } else {
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 10;
                error.error = "Couldn't zip a repo for You!";
                return error;
            }
        } catch (IOException e) {
            ErrorPacket error = new ErrorPacket();
            error.error = "Some IOException was occurred while handling Your query!";
            error.errorNumber = 2;
            return error;
        } catch (ProviderException e) {
            ErrorPacket error = new ErrorPacket();
            error.errorNumber = 6;
            error.error = "Some ProviderException was occurred while handling Your query!";
            return error;
        }
    }

    @Override
    public File getArchive() {
        return this.result;
    }
}
