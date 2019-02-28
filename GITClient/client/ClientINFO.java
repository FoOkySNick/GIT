package client;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientINFO {
    private static volatile ClientINFO instance;

    public static ClientINFO getInstance() {
        if (instance == null)
            synchronized (ClientINFO.class) {
                if (instance == null)
                    instance = new ClientINFO();
            }
        return instance;
    }

    private int id;

    private String login;

    private String workingDir;

    public HashMap<String, HashMap<String, String>> currentRepo = new HashMap<>();

    public String getWorkingDir(){
        return workingDir;
    }

    public void setWorkingDir(String newDir){
        this.workingDir = newDir;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
