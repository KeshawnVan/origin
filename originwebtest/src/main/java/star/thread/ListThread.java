package star.thread;

import java.util.List;

public class ListThread implements Runnable {

    private List<String> strings;

    public ListThread(List<String> strings) {
        this.strings = strings;
    }

    @Override
    public void run() {

    }

    public void get(List<String> strings) {
        synchronized (this.strings) {
            System.out.println("get start");
            System.out.println("sleep start");
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep end");
            System.out.println(strings.get(2));
            System.out.println("get end");
        }
    }
}
