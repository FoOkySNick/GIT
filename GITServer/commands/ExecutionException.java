package commands;

class ExecutionException extends Exception{
    ExecutionException(String message, Throwable cause){
        super(message, cause);
    }

    ExecutionException(){
        super();
    }
}
