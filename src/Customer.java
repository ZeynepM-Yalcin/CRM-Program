import java.util.*;

public class Customer {
    private static int nextId = 1;
    private int id;
    private String name;
    private String email;
    private String phone;
    private String notes;
    private List<Communication> communications;
    private List<Task> tasks;

    public Customer(String name, String email, String phone, String notes) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
        this.communications = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    public static void setNextId(int id) {
    nextId = id;
    }

    // Getters and setters
    public int getId() { 
        return id; }

    public String getName() { 
        return name; }

    public void setName(String name) { 
        this.name = name; }

    public String getEmail() { 
        return email; }

    public void setEmail(String email) { 
        this.email = email; }

    public String getPhone() { 
        return phone; }

    public void setPhone(String phone) { 
        this.phone = phone; }

    public String getNotes() { 
        return notes; }

    public void setNotes(String notes) { 
        this.notes = notes; }

    public List<Communication> getCommunications() { 
        return communications; }
        
    public List<Task> getTasks() { 
        return tasks; }

    public void addCommunication(Communication comm) {
        communications.add(comm);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    // Convert customer to a string for saving to file
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CUSTOMER|").append(id).append("|").append(name).append("|")
        .append(email).append("|").append(phone).append("|").append(notes).append("\n");
        
        // Add communications
        for (Communication comm : communications) {
            sb.append(comm.toFileString());
        }
        
        // Add tasks
        for (Task task : tasks) {
            sb.append(task.toFileString());
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s | Phone: %s | Notes: %s",
                id, name, email, phone, notes);
    }
}
