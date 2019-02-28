package commands;

import packets.ISerializable;

import java.io.File;

public abstract class ICommand {
    byte[] file;

    public abstract ISerializable execute();

    public abstract File getArchive();

    public void setFile(byte[] file){
        this.file = file;
    }

}