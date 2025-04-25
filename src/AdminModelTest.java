import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminModelTest {

    private Connection connection;
    @BeforeAll
    public void setUp() {
        try {
            // Connect to MySQL database
            AdminModel.connectToDatabase();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/airvista", "root", "root"
            );
        } catch (SQLException e) {
            fail("Failed to connect to database: " + e.getMessage());
        }
    }
    @Test
    void testIsAdminExisting() {
        boolean result = AdminModel.isAdmin("mohammad@flyops.com");
        assertTrue(result);
    }

    @Test
    void testIsAdminNonExistent() {
        boolean result = AdminModel.isAdmin("nonexistent@flyops.com");
        assertFalse(result, "Non-existent admin should return false");
    }
    @Test
    void testGetAdminDetailsExisting() {
        Admin admin = AdminModel.getAdminDetails("mohammad@flyops.com");
        assertNotNull(admin, "Admin should not be null");
        assertEquals("mohammad@flyops.com", admin.getEmail());
        assertEquals("1234567", admin.getPassword());
        assertEquals("mohammad", admin.getName());
    }
    @Test
    void testGetAdminDetailsNonExistent() {
        Admin admin = AdminModel.getAdminDetails("modahy@flyops.com");
        assertNull(admin);
    }
    


}