package client;

import commands.ICommand;
import factory.Factory;
import factory.FactoryException;
import org.javatuples.Pair;
import packets.ErrorPacket;
import packets.InputPacket;
import packets.OutputPacket;
import packets.ISerializable;
import serializator.SerializationException;
import serializator.Serializator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private String args;
    private Connection server;

    public Client(String args, Pair<String, Integer> server) {
        this.args = args.trim();
        try {
            this.server = new Connection(new Socket(server.getValue0(), server.getValue1()));
        } catch (IOException e) {
            System.out.println("Couldn't make up connection!");
        }
    }

    public void start() {
        try {
            ICommand cmd = Factory.recogniseCommand(args);
            InputPacket query = cmd.formPacket();
            if (query != null) {
                server.send(Serializator.serialize(query).getBytes("UTF-8"));
                if (query.hasFile()){
                    server.sendFile(cmd.getArchive());
                    cmd.getArchive().delete();
                }
            }
            else {
                System.out.println("An error occurred while preparing Your data for server!");
                server.close();
            }

            ISerializable response = Serializator.deserialize(server.read());
            try{
                OutputPacket packet = (OutputPacket) response;
                if (packet.hasFile){
                    byte[] file = server.readFile(packet.fileLength);
                    cmd.setFile(file);
                }
                System.out.println(cmd.execute(packet));
            } catch (ClassCastException e){
                try {
                    ErrorPacket error = (ErrorPacket) response;
                    System.out.println(String.format("%s -> %s", error.errorNumber, error.error));
                } catch (ClassCastException a){
                    System.out.println("Couldn't understand server's answer!");
                    server.close();
                }
            }
        } catch (FactoryException e) {
            System.out.println("There is no such command!");
        } catch (SocketException ignore){
        } catch (UnsupportedEncodingException e) {
            System.out.println("Couldn't recognize your encoding, use UTF-8(default value), please!");
        } catch (IOException e) {
            System.out.println("Exception in Internet connection was occurred, while handling your query!");
        } catch (SerializationException e) {
            System.out.println("Couldn't recognise Server answer or your query!");
        } catch (Exception e){
            System.out.println("Something went wrong! Write us your error case!");
        }
        finally {
            try {
                server.close();
                System.gc();
            } catch (IOException e) {
                System.out.println("Couldn't close connection and free resources! Fatal Error!");
            }
        }
    }
}