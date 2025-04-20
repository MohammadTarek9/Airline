import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    private Admin admin;

    @BeforeAll
    public static void setupDatabase() {
        FlightsModel.connectToDatabase();
        AdminModel.connectToDatabase();
    }

    @AfterAll
    public static void cleanup() {
        // Clean up
        try (Statement stmt = FlightsModel.getConnection().createStatement()) {
            stmt.execute("DELETE FROM flight WHERE flightNumber = 'EG101'");
            FlightsModel.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FlightsModel.closeConnection();
        AdminModel.closeConnection();
    }

    @BeforeEach
    public void setup() {
        admin = new Admin("mohammad@flyops.com", "1234567", "mohammad");
    }

    @Test
    public void testSettersAndGetters() {
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

    @Test
    public void testEmptyLogin() {
        String result = admin.login("", admin.getPassword());
        assertEquals("Email and password cannot be empty!", result);

        result = admin.login(admin.getEmail(), "");
        assertEquals("Email and password cannot be empty!", result);

        result = admin.login("", "");
        assertEquals("Email and password cannot be empty!", result);
    }

    @Test
    public void testInvalidPassword() {
        String result = admin.login(admin.getEmail(), "Wrong Password");
        assertEquals("Incorrect password!", result);
    }


    @Test
    public void testInvalidEmail() {
        String result = admin.login("Wrong@email.com", admin.getPassword());
        assertEquals("You do not have an account!", result);
    }

    @Test
    public void testInvalidEmailAndPassword() {
        String result = Admin.login("Wrong@email.com", "Wrong Password");
        assertEquals("You do not have an account!", result);
    }

    @Test
    public void testValidLogin() {
        String result = Admin.login(admin.getEmail(), admin.getPassword());
        assertEquals("success", result);
    }

    @Test
    public void testInvalidAddFlight() {
        Flight flight = new Flight("", 0, "", "", "", "", 0);

        //test Empty Fields
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight));

        //testWrongDateFormat
        flight = new Flight("EG101", -150, "Cairo", "London", "2023/12/31 12:00:00", "2023/12/31 17:00:00", 1000);
        assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", admin.addFlight(flight));

        //testInvalidDeparture
        flight.setDepartureTime("2024-12-31 12:00:00");
        flight.setArrivalTime("2023-12-31 17:00:00");
        assertEquals("Departure time cannot be later than arrival time.", admin.addFlight(flight));

        //testInvalidBaseFare
        flight.setDepartureTime("2023-12-31 12:00:00");
        flight.setFare(-100);
        assertEquals("Base fare cannot be negative.", admin.addFlight(flight));

        //testInvalidCapacity
        flight.setDepartureTime("2023-12-31 12:00:00");
        flight.setFare(100);
        assertEquals("Capacity cannot be negative.", admin.addFlight(flight));
    }

    @Test
    public void testValidAddFlight() {
        Flight flight = new Flight("EG101", 150, "Cairo", "London", "2023-12-31 12:00:00", "2023-12-31 17:00:00", 1000);
        assertEquals("success", admin.addFlight(flight));

    }

}