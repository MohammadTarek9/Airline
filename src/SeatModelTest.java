
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeatModelTest {

    private final String flightNumber = "TEST123";
    private Connection connection;

    @BeforeAll
    public void setUp() {
        try {
            SeatModel.connectToDatabase();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/airvista", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @BeforeEach
    public void initFlightAndSeats() {
        try {
            String insertFlightSQL = """
                        INSERT INTO flight (flightNumber, capacity, bookedSeats, 
                        source, destination, departureTime, arrivalTime, baseFare)
                        VALUES (?, 100, 0, 'TestCityA', 'TestCityB', '2025-04-15 10:00:00', '2025-04-15 12:00:00', 200.0)
                    """;
            try (PreparedStatement stmt = connection.prepareStatement(insertFlightSQL)) {
                stmt.setString(1, flightNumber);
                stmt.executeUpdate();
            }
            String insertSeatsSQL = """
                        INSERT INTO seat (seat_id, flightNumber, available, seatType) VALUES
                        ('1A', ?, true, 'economy'),
                        ('1B', ?, false, 'business'),
                        ('2A', ?, true, 'first')
                    """;
            try (PreparedStatement stmt = connection.prepareStatement(insertSeatsSQL)) {
                stmt.setString(1, flightNumber);
                stmt.setString(2, flightNumber);
                stmt.setString(3, flightNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            fail("Failed to initialize test flight or seats: " + e.getMessage());
        }
    }

    @AfterEach
    public void cleanUpSeatsAndFlight() {
        try {
            // Delete test seats
            String deleteSeatsSQL = "DELETE FROM seat WHERE flightNumber = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteSeatsSQL)) {
                stmt.setString(1, flightNumber);
                stmt.executeUpdate();
            }

            // Delete test flight
            String deleteFlightSQL = "DELETE FROM flight WHERE flightNumber = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteFlightSQL)) {
                stmt.setString(1, flightNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Failed to clean up test data: " + e.getMessage());
        }
    }

    @AfterAll
    public void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }

    @Test
    void testUpdateSeatAvailabilitySuccess() {
        boolean result = SeatModel.updateSeatAvailability(flightNumber, "1A", false);
        assertTrue(result, "Seat availability should be updated successfully");

        String query = "SELECT available FROM seat WHERE seat_id = ? AND flightNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "1A");
            stmt.setString(2, flightNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                assertFalse(rs.getBoolean("available"), "Seat 1A should be unavailable");
            } else {
                fail("Seat 1A not found in database");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }

    @Test
    void testUpdateSeatAvailabilityNonExistent() {
        boolean result = SeatModel.updateSeatAvailability(flightNumber, "999", true);
        assertFalse(result, "Updating non-existent seat should return false");
    }

    @Test
    void testGetAllSeats() {
        ArrayList<Seat> seats = SeatModel.getAllSeats(flightNumber);
        assertEquals(3, seats.size(), "Should return 3 seats for flight " + flightNumber);

        boolean found1A = false, found1B = false, found2A = false;
        for (Seat seat : seats) {
            if (seat.getSeat_id().equals("1A")) {
                found1A = true;
                assertTrue(seat.isAvailable(), "Seat 1A should be available");
                assertEquals("economy", seat.getSeatType(), "Seat 1A should be economy");
            } else if (seat.getSeat_id().equals("1B")) {
                found1B = true;
                assertFalse(seat.isAvailable(), "Seat 1B should be unavailable");
                assertEquals("business", seat.getSeatType(), "Seat 1B should be business");
            } else if (seat.getSeat_id().equals("2A")) {
                found2A = true;
                assertTrue(seat.isAvailable(), "Seat 2A should be available");
                assertEquals("first", seat.getSeatType(), "Seat 2A should be first");
            }
        }
        assertTrue(found1A && found1B && found2A);
    }

    @Test
    void testGetAllSeatsNonExistent() {
        ArrayList<Seat> seats = SeatModel.getAllSeats("INVALID");
        assertTrue(seats.isEmpty());
    }

    @Test
    void testGetSeatTypeSuccess() {
        String seatType = SeatModel.getSeatType("1A", flightNumber);
        assertEquals("economy", seatType);
    }

    @Test
    void testGetSeatTypeNonExistent() {
        String seatType = SeatModel.getSeatType("999", flightNumber);
        assertNull(seatType);
    }

    @Test
    void testGetSeatDetailsSuccess() {
        Seat seat = SeatModel.getSeatDetails("1A", flightNumber);
        assertNotNull(seat);
        assertEquals("1A", seat.getSeat_id());
        assertTrue(seat.isAvailable());
        assertEquals("economy", seat.getSeatType());
    }

    @Test
    void testGetSeatDetailsNonExistent() {
        Seat seat = SeatModel.getSeatDetails("999", flightNumber);
        assertNull(seat);
    }
}