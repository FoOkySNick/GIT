package providers;

public class ProviderException extends Exception{
    ProviderException(String message, Throwable cause){
        super(message, cause);
    }

    ProviderException(){
        super();
    }
}
