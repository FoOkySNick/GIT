package commands;

import client.ClientINFO;
import packets.InputPacket;
import packets.OutputPacket;
import packets.UpdateInputPacket;
import providers.Md5HashProvider;
import providers.ProviderException;
import providers.ZipProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Update extends ICommand {
    String[] args;

    public Update(String command){
        this.args = command.split(" ");
    }

    @Override
    public InputPacket formPacket() {
        ClientINFO client = ClientINFO.getInstance();
        UpdateInputPacket packet = new UpdateInputPacket();
        packet.id = client.getId();
        return packet;
    }

    @Override
    public String execute(OutputPacket packet) {
        try{
            ClientINFO clientINFO = ClientINFO.getInstance();
            File workingDir = new File(clientINFO.getWorkingDir());
            File[] todel = workingDir.listFiles();
            if (todel != null)
                for (File del: todel){
                    System.gc();
                    del.delete();
                }

            File temp = new File(System.getProperty("user.dir") + "\\temp\\" + clientINFO.getId() + ".zip");
            try {
                temp.createNewFile();
                new FileOutputStream(temp).write(super.file);
            } catch (IOException e) {
                return "Couldn't write temp file needed!";
            }

            if (new ZipProvider(workingDir).unzip(temp.getPath())) {
                temp.delete();
            HashMap<String, String> hashes = new HashMap<>();
            File[] list = workingDir.listFiles();
            if (list != null){
                for (File f: list){
                    try {
                        hashes.put(f.getName(), Md5HashProvider.hash(f));
                    } catch (ProviderException e) {
                        return "Couldn't generate hash through your file!";
                    }
                }
            }
            clientINFO.currentRepo.replace(workingDir.getPath(), hashes);
                return String.format("Your repository %s was updated!", workingDir);
            } else return String.format("Couldn't update your repo %s", workingDir);
        } catch (Exception e){
            return "Something went wrong! Write us your error case!";
        }
    }

    @Override
    public File getArchive() {
        return null;
    }
}
