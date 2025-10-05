import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class CustomerTest {
    
    private Customer customer;
    private Communication communication;
    private Task task;
    
    @BeforeEach
    void setUp() {
        Customer.setNextId(1); // Reset ID counter for consistent testing
        customer = new Customer("John Doe", "john@example.com", "123-456-7890", "VIP Customer");
        communication = new Communication(1, "email", "Follow-up call", "urgent");
        task = new Task(1, "Call customer", LocalDateTime.now().plusDays(1), "high");
    }
    
    @Test
    @DisplayName("Customer creation should set all fields correctly")
    void testCustomerCreation() {
        assertEquals(1, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("123-456-7890", customer.getPhone());
        assertEquals("VIP Customer", customer.getNotes());
        assertNotNull(customer.getCommunications());
        assertNotNull(customer.getTasks());
        assertTrue(customer.getCommunications().isEmpty());
        assertTrue(customer.getTasks().isEmpty());
    }
    
    @Test
    @DisplayName("Customer ID should auto-increment")
    void testCustomerIdAutoIncrement() {
        Customer customer2 = new Customer("Jane Smith", "jane@example.com", "987-654-3210", "Regular");
        assertEquals(2, customer2.getId());
    }
    
    @Test
    @DisplayName("Customer setters should update fields correctly")
    void testCustomerSetters() {
        customer.setName("John Smith");
        customer.setEmail("johnsmith@example.com");
        customer.setPhone("555-555-5555");
        customer.setNotes("Updated notes");
        
        assertEquals("John Smith", customer.getName());
        assertEquals("johnsmith@example.com", customer.getEmail());
        assertEquals("555-555-5555", customer.getPhone());
        assertEquals("Updated notes", customer.getNotes());
    }
    
    @Test
    @DisplayName("Adding communication should work correctly")
    void testAddCommunication() {
        customer.addCommunication(communication);
        
        assertEquals(1, customer.getCommunications().size());
        assertEquals(communication, customer.getCommunications().get(0));
    }
    
    @Test
    @DisplayName("Adding task should work correctly")
    void testAddTask() {
        customer.addTask(task);
        
        assertEquals(1, customer.getTasks().size());
        assertEquals(task, customer.getTasks().get(0));
    }
    
    @Test
    @DisplayName("Adding multiple communications and tasks should work")
    void testMultipleAdditions() {
        Communication comm2 = new Communication(1, "phone", "Initial contact", "sales");
        Task task2 = new Task(1, "Send proposal", LocalDateTime.now().plusDays(2), "medium");
        
        customer.addCommunication(communication);
        customer.addCommunication(comm2);
        customer.addTask(task);
        customer.addTask(task2);
        
        assertEquals(2, customer.getCommunications().size());
        assertEquals(2, customer.getTasks().size());
    }
    
    @Test
    @DisplayName("toFileString should generate correct format")
    void testToFileString() {
        customer.addCommunication(communication);
        customer.addTask(task);
        
        String fileString = customer.toFileString();
        
        assertTrue(fileString.startsWith("CUSTOMER|1|John Doe|john@example.com|123-456-7890|VIP Customer"));
        assertTrue(fileString.contains("COMMUNICATION|"));
        assertTrue(fileString.contains("TASK|"));
    }
    
    @Test
    @DisplayName("toString should format customer info correctly")
    void testToString() {
        String result = customer.toString();
        String expected = "ID: 1 | Name: John Doe | Email: john@example.com | Phone: 123-456-7890 | Notes: VIP Customer";
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Customer with null/empty values should handle gracefully")
    void testCustomerWithNullValues() {
        Customer nullCustomer = new Customer("", "", "", "");
        assertEquals("", nullCustomer.getName());
        assertEquals("", nullCustomer.getEmail());
        assertEquals("", nullCustomer.getPhone());
        assertEquals("", nullCustomer.getNotes());
    }
}