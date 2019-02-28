package workers;

public class WorkerException extends Exception {
    WorkerException(String message, Throwable cause){
        super(message, cause);
    }

    WorkerException(){
        super();
    }
}
