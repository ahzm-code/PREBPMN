package fr.blind.anonymous.rbpmn.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;

public class CheckIfGenerateInstances {
	
	
	    private static final double LAMBDA = 1.5; // 指数分布的 lambda 参数
	    private static final long SEED = 123456L; // 随机数生成器的种子
	    private static final long INTERVAL = 100L; // 任务执行间隔时间（毫秒）
	    private static boolean needExecuteCodeA = false; // 是否需要执行代码A的标志
	    private static int nbrInstance = 1000;
	    private BPMNProcess bpmnProcess;
	    private Map<String, Object> variables;
	    
	    public CheckIfGenerateInstances(BPMNProcess bpmnProcess, Map<String, Object> variables) {
	    			this.bpmnProcess = bpmnProcess;
	    			this.variables = new HashMap<>();
	    			this.variables.putAll(variables);
	    }
	    public static void main(String[] args) {
	        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	      
	        executorService.scheduleAtFixedRate(new Runnable() {
	            @Override
	            public void run() {
	                if (needExecuteCodeA) {
	                    executeCodeA();
	                } else {
	                    checkIfNeedExecuteCodeA();
	                }
	            }
	        }, 0, INTERVAL, TimeUnit.MILLISECONDS);
	    }

	    /**
	            * 检测是否需要执行代码A
	     */
	    private static void checkIfNeedExecuteCodeA() {
	        Random random = new Random(SEED);
	        long sleepTime = nextExponentialTime(LAMBDA, random);
	        int count = 0;
	        
	        while (count < nbrInstance) {
	            try {
	                //Thread.sleep(sleepTime);
	            	Thread.sleep(sleepTime);
	                count += random.nextInt(10);
	                System.out.println(count);
	                sleepTime = nextExponentialTime(LAMBDA, random);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        needExecuteCodeA = true;
	    }

	    /**
	     * 执行代码A
	     */
	    private static void executeCodeA() {
	        System.out.println("Executing code A...");
	        // TODO: 在这里执行代码A
	        // ...
	        // 执行完成后，将 needExecuteCodeA 标志置为 false
	        
	        needExecuteCodeA = false;
	        System.out.println("Code A execution completed.");
	    }

	    /**
	     * 生成下一个指数分布的等待时间
	     * @param lambda 指数分布的 lambda 参数
	     * @param random 随机数生成器
	     * @return 等待时间（毫秒）
	     */
	    private static long nextExponentialTime(double lambda, Random random) {
	        return (long) (-Math.log(1 - random.nextDouble()) / lambda * 1000);
	    }
	}

