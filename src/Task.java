import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int nextId = 1;
    private int id;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
    private String priority; // high, medium, low
    private int customerId;
    

    public Task(int customerId, String description, LocalDateTime dueDate, String priority) {
        this.id = nextId++;
        this.customerId = customerId;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
    }
    public static void setNextId(int id) {
        nextId = id;
    }

    public int getCustomerId() { return customerId; }

    // Getters and setters
    public int getId() { return id; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getPriority() { return priority; }

    // Convert to string for file saving
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "TASK|" + id + "|" + customerId + "|" + description + "|" + 
            dueDate.format(formatter) + "|" + priority + "|" + completed + "\n";
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String status = completed ? "COMPLETED" : "PENDING";
        return String.format("ID: %d | %s | Due: %s | Priority: %s | Description: %s",
                id, status, dueDate.format(formatter), priority, description);
    }
    
}
