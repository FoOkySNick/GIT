package thread_dispatcher;

public class DispatcherException extends Exception {
    DispatcherException(String message, Throwable cause){
        super(message, cause);
    }

    DispatcherException(){
        super();
    }
}
