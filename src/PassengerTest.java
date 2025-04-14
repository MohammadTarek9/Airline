import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PassengerTest {
    private Flight flight1;
    private Flight flight2;
    private Flight flight3;
    private Passenger Passenger;
    private Seat seat;
    private Booking booking1;
    private Booking booking2;

    /// ///////////////////////////////////////////////////
    @BeforeAll

    public static void setupDatabase() {
        UserModel.connectToDatabase();
        FlightsModel.connectToDatabase();
        BookingsModel.connectToDatabase();
        SeatModel.connectToDatabase();
        AdminModel.connectToDatabase();
    }

    @BeforeEach
    public void setup() {
        Passenger = new Passenger("Mohamed", "yousef", "0120291124124", "mohmmm@gmail.com", "111192004", 20);
        flight1 = new Flight("FN123", 150, 30, "Cairo", "New York", "2025-06-15T10:00", "2025-06-15T15:00", 500.00);
        flight2 = new Flight("FN124", 150, 45, "Cairo", "London", "2025-06-16T08:00", "2025-06-16T12:30", 600.00);
        flight3 = new Flight("FN125", 150, 60, "Dubai", "London", "2025-06-16T14:00", "2025-06-16T18:30", 550.00);

        seat = new Seat("A1", true, "Economy");
        booking1 = new Booking(1, flight1, Passenger, seat, false);
        booking2 = new Booking(2, flight2, Passenger, seat, false);
        Session.setPassenger(Passenger);
        Session.setFlight(flight1);

        System.out.println("setup is executed successfully");
    }

    // public void testPassengerConstructor() {

    // }
    @Test
    @Order(0)
    public void testAllGetters() {
        assertEquals("Mohamed", Passenger.getFirstName());
        assertEquals("yousef", Passenger.getLastName());
        assertEquals("0120291124124", Passenger.getPhone());
        assertEquals("mohmmm@gmail.com", Passenger.getEmail());
        assertEquals("111192004", Passenger.getPassword());
        assertEquals(20, Passenger.getAge());

        assertNotNull(Passenger.getBookings(), "Bookings list should not be null");
        assertTrue(Passenger.getBookings().isEmpty(), "Bookings list should be empty on init");
    }

    @Test
    @Order(1)
    public void testAllSetters() {
        Passenger.setFirstName("Ali");
        assertEquals("Ali", Passenger.getFirstName());
        Passenger.setLastName("Ibrahim");
        assertEquals("Ibrahim", Passenger.getLastName());
        Passenger.setPhone("+201234567890");
        assertEquals("+201234567890", Passenger.getPhone());
        Passenger.setEmail("ali@example.com");
        assertEquals("ali@example.com", Passenger.getEmail());
        Passenger.setPassword("newpassword123");
        assertEquals("newpassword123", Passenger.getPassword());
        Passenger.setAge(30);
        assertEquals(30, Passenger.getAge());
    }

    @Test
    @Order(2)
    public void testGetBookingsInitiallyEmpty() {
        // ArrayList<Booking> bookings = Passenger.getBookings();
        assertNotNull(Passenger.getBookings());
        assertTrue(Passenger.getBookings().isEmpty(), "Bookings list should be initialized empty.");
    }

    @Test
    @Order(3)
    public void testGetBookingsAfterAdding() {
        ArrayList<Booking> bookings = Passenger.getBookings();
        Passenger.addBooking(booking1);
        assertEquals(1, bookings.size(), "Bookings list should contain 1 booking");
        Passenger.addBooking(booking2);
        assertEquals(2, bookings.size(), "Bookings list should contain 2 bookings");
        assertEquals(booking1, bookings.get(0));
        assertEquals(booking2, bookings.get(1));
    }

    @Test
    @Order(4)
    public void testSetAndAddBookings() {

        ArrayList<Booking> newBookings = new ArrayList<>();
        newBookings.add(booking1);
        newBookings.add(booking2);
        Passenger.setBookings(newBookings);
        ArrayList<Booking> bookings = Passenger.getBookings();

        assertEquals(2, bookings.size());
        assertEquals(booking1, bookings.get(0));
        assertEquals(booking2, bookings.get(1));
    }

    @Test
    @Order(30)
    public void testSearchFlights() {
        ArrayList<Flight> result = Passenger.searchFlights("Chicago", "Miami");
    
        assertEquals(4, result.size());
    }
    @Test
    @Order(5)
    public void testFilterFlightsByDate() {
        ArrayList<Flight> allFlights = new ArrayList<>();
        allFlights.add(flight1);
        allFlights.add(flight2);
        allFlights.add(flight3);

        ArrayList<Flight> result = Passenger.filterFlights(allFlights, "2025-06-15");
        assertTrue(result.contains(flight1), "Flight FN123 should be included");
        assertFalse(result.contains(flight2), "Flight FN124 should be included");
        assertFalse(result.contains(flight3), "Flight FN125 should not be included");
    }

    @Test
    @Order(6)
    public void testBookFlight() {
        try (
                MockedStatic<SeatModel> seatModelMock = Mockito.mockStatic(SeatModel.class);
                MockedStatic<BookingsModel> bookingsModelMock = Mockito.mockStatic(BookingsModel.class);
                MockedStatic<FlightsModel> flightsModelMock = Mockito.mockStatic(FlightsModel.class)) {

            seatModelMock.when(() -> SeatModel.getSeatDetails("A1",
                    "FN123")).thenReturn(seat);
            bookingsModelMock.when(() -> BookingsModel.addBooking("FN123", Passenger,
                    "A1")).thenReturn(10);

            seatModelMock.when(() -> SeatModel.updateSeatAvailability("FN123", "A1",
                    false)).thenReturn(true);
            flightsModelMock.when(() -> FlightsModel.incrementBookedSeats("FN123")).thenAnswer(inv -> null);

            boolean result = Passenger.BookFlight("A1");

            assertTrue(result, "Booking should succeed");
            assertEquals(1, Passenger.getBookings().size());
            Booking booking = Passenger.getBookings().get(0);
            assertEquals("A1", booking.getSeat().getSeat_id());
            assertEquals(10, booking.getBookingID());
            assertEquals(flight1.getFlightNumber(),
                    booking.getFlight().getFlightNumber());
            assertEquals(Passenger.getEmail(), booking.getPassenger().getEmail());
            assertFalse(booking.getIsConfirmed());
        }
    }

    @Test
    @Order(7)
    public void testCancelBookingSuccess() {
        try (
                MockedStatic<BookingsModel> bookingsModelMock = Mockito.mockStatic(BookingsModel.class);
                MockedStatic<SeatModel> seatModelMock = Mockito.mockStatic(SeatModel.class);
                MockedStatic<FlightsModel> flightsModelMock = Mockito.mockStatic(FlightsModel.class)) {

            Seat mockSeat = new Seat("A1", false, "Economy");
            Flight mockFlight = new Flight("FN123", 150, 30, "Cairo", "New York",
                    "2025-06-15T10:00", "2025-06-15T15:00", 500.00);
            int bookingId = 1;

            bookingsModelMock.when(() -> BookingsModel.getBookingSeat(bookingId)).thenReturn(mockSeat);
            bookingsModelMock.when(() -> BookingsModel.getBookingFlight(bookingId)).thenReturn(mockFlight);
            seatModelMock.when(() -> SeatModel.updateSeatAvailability("FN123", "A1",
                    true)).thenReturn(true);
            flightsModelMock.when(() -> FlightsModel.decrementBookedSeats("FN123")).thenReturn(true);
            bookingsModelMock.when(() -> BookingsModel.deleteBooking(bookingId)).thenReturn(true);

            String result = Passenger.cancelBooking(new ArrayList<>(), bookingId);

            assertEquals("success", result, "Cancellation should succeed");
        }
    }

    @Test
    @Order(8)
    /// ////////////////////////////////////////////////////////////////////////
    public void testCreateAccountSuccess() {

        String email = "testuser54@example.com";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "+123456789012";
        String age = "30";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("success", result);
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////
    @Test
    @Order(9)
    public void testCreateAccountMissingField() {

        String email = "testuser@example.com";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "";
        String age = "30";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("All fields are required!", result, "Should return error for missing fields.");
    }

    @Test
    @Order(10)
    public void testCreateAccountInvalidEmail() {
        // Arrange: invalid email format
        String email = "invalid-email";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "+123456789";
        String age = "20";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("Invalid email format!", result);
    }

    @Test
    @Order(11)
    public void testCreateAccountInvalidPhone() {

        String email = "testuser3@example.com";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "123456789";
        String age = "30";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("Invalid phone number format!", result);
    }

    @Test
    @Order(12)
    public void testCreateAccountAgeNotNumber() {
        String email = "testuser3@example.com";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "+123456789012";
        String age = "yes";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("Age must be a number!", result);
    }

    /// ///////////////////////////////////////////////////////////////
    @Test
    @Order(13)
    public void testCreateAccountEmailExists() {
        String email = "testuser54@example.com";
        String password = "password123";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "+123456789012";
        String age = "30";

        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);

        assertEquals("Email already exists!", result);
    }

    /// ////////////////////////////////////////////////////////////////////////
    @Test
    @Order(14)
    public void testCreateAccountPasswordTooShort() {

        String email = "testuser454555@example.com";
        String password = "pwd";
        String firstName = "Mohamed";
        String lastName = "seif";
        String phone = "+123456789012";
        String age = "30";
        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);
        assertEquals("Password must be at least 6 characters long!", result);
    }

    @Test
    @Order(15)
    public void testRefreshBookings() {
    
    ArrayList<Booking> mockBookings = new ArrayList<>();
    
    mockBookings.add(booking1);
    mockBookings.add(booking2);
    try(MockedStatic <BookingsModel> bookingsModelMock =
    Mockito.mockStatic(BookingsModel.class)){
    bookingsModelMock.when(() ->
    BookingsModel.getAllBookings(Passenger.getEmail())).thenReturn(mockBookings);
    
    Passenger.refreshBookings();
    
    assertEquals(2, Passenger.getBookings().size(), "The bookings list should contain 2 bookings.");
    assertTrue(Passenger.getBookings().contains(booking1));
    assertTrue(Passenger.getBookings().contains(booking2));
    }
    //bookingsModelMock.when(() ->bookingsModelMock.getAllBookings(Passenger.getEmail())).thenReturn(mockBookings);
    }

    @Test
    @Order(16)
    public void testLoginWithEmptyEmailAndPassword() {
        String result = Passenger.login("", "");
        assertEquals("Email and password cannot be empty!", result);
    }

    @Test
    @Order(17)
    public void testLoginWithNonExistentEmail() {

        String result = Passenger.login("nonexistent@example.com", "somepassword");
        assertEquals("You do not have an account!", result);
    }

    @Test
    @Order(18)
    public void testLoginWithIncorrectPassword() {

        String result = Passenger.login("testuser54@example.com", "wrongpassword");
        assertEquals("Incorrect password!", result);
    }

    @Test
    @Order(19)
    public void testLoginWithCorrectEmailAndPassword() {

        String result = Passenger.login("testuser54@example.com", "password123");
        assertEquals("success", result);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @Order(29)
    public void testUpdateAccountWithValidData() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser55@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser55@example.com", "neij@example.com", "newpassword123",
                "newpassword123", "+12345678901", "31");
        assertEquals("success", result);
    }


    /// ////////////////////////////////////////////////////////////////////////////
    @Test
    @Order(20)
    public void testUpdateAccountWithInvalidEmailFormat() {
        Passenger passenger = new Passenger("Mohamed", "Dahy", "+1292149714", "testuser54@example.com", "password123",
                20);
        String result = passenger.updateAccount("testuser54@example.com", "ezayy", "newpassword123", "newpassword123",
                "+12345678901", "31");
        assertEquals("Invalid email format!", result);
    }

    @Test
    @Order(21)
    public void testUpdateAccountWithEmailAlreadyExists() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        Passenger passenger2 = new Passenger();
        passenger2.createAccount("old@example.com", "password456", "Ali", "Ahmed",
        "+9876543210", "41");
        String result = passenger.updateAccount("testuser54@example.com", "old@example.com", "newpassword320",
                "newpassword320", "+12345678901", "45");
        assertEquals("Email already exists!", result);
    }

    @Test
    @Order(22)
    public void testUpdateAccountWithShortPassword() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser54@example.com", "new", "new",
                "+12345678901", "31");
        assertEquals("Password must be at least 6 characters long!", result);
    }

    @Test
    @Order(23)
    public void testUpdateAccountWithPasswordMismatch() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser54@example.com", "newpassword123",
                "differentpassword", "+12345678901", "31");
        assertEquals("Passwords do not match!", result);
    }

    @Test
    @Order(24)
    public void testUpdateAccountWithInvalidPhoneFormat() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser54@example.com", "newpassword123",
                "newpassword123", "12345", "31");
        assertEquals("Invalid phone number format!", result);
    }

    @Test
    @Order(25)
    public void testUpdateAccountWithInvalidAgeFormat() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser55@example.com", "newpassword123",
                "newpassword123", "+12345678901", "Invalidage");
        assertEquals("Age must be a number!", result);
    }

    @Test
    @Order(26)
    public void testUpdateAccountWithNoChanges() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser54@example.com", "password123",
                "password123", "+1234567890", "30");
        assertEquals("New age cannot be the same as the old age!", result);
    }

    @Test
    @Order(27)
    public void testUpdateAccountWithSameEmail() {
        Passenger passenger = new Passenger("Mohamed", "Seif", "+1234567890", "testuser54@example.com", "password123",
                30);
        String result = passenger.updateAccount("testuser54@example.com", "testuser54@example.com", "newpassword123",
                "newpassword123", "+12345678901", "31");
        assertEquals("New email cannot be the same as the old email!", result);
    }

    @Test
    @Order(28)
    public void testremovebooking() {

        Passenger.addBooking(booking1);
        Passenger.addBooking(booking2);

        assertEquals(2, Passenger.getBookings().size());
        assertTrue(Passenger.getBookings().contains(booking1));
        assertTrue(Passenger.getBookings().contains(booking2));

        Passenger.removeBooking(booking1);

        assertEquals(1, Passenger.getBookings().size());
        assertFalse(Passenger.getBookings().contains(booking1));
        assertTrue(Passenger.getBookings().contains(booking2));

    }
}
