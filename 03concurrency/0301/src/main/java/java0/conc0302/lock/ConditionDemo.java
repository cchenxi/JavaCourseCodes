package java0.conc0302.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    
    final Object[] items = new Object[20];
    int putptr, takeptr, count;
    
    public void put(Object x) throws InterruptedException {
        lock.lock();
        Thread.sleep(10);
        try {
            // 当count等于数组的大小时，当前线程等待，直到notFull通知，再进行生产
            while (count == items.length) {
                System.out.println(Thread.currentThread().getName() + ", full...");
                notFull.await();
            }
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public Object take() throws InterruptedException {
        lock.lock();
        Thread.sleep(10);
        try {
            // 当count为0，进入等待，直到notEmpty通知，进行消费。
            while (count == 0) {
                System.out.println(Thread.currentThread().getName() + ", empty...");
                notEmpty.await();
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}