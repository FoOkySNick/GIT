package factory;

public class FactoryException extends Exception {
    FactoryException(String message, Throwable cause){
        super(message, cause);
    }

    FactoryException(){
        super();
    }
}
