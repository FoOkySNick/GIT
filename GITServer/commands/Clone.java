package commands;

import packets.CloneInputPacket;
import packets.ErrorPacket;
import packets.OutputPacket;
import providers.DataProvider;
import providers.ProviderException;
import providers.versionProviders.VersionProvider;
import packets.ISerializable;
import server.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Clone extends ICommand {
    private String repoName;
    private int id;
    private File result;

    public Clone(CloneInputPacket packet) {
        this.repoName = packet.repoName;
        this.id = packet.id;
    }

    @Override
    public ISerializable execute() {
        try {
            Config config = Config.getInstance();

            if (!config.users.contains(id)) {
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 0;
                error.error = "Authorization failed! Please, authorise!";
                return error;
            }

            if (!config.repo.containsKey(repoName)){
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 1;
                error.error = "There's no such repository!";
                return error;
            }

            File file = DataProvider.getRepo(config.repo.get(repoName), repoName);

            if (!config.versionCounter.containsKey(file.getPath()))
                config.versionCounter.put(file.getPath(), new VersionProvider());

            if (config.currentRepo.containsKey(id))
                config.currentRepo.replace(id, file.getPath());
            else config.currentRepo.put(id, file.getPath());

            File result = DataProvider.getLatestVersionArchiveById(id);
            if (result != null) {
                OutputPacket packet = new OutputPacket();
                packet.result = "Ok, Fine!";
                packet.hasFile = result.length() != 0;
                packet.fileLength = result.length();
                packet.command = "Clone";
                this.result = result;
                return packet;
            } else {
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 10;
                error.error = "Couldn't zip a repo for You!";
                return error;
            }
        } catch (FileNotFoundException e) {
            ErrorPacket error = new ErrorPacket();
            error.errorNumber = 1;
            error.error = "Your file was not found while handling Your query!";
            return error;
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
