package providers;

import packets.FilePacket;

import java.io.*;

public class ConvertersProvider {
    public static FilePacket toFilePacket(File file) throws IOException {
        FilePacket fp = new FilePacket();
        fp.fileName = file.getName();
        byte[] b = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        if (fis.read(b) >= 0)
            fp.rawFile = b;
        else throw new IOException();
        return fp;
    }
}
