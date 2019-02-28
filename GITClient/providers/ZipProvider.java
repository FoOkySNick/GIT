package providers;

import packets.FilePacket;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
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
            String fileName;
            if (files != null)
                for (FilePacket f : files) {
                    System.out.println(f.fileName);
                    if (f.fileName.contains("\\")) {
                        String[] name = f.fileName.split("\\\\");
                        fileName = name[name.length - 1];
                    } else {
                        fileName = f.fileName;
                    }
                    zos.putNextEntry(new ZipEntry(fileName));

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
