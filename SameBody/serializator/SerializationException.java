package serializator;

public class SerializationException extends Exception {
    SerializationException(String message, Throwable cause){
        super(message, cause);
    }
    SerializationException(){
        super();
    }
}
