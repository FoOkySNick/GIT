package thread_dispatcher;


public abstract class ThreadedTask implements Runnable {
    private String path = System.getProperty("user.dir");

    @Override
    public void run() {
        ThreadDispatcher.monitor.run();
        try {
            runTask();
        } catch (DispatcherException e) {
            e.printStackTrace();
        }
        ThreadMonitor.threadPool.remove(this);
        ThreadDispatcher.monitor.run();
    }

    public abstract void runTask() throws DispatcherException;

    protected synchronized void savePath(String path){
        this.path = path;
    }

    protected synchronized String getPath(){
        return this.path;
    }

}
