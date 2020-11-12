package java0.homework;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Homework03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Homework03().exec01();
        new Homework03().exec02();
        new Homework03().exec03();
        new Homework03().exec04();
        new Homework03().exec05();
        new Homework03().exec06();
        new Homework03().exec07();
        new Homework03().exec08();
    }

    /**
     * {@link Thread} + {@link FutureTask}
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void exec01() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return CalcFibo.fibo(30);
        });
        Thread thread = new Thread(futureTask);
        thread.start();

        System.out.println("异步计算结果为：" + futureTask.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * {@link ExecutorService} + {@link CountDownLatch} + {@link Future}
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void exec02() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Future<Integer> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
                return CalcFibo.fibo(30);
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

        System.out.println("异步计算结果为：" + future.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        executorService.shutdown();
    }

    /**
     * {@link ExecutorService} + {@link CyclicBarrier} + {@link Future}
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void exec03() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        Future<Integer> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
                return CalcFibo.fibo(30);
            } finally {
                cyclicBarrier.await();
            }
        });

        System.out.println("异步计算结果为：" + future.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        executorService.shutdown();
    }

    /**
     * {@link ExecutorService} + {@link Semaphore} + {@link Future}
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void exec04() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Semaphore semaphore = new Semaphore(1);
        Future<Integer> future = executorService.submit(() -> {
            try {
                semaphore.acquire();
                Thread.sleep(1000);
                return CalcFibo.fibo(30);
            } finally {
                semaphore.release();
            }
        });

        System.out.println("异步计算结果为：" + future.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        executorService.shutdown();
    }

    /**
     * {@link CompletableFuture}
     */
    public void exec05() {
        long start = System.currentTimeMillis();

        Integer result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return CalcFibo.fibo(30);
        }).join();

        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //***********************锁相关**************************//
    // 锁主线程
    // 异步线程完成任务后通知
    // 主线程等待通知后从任务中获取结果

    /**
     * synchronized
     *
     * @throws InterruptedException
     */
    public void exec06() throws InterruptedException {
        long start = System.currentTimeMillis();

        Object o = new Object();
        Task06 task06 = new Task06(o);
        new Thread(task06).start();

        synchronized (o) {
            o.wait();
        }

        System.out.println("异步计算结果为：" + task06.getResult());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    static class Task06 implements Runnable {
        private Object o;
        private Integer result;

        public Task06(Object o) {
            this.o = o;
        }

        public Integer getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (o) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = CalcFibo.fibo(30);
                o.notifyAll();
            }
        }
    }

    /**
     * {@link Thread} + {@link Lock} + {@link Condition}
     *
     * @throws InterruptedException
     */
    public void exec07() throws InterruptedException {
        long start = System.currentTimeMillis();
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();

        Task07 task07 = new Task07(lock, condition);
        new Thread(task07).start();

        lock.lock();
        condition.await();
        lock.unlock();
        System.out.println("异步计算结果为：" + task07.getResult());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    static class Task07 implements Runnable {
        private Lock lock;
        private Condition condition;
        private Integer result;

        public Task07(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        public Integer getResult() {
            return result;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                result = CalcFibo.fibo(30);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * {@link LockSupport}
     */
    public void exec08() {
        long start = System.currentTimeMillis();

        Task08 task08 = new Task08(Thread.currentThread());
        new Thread(task08).start();
        LockSupport.park();

        System.out.println("异步计算结果为：" + task08.getResult());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    static class Task08 implements Runnable {
        private Thread thread;
        private Integer result;

        public Task08(Thread thread) {
            this.thread = thread;
        }

        public Integer getResult() {
            return result;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = CalcFibo.fibo(30);
            LockSupport.unpark(thread);
        }
    }
}

class CalcFibo {
    public static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
