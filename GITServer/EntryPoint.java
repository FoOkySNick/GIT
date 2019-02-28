import server.Server;
import thread_dispatcher.ThreadDispatcher;

import java.util.HashMap;

public class EntryPoint {
    private static ThreadDispatcher dispatcher = ThreadDispatcher.getInstance();

    public static void main(String[] args) {
        dispatcher.Add(new Server(dispatcher, 8080));
    }
}
