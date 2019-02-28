package commands;

import packets.ErrorPacket;
import packets.OutputPacket;
import packets.RevertInputPacket;
import providers.DataProvider;
import providers.ProviderException;
import packets.ISerializable;
import server.Config;

import java.io.File;
import java.io.IOException;

public class Revert extends ICommand {
    private int id;
    private String version;
    private  boolean flag;
    private File result;

    public Revert(RevertInputPacket packet){
        this.id = packet.id;
        this.version = packet.version;
        this.flag= packet.flag;
    }

    @Override
    public ISerializable execute() {
        Config config = Config.getInstance();
        if (!(config.users.contains(id) || config.currentRepo.containsKey(id))) {
            ErrorPacket error = new ErrorPacket();
            error.error = "Authorization failed! Please, authorise!";
            error.errorNumber = 0;
            return error;
        }

        if (this.flag && this.version.equals("current")) {
            ErrorPacket error = new ErrorPacket();
            error.error = "It is Update command, so cal this one, please!";
            error.errorNumber = 17;
            return error;
        } else {
            OutputPacket packet = new OutputPacket();
            packet.result = "Ok, Fine!";
            packet.command = "Revert";

            File result;
            try {
                result = DataProvider.getArchiveByVersionAndId(this.version, this.id);
            }  catch (IOException e) {
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
            if (result == null) {
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 20;
                error.error = "Something went wrong! Your repo file was null!";
                return error;
            } else this.result = result;

            packet.hasFile = result.length() != 0;
            packet.fileLength = result.length();
            return packet;
        }
    }

    @Override
    public File getArchive() {
        return this.result;
    }
}
