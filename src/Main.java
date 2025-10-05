import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CRMManager crm = CRMManager.getInstance();

    public static void main(String[] args) {
        // Set up notification manager
        NotificationManager notificationManager = new NotificationManager("System");
        crm.addObserver(notificationManager);

        System.out.println("=== Welcome to Simple CRM System ===");
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        crm.setCurrentUser(userName);
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    viewAllCustomers();
                    break;
                case 3:
                    searchCustomers();
                    break;
                case 4:
                    addCommunication();
                    break;
                case 5:
                    addTask();
                    break;
                case 6:
                    viewCustomerDetails();
                    break;
                case 7:
                    markTaskComplete();
                    break;
                case 8:
                    generateReports();
                    break;
                case 9:
                    System.out.println("Thank you for using Simple CRM System!");
                     crm.saveToFile();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== CRM MENU ===");
        System.out.println("1. Create Customer");
        System.out.println("2. View All Customers");
        System.out.println("3. Search Customers");
        System.out.println("4. Add Communication");
        System.out.println("5. Add Task");
        System.out.println("6. View Customer Details");
        System.out.println("7. Mark Task Complete");
        System.out.println("8. Generate Reports");
        System.out.println("9. Exit");
    }

    private static void createCustomer() {
        System.out.println("\n--- Create New Customer ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        Customer customer = CRMFactory.createCustomer(name, email, phone, notes);
        crm.addCustomer(customer);
        System.out.println("Customer created successfully with ID: " + customer.getId());
    }

    private static void viewAllCustomers() {
        System.out.println("\n--- All Customers ---");
        List<Customer> customers = crm.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void searchCustomers() {
        System.out.print("Enter search partial keyword (name, email, or phone): ");
        String keyword = scanner.nextLine();
        List<Customer> results = crm.searchCustomers(keyword);
        
        System.out.println("\n--- Search Results ---");
        if (results.isEmpty()) {
            System.out.println("No customers found matching: " + keyword);
        } else {
            for (Customer customer : results) {
                System.out.println(customer);
            }
        }
    }

    private static void addCommunication() {
        System.out.println("\n--- Add Communication ---");
        int customerId = getIntInput("Enter Customer ID: ");
        Customer customer = crm.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("Communication types: phone, email, meeting");
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Tags (optional): ");
        String tags = scanner.nextLine();

        Communication comm = CRMFactory.createCommunication(customerId, type, description, tags);        crm.addCommunicationToCustomer(customerId, comm);
        System.out.println("Communication added successfully!");
    }

    private static void addTask() {
        System.out.println("\n--- Add Task ---");
        int customerId = getIntInput("Enter Customer ID: ");
        Customer customer = crm.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.print("Task description: ");
        String description = scanner.nextLine();
        System.out.print("Days from now for due date: ");
        int days = getIntInput("");
        System.out.println("Priority levels: high, medium, low");
        System.out.print("Priority: ");
        String priority = scanner.nextLine();

        LocalDateTime dueDate = LocalDateTime.now().plusDays(days);
        Task task = CRMFactory.createTask(customerId, description, dueDate, priority);        crm.addTaskToCustomer(customerId, task);
        System.out.println("Task added successfully!");
    }

    private static void viewCustomerDetails() {
        int customerId = getIntInput("Enter Customer ID: ");
        crm.generateCustomerReport(customerId);
    }

    private static void markTaskComplete() {
        System.out.println("\n--- Mark Task Complete ---");
        int customerId = getIntInput("Enter Customer ID: ");
        Customer customer = crm.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        List<Task> tasks = customer.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found for this customer.");
            return;
        }

        System.out.println("Tasks for " + customer.getName() + ":");
        for (Task task : tasks) {
            System.out.println(task);
        }

        int taskId = getIntInput("Enter Task ID to mark complete: ");
        Task taskToComplete = tasks.stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElse(null);

        if (taskToComplete != null) {
            taskToComplete.setCompleted(true);
            crm.saveToFile();
            System.out.println("Task marked as completed!");
            crm.notifyObservers("Task completed for " + customer.getName() + ": " + taskToComplete.getDescription());
        } else {
            System.out.println("Task not found!");
        }
    }

    private static void generateReports() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Customer Report");
        System.out.println("2. Overall Report");
        int choice = getIntInput("Choose report type: ");

        switch (choice) {
            case 1:
                int customerId = getIntInput("Enter Customer ID: ");
                crm.generateCustomerReport(customerId);
                break;
            case 2:
                crm.generateOverallReport();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}