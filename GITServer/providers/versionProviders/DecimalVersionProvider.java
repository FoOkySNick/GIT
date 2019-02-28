package providers.versionProviders;

public class DecimalVersionProvider implements IVersionProvider {
    private double version = 0;

    public String getVersion(){
        return String.valueOf(this.version);
    }

    public void incrementVersion(){
        this.version += 0.1;
    }
}
