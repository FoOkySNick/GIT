package providers.versionProviders;

public interface IVersionProvider {
    String getVersion();

    void incrementVersion();
}
