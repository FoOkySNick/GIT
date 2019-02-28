package server;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Logs {
    private static volatile Logs instance;

    public static Logs getInstance() {
        if (instance == null)
            synchronized (Logs.class) {
                if (instance == null)
                    instance = new Logs();
            }
        return instance;
    }

    public static HashMap<Integer, String> history = new HashMap<>();

    public static HashMap<Integer, ArrayList<HashMap<String, Triplet<Date, String[], String[]>>>>
            logs = new HashMap<>();
}