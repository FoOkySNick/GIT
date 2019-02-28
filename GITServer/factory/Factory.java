package factory;

import commands.ICommand;
import packets.InputPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Factory {
    public static ICommand recogniseCommand(InputPacket packet) throws Exception {
        try {
            Class cls = Class.forName("commands." + packet.getCommand());
            Constructor[] constructor = cls.getDeclaredConstructors();
            return (ICommand) constructor[0].newInstance(packet);
        } catch (ClassNotFoundException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            throw new FactoryException(e.getMessage(), e.getCause());
        }
    }
}