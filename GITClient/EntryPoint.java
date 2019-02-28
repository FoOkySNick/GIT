import org.javatuples.Pair;
import thread_dispatcher.ThreadDispatcher;
import client.Client;

import java.util.Scanner;

public class EntryPoint {
    private static ThreadDispatcher dispatcher = ThreadDispatcher.getInstance();

    public static void main(String[] args) {
        Pair<String, Integer> server = new Pair<>("localhost", 8080);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            new Client(scanner.nextLine(), server).start();
        }
    }
}