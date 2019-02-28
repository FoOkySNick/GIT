package commands;

import client.ClientINFO;
import packets.*;
import providers.ConvertersProvider;
import providers.Md5HashProvider;
import providers.ProviderException;
import providers.ZipProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Commit extends ICommand {
    private String[] args;
    private File result;

    public Commit(String command){
        this.args = command.split(" ");
    }

    @Override
    public InputPacket formPacket() {
        ClientINFO clientINFO = ClientINFO.getInstance();
        File file = new File(clientINFO.getWorkingDir());
        HashMap<String, String> hashes = clientINFO.currentRepo.get(file.getPath());
        HashMap<String, String> currentHashes = new HashMap<>();
        ArrayList<File> toSend = new ArrayList<>();
        File[] files = file.listFiles();
        if (files == null) return null;
        for (File f: files){
            String fileHash;
            try {
                fileHash = Md5HashProvider.hash(f);
            } catch (ProviderException e) {
                return null;
            }
            if (!hashes.containsKey(f.getName())){
                toSend.add(f);
            } else if (!hashes.get(f.getName()).equals(fileHash)){
                toSend.add(f);
            }
            currentHashes.put(f.getName(), fileHash);
        }

        ArrayList<String> toDelete = new ArrayList<>();
        if (currentHashes.size() != hashes.size()){
            for (String key: hashes.keySet()){
                if (!currentHashes.containsKey(key)){
                    toDelete.add(key);
                }
            }
        }

        FilePacket[] res = new FilePacket[toSend.size()];
        for (int i = 0; i < toSend.size(); i++) {
            try {
                res[i] = ConvertersProvider.toFilePacket(toSend.get(i));
            } catch (IOException e) {
                return null;
            }
        }

        clientINFO.currentRepo.replace(file.getPath(), currentHashes);
        File temp = new File(System.getProperty("user.dir") + "\\temp\\" + clientINFO.getId() + ".zip");
        try {
            temp.createNewFile();
        } catch (IOException e) {
            return null;
        }
        if (new ZipProvider(temp).zip(res)) {
            CommitInputPacket packet = new CommitInputPacket();
            packet.id = clientINFO.getId();
            packet.fileLength = temp.length();
            packet.hasFile = temp.length() != 0;
            packet.toDelete = new String[toDelete.size()];
            for (int i = 0; i < toDelete.size(); i++) {
                packet.toDelete[i] = toDelete.get(i);
            }
            result = temp;
            return packet;
        } else return null;
    }

    @Override
    public String execute(OutputPacket packet) {
        return "Your files was commited!";
    }

    @Override
    public File getArchive() {
        return result;
    }
}
