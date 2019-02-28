package providers;

import packets.FilePacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipProvider {
    private File file;

    public ZipProvider(File file){
        this.file = file;
    }

    public boolean unzip(String path) {
        try{
            ZipFile zipFile = new ZipFile(path);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();

                InputStream fis = zipFile.getInputStream(entry);

                FileOutputStream fos = new FileOutputStream(file.getPath() + "\\" + entryName);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer, 0, buffer.length);
                fis.close();
                fos.write(buffer, 0, buffer.length);
                fos.close();
            }
            zipFile.close();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean zip(FilePacket[] files) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))){
            for (FilePacket f : files) {
                String[] name = f.fileName.split("\\\\");
                zos.putNextEntry(new ZipEntry(name[name.length-1]));
                zos.write(f.rawFile);
                zos.flush();
                zos.closeEntry();
            }
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
}
