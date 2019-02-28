package thread_dispatcher;


public class ThreadDispatcher {
    private static volatile ThreadDispatcher instance;

    static ThreadMonitor monitor = ThreadMonitor.getInstance();

    private ThreadDispatcher(){
    }

    public void Add(ThreadedTask task){
        ThreadMonitor.threadPool.add(task);
        new Thread(task).start();
    }

    public static ThreadDispatcher getInstance(){
        if (instance == null)
            synchronized (ThreadDispatcher.class) {
                if (instance == null)
                    instance = new ThreadDispatcher();
            }
        return instance;
    }


}
