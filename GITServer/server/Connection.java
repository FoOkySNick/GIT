package server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Connection {
    private Socket destination;
    private DataInputStream in;
    private DataOutputStream out;

    public Connection(Socket destination) throws IOException {
        destination.setKeepAlive(true);
        this.destination = destination;
        this.in = new DataInputStream(destination.getInputStream());
        this.out = new DataOutputStream(destination.getOutputStream());
    }

    public byte[] read() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean flag = false;

        while (true) {
            byte[] buffer = new byte[1024];
            in.read(buffer);
            for (byte b : buffer) {
                if (b != 0) {
                    baos.write(b);
                }
                else {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        return baos.toByteArray();
    }

    public void send(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    public byte[] readFile(long fileLength) throws IOException{
        byte[] buffer = new byte[(int) fileLength];
        in.readFully(buffer);
        return buffer;
    }

    public void sendFile(File file) throws IOException{
        DataInputStream fis = new DataInputStream(new FileInputStream(file));
        byte[] buffer = new byte[(int) file.length()];
        fis.readFully(buffer);
        fis.close();
        out.write(buffer);
        out.flush();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        destination.close();
    }
}
