package factory;

import commands.ICommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Factory {
    public static ICommand recogniseCommand(String command) throws FactoryException {
        try {
            String[] args = command.split(" ");
            Class cls = Class.forName("commands." + args[0]);
            Constructor[] constructor = cls.getDeclaredConstructors();
            return (ICommand) constructor[0].newInstance(command);
        } catch (ClassNotFoundException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            throw new FactoryException(e.getMessage(), e.getCause());
        }
    }
}