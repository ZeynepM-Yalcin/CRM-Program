import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;

public class CRMManager implements Subject {
    private static CRMManager instance;
    private List<Customer> customers;
    private List<Observer> observers;
    private String currentUser;
    private static final String DATA_FILE = "customers.txt";

    private CRMManager() {
        customers = new ArrayList<>();
        observers = new ArrayList<>();
        currentUser = "Default User";
        loadFromFile();
    }

    public static CRMManager getInstance() {
        if (instance == null) {
            instance = new CRMManager();
        }
        return instance;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveToFile();
        notifyObservers("New customer added: " + customer.getName());
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer findCustomerById(int id) {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customers.stream()
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                           c.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                           c.getPhone().contains(keyword))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void addCommunicationToCustomer(int customerId, Communication comm) {
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            customer.addCommunication(comm);
            saveToFile();
            notifyObservers("Communication logged for " + customer.getName());
        }
    }

    public void addTaskToCustomer(int customerId, Task task) {
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            customer.addTask(task);
            saveToFile();
            notifyObservers("Task created for " + customer.getName() + ": " + task.getDescription());
        }
    }

    public void generateCustomerReport(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("\n=== CUSTOMER REPORT ===");
        System.out.println(customer);
        System.out.println("\nCommunications (" + customer.getCommunications().size() + "):");
        for (Communication comm : customer.getCommunications()) {
            System.out.println("  " + comm);
        }
        System.out.println("\nTasks (" + customer.getTasks().size() + "):");
        for (Task task : customer.getTasks()) {
            System.out.println("  " + task);
        }
    }

    public void generateOverallReport() {
        System.out.println("\n=== OVERALL CRM REPORT ===");
        System.out.println("Total Customers: " + customers.size());
        
        int totalCommunications = customers.stream()
                .mapToInt(c -> c.getCommunications().size())
                .sum();
        System.out.println("Total Communications: " + totalCommunications);
        
        long totalTasks = customers.stream()
                .mapToLong(c -> c.getTasks().size())
                .sum();
        System.out.println("Total Tasks: " + totalTasks);
        
        long completedTasks = customers.stream()
                .flatMap(c -> c.getTasks().stream())
                .filter(Task::isCompleted)
                .count();
        System.out.println("Completed Tasks: " + completedTasks);
        
        if (totalTasks > 0) {
            double completionRate = (double) completedTasks / totalTasks * 100;
            System.out.printf("Task Completion Rate: %.1f%%\n", completionRate);
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Customer customer : customers) {
                writer.print(customer.toFileString());
            }
            System.out.println("Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

        
    public void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No data file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            int maxCustomerId = 0;
            int maxCommId = 0;
            int maxTaskId = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                if (parts[0].equals("CUSTOMER")) {
                    int id = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    String email = parts[3];
                    String phone = parts[4];
                    String notes = parts[5];
                    
                    Customer customer = new Customer(name, email, phone, notes);
                    customers.add(customer);
                    
                    if (id > maxCustomerId) maxCustomerId = id;
                    
                } else if (parts[0].equals("COMMUNICATION")) {
                    int id = Integer.parseInt(parts[1]);
                    int customerId = Integer.parseInt(parts[2]);
                    String type = parts[3];
                    String description = parts[4];
                    String timestamp = parts[5];
                    String tags = parts[6];
                    
                    Communication comm = new Communication(customerId, type, description, tags);
                    Customer customer = findCustomerById(customerId);
                    if (customer != null) {
                        customer.addCommunication(comm);
                    }
                    
                    if (id > maxCommId) maxCommId = id;
                    
                } else if (parts[0].equals("TASK")) {
                    int id = Integer.parseInt(parts[1]);
                    int customerId = Integer.parseInt(parts[2]);
                    String description = parts[3];
                    String dueDateStr = parts[4];
                    String priority = parts[5];
                    boolean completed = Boolean.parseBoolean(parts[6]);
                    
                    LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    
                    Task task = new Task(customerId, description, dueDate, priority);
                    task.setCompleted(completed);
                    Customer customer = findCustomerById(customerId);
                    if (customer != null) {
                        customer.addTask(task);
                    }
                    
                    if (id > maxTaskId) maxTaskId = id;
                }
            }
            
            Customer.setNextId(maxCustomerId + 1);
            Communication.setNextId(maxCommId + 1);
            Task.setNextId(maxTaskId + 1);
            
            System.out.println("Loaded " + customers.size() + " customers from " + DATA_FILE);
            
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
}
