package server;

import providers.versionProviders.IVersionProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class Config {
    private static volatile Config instance;

    public static Config getInstance() {
        if (instance == null)
            synchronized (Config.class) {
                if (instance == null)
                    instance = new Config();
            }
        return instance;
    }

    public ArrayList<Integer> users = new ArrayList<>();

    public HashMap<String, Integer> repo = new HashMap<>();

    public HashMap<String, String[]> deleted = new HashMap<>();

    public HashMap<Integer, String> currentRepo = new HashMap<>();

    public HashMap<String, IVersionProvider> versionCounter = new HashMap<>();
}