package commands;

import client.ClientINFO;
import packets.InputPacket;
import packets.LoginInputPacket;
import packets.OutputPacket;

import java.io.File;


public class Login extends ICommand {
    private String login;
    private String password;

    public Login(String command) {
        String[] args = command.split(" ");
        this.login = args[1];
        this.password = args[2];
    }

    @Override
    public InputPacket formPacket() {
        LoginInputPacket packet = new LoginInputPacket();
        packet.login = this.login;
        packet.password = this.password;
        return packet;
    }

    @Override
    public String execute(OutputPacket packet) {
        ClientINFO client = ClientINFO.getInstance();
        client.setId(Integer.parseInt(packet.result));
        client.setLogin(login);
        return "Ok, fine! Your id is " + client.getId();
    }

    @Override
    public File getArchive() {
        return null;
    }
}
