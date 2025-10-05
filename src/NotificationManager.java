public class NotificationManager implements Observer {
    private String name;

    public NotificationManager(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[NOTIFICATION - " + name + "] " + message);
    }
    
}
