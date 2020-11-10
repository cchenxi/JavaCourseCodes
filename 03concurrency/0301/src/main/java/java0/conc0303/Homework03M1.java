package java0.conc0303;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 方法 1
 *
 * 使用 {@link ThreadX} 实现 {@link Callable}，在call()方法中返回计算结果
 * 封装 {@link FutureTask} 用做创建 {@link Thread} 的参数，并启动线程
 * 通过 {@link FutureTask#get()} 在主线程中获取返回结果
 */
public class Homework03M1 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        ThreadX threadX = new ThreadX();
        FutureTask<Integer> futureTask = new FutureTask<>(threadX);
        new Thread(futureTask).start();

        System.out.println("异步计算结果为：" + futureTask.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

}

class ThreadX implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return fibo(30);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
