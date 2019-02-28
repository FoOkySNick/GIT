package commands;

import client.ClientINFO;
import packets.CloneInputPacket;
import packets.InputPacket;
import packets.OutputPacket;
import providers.Md5HashProvider;
import providers.ProviderException;
import providers.ZipProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Clone extends ICommand {
    private String[] args;

    public Clone(String command) {
        this.args = command.split(" ");
    }

    @Override
    public InputPacket formPacket() {
        ClientINFO client = ClientINFO.getInstance();
        CloneInputPacket packet = new CloneInputPacket();
        packet.id = client.getId();
        try {
            packet.repoName = args[2];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
        return packet;
    }

    @Override
    public String execute(OutputPacket packet) {
        File file = new File(args[1]);
        ClientINFO clientINFO = ClientINFO.getInstance();

        if (file.exists()) {
            File[] toDelete = file.listFiles();
            if (toDelete != null)
                for (File f: toDelete) {
                    System.gc();
                    f.delete();
                }
        } else file.mkdirs();

        if (!(args.length == 4 && args[3].equals("."))) {
            file = new File(args[1] + "\\" + args[2]);
            file.mkdirs();
        }

        File temp = new File(System.getProperty("user.dir") + "\\temp\\" + clientINFO.getId() +  ".zip");
        try {
            temp.createNewFile();
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(super.file);
            fos.close();
        } catch (IOException e) {
            return "Couldn't write temp file needed!";
        }

        if (new ZipProvider(file).unzip(temp.getPath())) {
            temp.delete();
            clientINFO.setWorkingDir(file.getPath());
            HashMap<String, String> hashes = new HashMap<>();
            File[] list = file.listFiles();
            if (list != null){
                for (File f: list){
                    try {
                        hashes.put(f.getName(), Md5HashProvider.hash(f));
                        System.out.println(f.getName());
                    } catch (ProviderException e) {
                        return "Couldn't generate hash through your file!";
                    }
                }
            }
            clientINFO.currentRepo.put(file.getPath(), hashes);
            return String.format("Ok, Fine! Repo \"%s\" was cloned to your \"%s\" directory", args[2], args[1]);
        }
        else return "Couldn't unzip repo " + args[2] + " for You!";
    }

    @Override
    public File getArchive() {
        return null;
    }
}
