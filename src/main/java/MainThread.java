package main.java;

public class MainThread extends Thread{
    
    public static final int FPS_TARGET = 60;

    public GameRunner gameRunner;

    private volatile boolean isRunning;
    private long desiredFramerate;
    private long startTime;
    private long elapsedTime;
    private long waitTime;

    public MainThread(String name){
        super(name);
        desiredFramerate = 1000/FPS_TARGET;
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                startTime = System.currentTimeMillis();
                waitUntilResumed();
                doThreadActions();
                setFpsCap();
                 
            } catch (InterruptedException e) {
                ErrorLogger.logErrorMessage("There was an error in the main thread.", e);
            }
        }
    }

    private synchronized void waitUntilResumed() throws InterruptedException {
        while (!isRunning) {
            wait();
        }
    }

    private void doThreadActions() {
        try{
            gameRunner.repaint();
        }catch(NullPointerException e){
            ErrorLogger.logWarningMessage("The game runner has not been attached to the main thread.", e);
        }
    }

    private void setFpsCap() throws InterruptedException {
        elapsedTime = System.currentTimeMillis() - startTime;
        waitTime = desiredFramerate - elapsedTime;
        if(waitTime > 0){
            Thread.sleep(waitTime);
        }
    }

    public void pauseThread() {
        isRunning = false;
    }

    public synchronized void resumeThread() {
        isRunning = true;
        notifyAll();
    }
}
