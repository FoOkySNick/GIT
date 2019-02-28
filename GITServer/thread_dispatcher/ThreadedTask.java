package thread_dispatcher;


import workers.WorkerException;

public abstract class ThreadedTask implements Runnable {
    private String path = System.getProperty("user.dir");

    @Override
    public void run() {
        ThreadDispatcher.monitor.runTask();
        try {
            runTask();
        } catch (WorkerException e) {
            e.printStackTrace();
        }
        ThreadMonitor.threadPool.remove(this);
        ThreadDispatcher.monitor.runTask();
    }
    public abstract void runTask() throws WorkerException;

    protected synchronized void savePath(String path){
        this.path = path;
    }

    protected synchronized String getPath(){
        return this.path;
    }

}
