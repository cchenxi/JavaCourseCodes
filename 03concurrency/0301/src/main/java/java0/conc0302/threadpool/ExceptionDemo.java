package java0.conc0302.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExceptionDemo {
    
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // submit方法抛出的异常可以在主线程被捕获
//        output:
//        catch submit
//        Main Thread End!
//                java.util.concurrent.ExecutionException: java.lang.RuntimeException: executorService.submit()
//        at java.util.concurrent.FutureTask.report(FutureTask.java:122)
//        at java.util.concurrent.FutureTask.get(FutureTask.java:192)
//        at java0.conc0302.threadpool.ExceptionDemo.main(ExceptionDemo.java:20)
//        Caused by: java.lang.RuntimeException: executorService.submit()
//        at java0.conc0302.threadpool.ExceptionDemo.lambda$main$0(ExceptionDemo.java:17)
//        at java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:266)
//        at java.util.concurrent.FutureTask.run(FutureTask.java)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
//        at java.lang.Thread.run(Thread.java:748)
        try {
            Future<Double> future = executorService.submit(() -> {
                throw new RuntimeException("executorService.submit()");
            });

            double b = future.get();
            System.out.println(b);

        } catch (Exception ex) {
            System.out.println("catch submit");
            ex.printStackTrace();
        }

        // execute方法抛出的异常不能在主线程被捕获
//        output:
//        Main Thread End!
//                Exception in thread "pool-1-thread-1" java.lang.RuntimeException: executorService.execute()
//        at java0.conc0302.threadpool.ExceptionDemo.lambda$main$0(ExceptionDemo.java:45)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
//        at java.lang.Thread.run(Thread.java:748)
        try {
            executorService.execute(() -> {
                  throw new RuntimeException("executorService.execute()");
                });
        } catch (Exception ex) {
            System.out.println("catch execute");
            ex.printStackTrace();
        }

        executorService.shutdown();
        System.out.println("Main Thread End!");
    }
    
}
