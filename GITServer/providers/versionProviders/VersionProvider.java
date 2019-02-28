package providers.versionProviders;


public class VersionProvider implements IVersionProvider {
    private double version = 0;

    public String getVersion(){
        return String.valueOf(this.version);
    }

    public void incrementVersion(){
        this.version += 1;
    }
}
