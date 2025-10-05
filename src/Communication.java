import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Communication {
    private static int nextId = 1;
    private int id;
    private String type; // phone, email, meeting
    private String description;
    private LocalDateTime timestamp;
    private String tags;
    private int customerId;

    public Communication(int customerId, String type, String description, String tags) {
        this.id = nextId++;
        this.customerId = customerId;
        this.type = type;
        this.description = description;
        this.tags = tags;
        this.timestamp = LocalDateTime.now();
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    public int getCustomerId() { 
        return customerId; }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getTags() { return tags; }

    // Convert to string for file saving
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "COMMUNICATION|" + id + "|" + customerId + "|" + type + "|" + 
            description + "|" + timestamp.format(formatter) + "|" + tags + "\n";
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("ID: %d | Type: %s | Time: %s | Description: %s | Tags: %s",
                id, type, timestamp.format(formatter), description, tags);
    }
    
}
