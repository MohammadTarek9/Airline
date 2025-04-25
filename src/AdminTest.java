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
            stmt.execute("DELETE FROM flight WHERE flightNumber in ('EG101', 'DUPLICATE101')");
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
    public void testFields() {
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

    @Test
    public void testInvalidAddFlight() {
    // Create a flight with valid values
    Flight flight = new Flight("DUPLICATE101", 100, "Cairo", "Dubai", "2025-12-10 10:00:00", "2025-12-10 15:00:00", 500);

    assertEquals("success", admin.addFlight(flight));

    // Add it again with the same flight number - assuming duplicate is rejected by the model
    assertEquals("Error adding flight", admin.addFlight(flight));
    }

    
    @Test
    public void testInvalidTimes(){
        Flight flight_1 = new Flight("EG101", -150, "Cairo", "London", "2023-12-31 12:00:00", "2023/12/31 17:00:00", 1000);
        assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", admin.addFlight(flight_1));

        Flight flight_2 = new Flight("EG101", -150, "Cairo", "London", "2023/12/31 12:00:00", "2023-12-31 17:00:00", 1000);
        assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", admin.addFlight(flight_2));

    }

    @Test
    public void testIndividualEmptyFields() {
        // Empty flight number
        Flight flight1 = new Flight("", 150, "Cairo", "London", "2025-12-01 12:00:00", "2025-12-01 17:00:00", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight1));

        // Empty source
        Flight flight2 = new Flight("EG102", 150, "", "London", "2025-12-01 12:00:00", "2025-12-01 17:00:00", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight2));

        // Empty destination
        Flight flight3 = new Flight("EG103", 150, "Cairo", "", "2025-12-01 12:00:00", "2025-12-01 17:00:00", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight3));

        // Empty departure time
        Flight flight4 = new Flight("EG104", 150, "Cairo", "London", "", "2025-12-01 17:00:00", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight4));

        // Empty arrival time
        Flight flight5 = new Flight("EG105", 150, "Cairo", "London", "2025-12-01 12:00:00", "", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight5));

        // Capacity is 0
        Flight flight6 = new Flight("EG106", 0, "Cairo", "London", "2025-12-01 12:00:00", "2025-12-01 17:00:00", 500);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight6));

        // Base fare is 0
        Flight flight7 = new Flight("EG107", 150, "Cairo", "London", "2025-12-01 12:00:00", "2025-12-01 17:00:00", 0);
        assertEquals("Please fill in all fields correctly.", admin.addFlight(flight7));
    }
    
}