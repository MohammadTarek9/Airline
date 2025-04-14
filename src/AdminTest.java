import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    private Admin admin;

    @BeforeEach
    public void setup(){
        admin = new Admin("mohammad@flyops.com", "1234567", "mohammad");
    }

    @Test
    public void testSettersAndGetters(){
        assertEquals("mohammad@flyops.com", admin.getEmail());
        admin.setEmail("admin@flyops.com");
        assertEquals("admin@flyops.com", admin.getEmail());

        assertEquals("1234567", admin.getPassword());
        admin.setPassword("newpassword123");
        assertEquals("newpassword123", admin.getPassword());

        assertEquals("mohammad", admin.getName());
        admin.setName("admin");
        assertEquals("admin", admin.getName());

    }
}