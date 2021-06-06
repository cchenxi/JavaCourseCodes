package java0.conc0302.lock;

public class ConditionUsage {
    public static void main(String[] args) {
        ConditionDemo conditionDemo = new ConditionDemo();
        Thread t1 = new Thread(() -> {
            try {
                while (true) {
                    conditionDemo.put(new Object());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    conditionDemo.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
