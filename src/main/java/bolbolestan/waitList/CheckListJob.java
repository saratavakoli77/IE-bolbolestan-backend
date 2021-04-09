package bolbolestan.waitList;

public class CheckListJob implements Runnable {

    @Override
    public void run() {
        System.out.println("job is running");
    }
}