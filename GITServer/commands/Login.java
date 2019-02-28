package commands;

import packets.ErrorPacket;
import packets.LoginInputPacket;
import packets.OutputPacket;
import packets.ISerializable;
import server.Config;
import server.Logs;

import java.io.File;
import java.util.ArrayList;


public class Login extends ICommand {
    private String login;
    private String password;

    public Login(LoginInputPacket packet){
        login = packet.login;
        password = packet.password;
    }

    @Override
    public ISerializable execute() {
        try {
            if (login != null && password != null) {
                Config config = Config.getInstance();
                OutputPacket packet = new OutputPacket();
                int id = (login + password).hashCode();
                packet.result = String.valueOf(id);
                packet.hasFile = false;
                packet.command = "Login";
                Logs.history.put(id, "Login");
                if (!Logs.logs.containsKey(id))
                    Logs.logs.put(id, new ArrayList<>());
                config.users.add(id);
                return packet;
            }
            else throw new ExecutionException();
        } catch (ExecutionException e){
            ErrorPacket error = new ErrorPacket();
            error.errorNumber = 0;
            error.error = "Authorization failed! Please, authorise!";
            return error;
        }
    }

    @Override
    public File getArchive() {
        return null;
    }
}
