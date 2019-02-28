package server;

import thread_dispatcher.ThreadDispatcher;
import thread_dispatcher.ThreadedTask;
import workers.ClientWorker;

import java.io.IOException;
import java.net.ServerSocket;


public class Server extends ThreadedTask {
    private static volatile Server instance;

    private ThreadDispatcher dispatcher;
    private int port = 0;

    public Server(ThreadDispatcher dispatcher, int port) {
        this.dispatcher = dispatcher;
        this.port = port;
    }

    public static Server getInstance(ThreadDispatcher dispatcher, int port, String address){
        if (instance == null)
            synchronized (Server.class){
                if (instance == null)
                    instance = new Server(dispatcher, port);
            }
        return instance;
    }

    @Override
    public void runTask() {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server was started!");
            while (true) {
                dispatcher.Add(new ClientWorker(new server.Connection(server.accept())));
                System.out.println("Client was started!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Server";
    }
}
