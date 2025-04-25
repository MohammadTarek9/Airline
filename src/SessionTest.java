import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @AfterEach
    void tearDown() {
        Session.setPassenger(null);
        Session.setBookings(null);
        Session.setFlight(null);
        Session.setAdmin(null);
    }
    @Test
    void testPassengerSetterAndGetter() {
        Passenger passenger = new Passenger();
        Session.setPassenger(passenger);
        assertSame(passenger, Session.getPassenger(), "getPassenger() should return the value set by setPassenger()");
    }
    @Test
    void testBookingSetterAndGetter() {
        Booking booking = new Booking(1, 
            new Flight("FL123", 10, "A","B","t","t",100.0), 
            new Passenger(), 
            new Seat("1A", true, "Economy"));
        Session.setBookings(booking);
        assertSame(booking, Session.getBookings(), "getBookings() should return the value set by setBookings()");
    }
    @Test
    void testFlightSetterAndGetter() {
        Flight flight = new Flight("FL123", 20, "X","Y","t","t",200.0);
        Session.setFlight(flight);
        assertSame(flight, Session.getFlight(), "getFlight() should return the value set by setFlight()");
    }
    @Test
    void testAdminSetterAndGetter() {
        Admin admin = new Admin("admin123", "password123", "Admin Name");
        Session.setAdmin(admin);
        assertSame(admin, Session.getAdmin(), "getAdmin() should return the value set by setAdmin()");
    }
    @Test
    void testDefaultsAreNull() {
        assertNull(Session.getPassenger(), "Passenger should be null by default");
        assertNull(Session.getBookings(), "Booking should be null by default");
        assertNull(Session.getFlight(), "Flight should be null by default");
        assertNull(Session.getAdmin(), "Admin should be null by default");
    }
}
