package providers;

import packets.FilePacket;
import providers.versionProviders.IVersionProvider;
import server.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static providers.ConvertersProvider.toFilePacket;

public class DataProvider {
    public static File getRepo(int id, String repoName){
        String path = "archive\\" + String.valueOf(id) + "\\" + repoName;
        return new File(path);
    }

    public static File getRepoWithVersion(String repoName, IVersionProvider versionProvider){
        String path = repoName + "\\" + versionProvider.getVersion();
        return new File(path);
    }

    private static File[] getLatestVersionRepository(int id) throws ProviderException {
        Config conf = Config.getInstance();
        String repoName = conf.currentRepo.get(id);
        if (repoName == null) throw new ProviderException();
        File currentRepo = new File(repoName);

        HashMap<String, File> names = new HashMap<>();
        getFile(currentRepo, names, null);

        Object[] values = names.values().toArray();
        File[] res = new File[values.length];
        for (int i = 0; i < values.length; i++) {
            res[i] = (File) values[i];
        }
        return res;
    }

    private static void getFile(File file, HashMap<String, File> names, String[] banned){
        Config conf = Config.getInstance();
        File[] list = file.listFiles();
        if (list != null)
            for (File f: list)
                if (f.isDirectory()) {
                    String[] ban = null;
                    if (conf.deleted.containsKey(f.getName())){
                        ban = conf.deleted.get(f.getName());
                    }
                    getFile(f, names, ban);
                }
                else {
                    if (!names.containsKey(f.getName())) names.put(f.getName(), f);
                    else names.replace(f.getName(), f);
                }
        if (banned != null)
            for (String name : banned)
                if (names.containsKey(name))
                    names.remove(name);
    }

    private static ArrayList<FilePacket> getRawFiles(File[] list) throws IOException, ProviderException {
        ArrayList<FilePacket> result = new ArrayList<>();
        if (list != null) {
            for (File f : list) {
                if (!f.isDirectory()) {
                    FilePacket filePacket = new FilePacket();
                    filePacket.fileName = f.getPath();
                    byte[] b = new byte[(int) f.length()];
                    FileInputStream fis = new FileInputStream(f);
                    if (fis.read(b) >= 0)
                        filePacket.rawFile = b;
                    else throw new ProviderException();
                    result.add(filePacket);
                } else {
                    result.addAll(getRawFiles(f.listFiles()));
                }
            }
        }
        return result;
    }

    public static File getLatestVersionArchiveById(int id) throws IOException, ProviderException {
        File[] list = DataProvider.getLatestVersionRepository(id);
        ArrayList<FilePacket> toRes = getRawFiles(list);
        FilePacket[] result = new FilePacket[toRes.size()];
        int index = 0;
        while (index < toRes.size()){
            result[index] = toRes.get(index);
            index++;
        }

        File f = new File(System.getProperty("user.dir") + "\\temp\\" + id + ".zip");
        f.createNewFile();
        if (new ZipProvider(f).zip(result)) return f;
        else return null;
    }

    private static void getFilesByVersion(File file, HashMap<String, File> names, String version, String[] banned){
        Config conf = Config.getInstance();
        File[] list = file.listFiles();
        boolean flag = false;
        if (list != null)
            for (File f: list)
                if (f.isDirectory()) {
                    if (flag) break;
                    String[] ban = null;
                    if (conf.deleted.containsKey(f.getName())){
                        ban = conf.deleted.get(f.getName());
                    }
                    if (f.getName().equals(version)) flag = true;
                    getFile(f, names, ban);
                }
                else
                    if (!names.containsKey(f.getName())) names.put(f.getName(), f);
                    else names.replace(f.getName(), f);

        if (banned != null)
            for (String name : banned)
                if (names.containsKey(name))
                    names.remove(name);

    }

    public static File getArchiveByVersionAndId(String version, int id) throws ProviderException, IOException {
        Config conf = Config.getInstance();
        String repoName = conf.currentRepo.get(id);
        if (repoName == null) throw new ProviderException();
        File currentRepo = new File(repoName);

        HashMap<String, File> names = new HashMap<>();
        getFilesByVersion(currentRepo, names, version, null);

        Object[] values = names.values().toArray();
        File[] res = new File[values.length];
        for (int i = 0; i < values.length; i++) {
            res[i] = (File) values[i];
        }

        FilePacket[] result = new FilePacket[res.length];
        int index = 0;
        while (index < res.length){
            result[index] = toFilePacket(res[index]);
            index++;
        }

        File f = new File(System.getProperty("user.dir") + "\\temp\\" + id + ".zip");
        f.createNewFile();
        if (new ZipProvider(f).zip(result)) return f;
        else return null;
    }
}
