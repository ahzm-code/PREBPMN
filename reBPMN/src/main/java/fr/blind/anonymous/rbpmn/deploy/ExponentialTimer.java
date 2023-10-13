package fr.blind.anonymous.rbpmn.deploy;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ExponentialTimer {
	
	private final double lambda;
    private final Random random;
    private Runnable task;
	
	public ExponentialTimer(double lambda) {
        this.lambda = lambda;
        this.random = new Random(100);
    }
	
	public void runWithExponentialDelay(Runnable codeToRun) throws InterruptedException {
        double delay = exponentialDelay(lambda);
        Thread.sleep((long) delay);
        codeToRun.run();
    }

    private double exponentialDelay(double lambda) {
        return -Math.log(1 - random.nextDouble()) / lambda;
    }
    
    public void start() {
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
                double delay = exponentialDelay(5);
                long delayMillis = (long) (delay * 1000);
                timer.schedule(this, delayMillis);
            }
        };

        // Schedule the first task
        double delay =  exponentialDelay(5);
        long delayMillis = (long) (delay * 1000);
        timer.schedule(timerTask, delayMillis);
    }
    
    public static void main(String[] args) throws InterruptedException {
    	ExponentialTimer timer = new ExponentialTimer(0.1);

        long startTime = System.currentTimeMillis();

        // Run the code block 10 times with exponential delay
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            timer.runWithExponentialDelay(() -> {
                System.out.println("Iteration " + finalI + " started after " + (System.currentTimeMillis() - startTime) + "ms");
            });
        }

        long endTime = System.currentTimeMillis();
    }
}
