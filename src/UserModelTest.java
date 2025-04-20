import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserModelTest {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/airvista";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static String testEmail = "jana@example.com";

    @BeforeAll
    void setUpAll () throws Exception{
        UserModel.connectToDatabase();
        try(Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM passenger WHERE email = ?")){
                ps.setString(1, testEmail);
                ps.executeUpdate();

        }
    }

    @BeforeEach
    void cleanBeforeEach() throws SQLException{
        try(Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM passenger WHERE email = ?")){
            ps.setString(1, testEmail);
            ps.executeUpdate();

        }
    }

    @Test
    void testStoreUserAndEmailExists(){
        assertFalse(UserModel.isEmailExists(testEmail));
        UserModel.storeUserData(testEmail, "password999", "Jana", "Hany", "+20123456789", 21);
        assertTrue(UserModel.isEmailExists(testEmail));
    }

    @Test
    void testGetPassword(){
        UserModel.storeUserData(testEmail, "mypassword", "Jana", "Hany", "+20123456789", 21);
        String pass = UserModel.getPassword(testEmail);
        assertEquals("mypassword", pass);
    }

    @Test
    void testGetPassengerDetails(){
        UserModel.storeUserData(testEmail, "Hello123", "Jana", "Hany", "+20123456789", 21);
        Passenger passenger = UserModel.getPassengerDetails(testEmail);
        assertNotNull(passenger);
        assertEquals("Jana", passenger.getFirstName());
        assertEquals("Hany", passenger.getLastName());
        assertEquals("+20123456789", passenger.getPhone());
        assertEquals(21, passenger.getAge());
        assertEquals("Hello123", passenger.getPassword());
        assertEquals(testEmail, passenger.getEmail());
    }

    @Test
    void testChangePassword(){
        UserModel.storeUserData(testEmail, "oldpass", "Jana", "Hany", "+20123456789", 21);
        String result = UserModel.changePassword(testEmail, "newpass");
        assertEquals("success", result);
        assertEquals("newpass", UserModel.getPassword(testEmail));
    }
    @Test
    void testChangeEmail() throws Exception{
        UserModel.storeUserData(testEmail, "pass999", "Jana", "Hany", "+20123456789", 21);
        String newEmail = "jana_new@example.com";
        try(Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM passenger WHERE email = ?")){
                ps.setString(1, newEmail);
                ps.executeUpdate();
        }

        String result = UserModel.changeEmail(testEmail, newEmail);
        assertEquals("success", result);
        assertTrue(UserModel.isEmailExists(newEmail));
        assertFalse(UserModel.isEmailExists(testEmail));

        // rollback
        UserModel.changeEmail(newEmail, testEmail);
    }

    @Test
    void  testChangePhone(){
        UserModel.storeUserData(testEmail, "pass123", "Jana", "Hany", "+20123456789", 21);
        String result = UserModel.changePhone(testEmail, "+96954322456");
        assertEquals("success", result);
        Passenger passenger = UserModel.getPassengerDetails(testEmail);
        assertEquals("+96954322456", passenger.getPhone());
    }

    @Test
    void testChangeAge(){
        UserModel.storeUserData(testEmail, "pass123", "Jana", "Hany", "+20123456789", 21);
        String result = UserModel.changeAge(testEmail, 45);
        assertEquals("success", result);
        Passenger passenger = UserModel.getPassengerDetails(testEmail);
        assertEquals(45, passenger.getAge());
    }
}
