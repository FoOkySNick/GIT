package commands;

import client.ClientINFO;
import packets.InputPacket;
import packets.OutputPacket;
import packets.RevertInputPacket;
import packets.UpdateInputPacket;
import providers.Md5HashProvider;
import providers.ProviderException;
import providers.ZipProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Revert extends ICommand {
    private String version;
    private boolean flag;

    public Revert(String command){
        String[] args = command.split(" ");
        if (args.length == 1 || (args.length == 2 && args[1].equals("-hard"))){
            this.version = "current";
            this.flag = true;
        } else if (args.length == 2){
            this.version = args[1];
            this.flag = false;
        } else {
            this.version = args[1];
            this.flag = args[2].equals("-hard");
        }
    }

    @Override
    public InputPacket formPacket() {
        if (this.flag && this.version.equals("current")){
            UpdateInputPacket packet = new UpdateInputPacket();
            ClientINFO client = ClientINFO.getInstance();
            packet.id = client.getId();
            return packet;
        } else {
            RevertInputPacket packet = new RevertInputPacket();
            ClientINFO client = ClientINFO.getInstance();
            packet.id = client.getId();
            packet.version = this.version;
            packet.flag = this.flag;
            return packet;
        }
    }

    @Override
    public String execute(OutputPacket packet) {
        ClientINFO clientINFO = ClientINFO.getInstance();
        File workingDir = new File(clientINFO.getWorkingDir());
        File[] files = workingDir.listFiles();

        if (this.flag) {
            if (files != null)
                for (File f : files) {
                    System.gc();
                    f.delete();
                }
        }

        File temp = new File(System.getProperty("user.dir") + "\\temp\\" + clientINFO.getId() + ".zip");
        try {
            temp.createNewFile();
            new FileOutputStream(temp).write(super.file);
        } catch (IOException e) {
            return "Couldn't write temp file needed!";
        }
        if (new ZipProvider(workingDir).unzip(temp.getPath())) {
            System.gc();
            temp.delete();
            HashMap<String, String> hashes = new HashMap<>();
            File[] list = workingDir.listFiles();
            if (list != null) {
                for (File f : list) {
                    try {
                        hashes.put(f.getName(), Md5HashProvider.hash(f));
                    } catch (ProviderException e) {
                        return "Couldn't generate hash through your file!";
                    }
                }
            }
            clientINFO.currentRepo.replace(workingDir.getPath(), hashes);
            return String.format("Your repository %s was reverted to version %s!", workingDir, this.version);
        } else return String.format("Couldn't revert your repo %s", workingDir);
    }

    @Override
    public File getArchive() {
        return null;
    }
}
