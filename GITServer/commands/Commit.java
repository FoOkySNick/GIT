package commands;

import org.javatuples.Triplet;
import packets.CommitInputPacket;
import packets.ErrorPacket;
import packets.OutputPacket;
import providers.DataProvider;
import providers.versionProviders.IVersionProvider;
import packets.ISerializable;
import server.Config;
import server.Logs;
import providers.ZipProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Commit extends ICommand {
    private int id;
    private boolean hasFile;
    private long fileLength;
    private String[] toDelete;

    public Commit(CommitInputPacket packet){
        this.id = packet.id;
        this.hasFile = packet.hasFile;
        this.fileLength = packet.fileLength;
        this.toDelete= packet.toDelete;
    }

    @Override
    public ISerializable execute() {
        Config conf = Config.getInstance();
        if (!(conf.users.contains(id) || conf.currentRepo.containsKey(id) ||
                conf.versionCounter.containsKey(conf.currentRepo.get(id)))){
            ErrorPacket error = new ErrorPacket();
            error.error = "Authorization failed! Please, authorise!";
            error.errorNumber = 0;
            return error;
        }

        if (super.file.length == 0){
            ErrorPacket error = new ErrorPacket();
            error.error = "There's no files to commit!";
            error.errorNumber = 25;
            return error;
        }

        IVersionProvider vp = conf.versionCounter.get(conf.currentRepo.get(id));
        vp.incrementVersion();
        File repo = DataProvider.getRepoWithVersion(conf.currentRepo.get(id), vp);
        try {
            if (!repo.mkdirs()) throw new ExecutionException();

            File temp = new File(System.getProperty("user.dir") + "\\temp\\" + id + ".zip");
            try {
                temp.createNewFile();
                FileOutputStream fos = new FileOutputStream(temp);
                fos.write(super.file);
                fos.close();
            } catch (IOException e) {
                ErrorPacket error = new ErrorPacket();
                error.error = "Couldn't write temp file needed!";
                error.errorNumber = 15;
                return error;
            }

            if (new ZipProvider(repo).unzip(temp.getPath())) {
                System.gc();
                temp.delete();
            } else {
                ErrorPacket error = new ErrorPacket();
                error.errorNumber = 1;
                error.error = "Couldn't unzip client's files!";
                return error;
            }
        } catch (ExecutionException e) {
            ErrorPacket error = new ErrorPacket();
            error.errorNumber = 3;
            error.error = "Newer version found that you want to commit!";
            return error;
        }

        if (!Logs.logs.containsKey(id)) {
            Triplet<Date, String[], String[]> logs = new Triplet<>(new Date(), repo.list(), toDelete);
            ArrayList<HashMap<String, Triplet<Date, String[], String[]>>> list = new ArrayList<>();
            HashMap<String, Triplet<Date, String[], String[]>> map = new HashMap<>();
            map.put(repo.getName(), logs);
            list.add(map);
            Logs.logs.put(id, list);
        } else {
            ArrayList<HashMap<String, Triplet<Date, String[], String[]>>> logs = Logs.logs.get(id);
            HashMap<String, Triplet<Date, String[], String[]>> map = new HashMap<>();
            map.put(conf.currentRepo.get(id), new Triplet<>(new Date(), repo.list(), toDelete));
            logs.add(map);
        }

        System.out.println(repo.getName());
        conf.deleted.put(repo.getName(), toDelete);

        OutputPacket packet = new OutputPacket();
        packet.result = "Ok, fine!";
        packet.fileLength = 0;
        packet.hasFile = false;
        packet.command = "Commit";
        return packet;
    }

    @Override
    public File getArchive() {
        return null;
    }
}
