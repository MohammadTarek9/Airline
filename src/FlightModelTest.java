import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlightModelTest {

    @BeforeAll
    public static void setUp() {
        FlightsModel.connectToDatabase();
        assertNotNull(FlightsModel.getConnection());
    }

    @AfterAll
    public static void cleanup() {
        // Clean up
        try (Statement stmt = FlightsModel.getConnection().createStatement()) {
            stmt.execute("DELETE FROM flight WHERE flightNumber = 'TEST123'");
            FlightsModel.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FlightsModel.closeConnection();
    }

    @Test
    public void testGetAllFlights() {
        ArrayList<Flight> flights = FlightsModel.getAllFlights();
        assertNotEquals(0, flights.size());
        assertEquals("AI202", flights.get(0).getFlightNumber());
    }

    @Test
    public void testGetFlightDetails() {
        Flight flight = FlightsModel.getFlightDetails("AI202");
        assertNotNull(flight);

        assertEquals(180, flight.getCapacity());
        assertEquals("New York", flight.getSource());
        assertEquals("Los Angeles", flight.getDestination());
        assertEquals(300, flight.getBaseFare());
    }

    @Test
    public void testGetFlightDetails_Null() {
        Flight flight = FlightsModel.getFlightDetails("none");
        assertNull(flight);
    }

    @Test
    public void testDecrementBookedSeats_Success() {

    }

    @Test
    @Order(1)
    void testAddFlight() {
        Flight newFlight = new Flight("TEST123", 150, 0, "TestSource", "TestDest", "2025-12-01 10:00:00", "2025-12-01 12:00:00", 150.00);

        String result = FlightsModel.addFlight(newFlight);
        assertEquals("success", result);

        // Verify the flight was added
        Flight retrievedFlight = FlightsModel.getFlightDetails("TEST123");
        assertNotNull(retrievedFlight);
        assertEquals("TestSource", retrievedFlight.getSource());
        assertEquals("TestDest", retrievedFlight.getDestination());
    }

    @Test
    void testCheckIfDelayed_NotDelayedFlight() {
        boolean isDelayed = FlightsModel.checkIfDelayed("AI202");
        assertFalse(isDelayed);
    }

    @Test
    void testGetDelayReason_NotDelayedFlight() {
        String reason = FlightsModel.getDelayReason("AI203");
        assertNull(reason);
    }

    @Test
    void testGetDepartureTime() {
        String departureTime = FlightsModel.getDepartureTime("DL404");
        assertNotNull(departureTime);
        assertTrue(departureTime.contains("2025-10-03 12:00:00"));
    }

    @Test
    void testGetArrivalTime() {
        String arrivalTime = FlightsModel.getArrivalTime("DL404");
        assertNotNull(arrivalTime);
        assertTrue(arrivalTime.contains("2025-10-03 14:30:00"));
    }

    @Test
    void testCheckIfCancelled_NotCancelledFlight() {
        boolean isCancelled = FlightsModel.checkIfCancelled("AI202");
        assertFalse(isCancelled);
    }

    @Test
    @Order(2)
    void testDelayFlight() {
        // Delay a flight
        FlightsModel.delayFlight("TEST123", "Weather delay", "2025-10-08 16:00:00", "2025-10-08 19:00:00");

        // Verify
        boolean isDelayed = FlightsModel.checkIfDelayed("TEST123");
        assertTrue(isDelayed);

        String reason = FlightsModel.getDelayReason("TEST123");
        assertEquals("Weather delay", reason);

        String newDeparture = FlightsModel.getDepartureTime("TEST123");
        assertTrue(newDeparture.contains("2025-10-08 16:00:00"));
    }

    @Test
    @Order(3)
    void testCancelFlight() {
        boolean result = FlightsModel.cancelFlight("TEST123");
        assertTrue(result);

        // Verify
        boolean isCancelled = FlightsModel.checkIfCancelled("TEST123");
        assertTrue(isCancelled);
    }

    @Test
    @Order(4)
    void testGetAllAvailableFlights() {
        ArrayList<Flight> availableFlights = FlightsModel.getAllAvailableFlights();
        assertNotNull(availableFlights);

        // Verify cancelled flight is not included
        boolean foundCancelled = false;
        for (Flight flight : availableFlights) {
            if (flight.getFlightNumber().equals("TEST123")) {
                foundCancelled = true;
                break;
            }
        }
        assertFalse(foundCancelled);

        // Verify regular flights are included
        boolean foundRegular = false;
        for (Flight flight : availableFlights) {
            if (flight.getFlightNumber().equals("AI202")) {
                foundRegular = true;
                break;
            }
        }
        assertTrue(foundRegular);
    }

    @Test
    @Order(5)
    void testCheckIfCancelled_CancelledFlight() {
        boolean isCancelled = FlightsModel.checkIfCancelled("TEST123");
        assertTrue(isCancelled);
    }

    @Test
    @Order(6)
    void testGetDelayReason_DelayedFlight() {
        String reason = FlightsModel.getDelayReason("TEST123");
        assertEquals("Weather delay", reason);
    }

    @Test
    @Order(7)
    void testCheckIfDelayed_DelayedFlight() {
        boolean isDelayed = FlightsModel.checkIfDelayed("TEST123");
        assertTrue(isDelayed);
    }

    @Test
    @Order(8)
    void testDecrementBookedSeats() {
        // First get current booked seats
        Flight flightBefore = FlightsModel.getFlightDetails("TEST123");
        int initialBookedSeats = flightBefore.getBookedSeats();

        // Decrement
        boolean result = FlightsModel.decrementBookedSeats("TEST123");
        assertTrue(result);

        // Verify
        Flight flightAfter = FlightsModel.getFlightDetails("TEST123");
        assertEquals(initialBookedSeats - 1, flightAfter.getBookedSeats());
    }

    @Test
    @Order(9)
    void testIncrementBookedSeats() {
        // First get current booked seats
        Flight flightBefore = FlightsModel.getFlightDetails("TEST123");
        int initialBookedSeats = flightBefore.getBookedSeats();

        // Increment
        FlightsModel.incrementBookedSeats("TEST123");

        // Verify
        Flight flightAfter = FlightsModel.getFlightDetails("TEST123");
        assertEquals(initialBookedSeats + 1, flightAfter.getBookedSeats());
    }
    
}
