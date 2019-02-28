package commands;

import org.javatuples.Triplet;
import packets.ErrorPacket;
import packets.LogInputPacket;
import packets.OutputPacket;
import packets.ISerializable;
import server.Logs;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Log extends ICommand {
    private int id;

    public Log(LogInputPacket packet){
        this.id = packet.id;
    }

    @Override
    public ISerializable execute() {
        try {
            StringBuilder res = new StringBuilder();
            OutputPacket packet = new OutputPacket();
            ArrayList<HashMap<String, Triplet<Date, String[], String[]>>> history = Logs.logs.get(id);
            for (HashMap<String, Triplet<Date, String[], String[]>> map : history){
                for (String key: map.keySet()) {
                    Triplet<Date, String[], String[]> el = map.get(key);
                    res.append("at ").append(el.getValue0().toString()).append(" to ").append(key).append(":\n");
                    for (String s : el.getValue1())
                        res.append("\tAdded: ").append(s).append("\n");
                    for (String s : el.getValue2()) {
                        if (s.equals("")) continue;
                        res.append("\tDeleted: ").append(s).append("\n");
                    }
                }
            }

            Logs.history.put(id, "Log");
            packet.result = res.toString();
            packet.command = "Log";
            packet.hasFile = false;
            return packet;
        } catch (Exception e){
            ErrorPacket error = new ErrorPacket();
            error.error = "Some unexpected Exception was occurred while handling Your query!";
            error.errorNumber = 4;
            return error;
        }
    }

    @Override
    public File getArchive() {
        return null;
    }
}
