package thread_dispatcher;


import java.util.concurrent.CopyOnWriteArrayList;


public class ThreadMonitor extends ThreadedTask {
    private static volatile ThreadMonitor instance;

    private ThreadMonitor(){}

    static final CopyOnWriteArrayList<ThreadedTask> threadPool = new CopyOnWriteArrayList<>();

    static ThreadMonitor getInstance(){
        if (instance == null)
            synchronized (ThreadMonitor.class){
                if (instance == null)
                    instance = new ThreadMonitor();
            }
        return instance;
    }

    @Override
    public void runTask() {
        try {
            System.out.println(threadPool.toString());
//            FileOutputStream fstream = new FileOutputStream(System.getProperty("user.dir") + "\\logs.txt\\");
//            Writer writer = new BufferedWriter(new OutputStreamWriter(fstream, "utf-8"));
//
//            synchronized (threadPool) {
//                for (ThreadedTask thread : threadPool)
//                    writer.write(thread.toString() + "\n");
//                    writer.flush();
//            }
//
//            writer.close();
//            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return "ThreadMonitor";
    }
}
