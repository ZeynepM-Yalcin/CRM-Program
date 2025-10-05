import java.time.LocalDateTime;

public class CRMFactory {
    public static Customer createCustomer(String name, String email, String phone, String notes) {
        return new Customer(name, email, phone, notes);
    }

    public static Communication createCommunication(int customerId, String type, String description, String tags) {
        return new Communication(customerId, type, description, tags);
    }

    public static Task createTask(int customerId, String description, LocalDateTime dueDate, String priority) {
        return new Task(customerId, description, dueDate, priority);
    }
}

