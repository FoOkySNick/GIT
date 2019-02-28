package workers;

import commands.ICommand;
import packets.InputPacket;
import packets.OutputPacket;
import packets.ISerializable;
import factory.Factory;
import serializator.Serializator;
import server.Connection;
import thread_dispatcher.ThreadedTask;


public class ClientWorker extends ThreadedTask {
    private Connection client;

    public ClientWorker(Connection client){
        this.client = client;
    }

    @Override
    public void runTask() throws WorkerException {
        try {
            InputPacket packet = Serializator.deserialize(client.read());
            ICommand command = Factory.recogniseCommand(packet);

            if (packet.hasFile()){
                byte[] file = client.readFile(packet.fileLength());
                command.setFile(file);
            }

            ISerializable response = command.execute();
            client.send(Serializator.serialize(response).getBytes("UTF-8"));
            try {
                if (((OutputPacket) response).hasFile) {
                    client.sendFile(command.getArchive());
                    System.gc();
                    command.getArchive().delete();
                }
            } catch (ClassCastException ignore){}
        } catch (Exception e) {
            System.out.println("Client's query was rejected!");
        }
    }

    @Override
    public String toString() {
        return "Client";
    }
}
