import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {

    private Flight flight;

    @BeforeEach
    public void setUp(){
        flight = new Flight("FL123", 5,2, 
        "NYC", "LAX", "2030-01-01T10:00:00", 
        "2030-01-01T14:00:00", 100.0);
    }

    @Test
    public void testGettersAndSetters(){
        assertEquals("FL123", flight.getFlightNumber());
        flight.setFlightNumber("FL999");
        assertEquals("FL999", flight.getFlightNumber());


        assertEquals(5, flight.getCapacity());
        flight.setCapacity(10);
        assertEquals(10, flight.getCapacity());


        assertEquals(2, flight.getBookedSeats());
        flight.setBookedSeats(8);
        assertEquals(8, flight.getBookedSeats());


        assertEquals("NYC", flight.getSource());
        flight.setSource("SFO");
        assertEquals("SFO", flight.getSource());


        assertEquals("LAX", flight.getDestination());
        flight.setDestination("MIA");
        assertEquals("MIA", flight.getDestination());


        assertEquals("2030-01-01T10:00:00", flight.getDepartureTime());
        flight.setDepartureTime("2025-04-10T11:00:00");
        assertEquals("2025-04-10T11:00:00", flight.getDepartureTime());


        assertEquals("2030-01-01T14:00:00", flight.getArrivalTime());
        flight.setArrivalTime("2025-04-10T19:20:00");
        assertEquals("2025-04-10T19:20:00", flight.getArrivalTime());


        assertEquals(100.0, flight.getBaseFare());
        flight.setFare(150.0);
        assertEquals(150.0, flight.getBaseFare());

        // Seats
        ArrayList<Seat> seats = new ArrayList<>();
        Seat s = new Seat("1A",true,"Economy");
        seats.add(s);
        flight.setSeats(seats);
        assertSame(seats, flight.getSeats());
    }

    @Test
    public void testCapacityAndBookedSeatsValidation(){
        //cannot set capacity below booked seats (currently 2)
        flight.setCapacity(1);
        assertEquals(5, flight.getCapacity()); // no changes

        //cannot set bookedseats above capacity
        flight.setBookedSeats(8);
        assertEquals(2, flight.getBookedSeats());
        
    }

    @Test
    public void testBookSeatAndCancelBooking(){
        flight.bookSeat();
        assertEquals(3, flight.getBookedSeats());

        // fill to capacity
        flight.setBookedSeats(flight.getCapacity());
        flight.bookSeat();
        assertEquals(flight.getCapacity(), flight.getBookedSeats());

        // cancel
        flight.cancelBooking();
        assertEquals(flight.getCapacity() - 1, flight.getBookedSeats());

        // bring to zero and cancel again
        flight.setBookedSeats(0);
        flight.cancelBooking();
        assertEquals(0, flight.getBookedSeats());
    }

    @Test
    public void testCalculateFare(){
        double base = flight.getBaseFare();
        assertEquals(base, flight.calculateFare("economy"));
        assertEquals(base*1.5, flight.calculateFare("business"));
        assertEquals(base*2, flight.calculateFare("first"));
        assertEquals(base, flight.calculateFare("invalid")); // remains unchanged
    }

    @Test
    public void testGetAllFlights(){
        ArrayList<Flight> fake = new ArrayList<>();
        fake.add(flight);
        try(MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class)){
            fm.when(FlightsModel::getAllFlights).thenReturn(fake);

            ArrayList<Flight> result = Flight.getAllFlights();
            assertSame(fake, result);
            fm.verify(FlightsModel::getAllFlights, Mockito.times(1));
        }
    }

    @Test
    public void testHandleAllDelays_NoDelay(){
        try(MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class)){
            fm.when(() -> FlightsModel.checkIfDelayed("FL123")).thenReturn(false);

            String reason = flight.handleDelays();
            assertEquals("nothing", reason);
        }
    }

    @Test
    public void testHandleAllDelays_WithDelay(){
        try(MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class)){
            fm.when(() -> FlightsModel.checkIfDelayed("FL123")).thenReturn(true);
            fm.when(() -> FlightsModel.getDelayReason("FL123")).thenReturn("Weather");
            fm.when(() -> FlightsModel.getDepartureTime("FL123")).thenReturn("2030-01-01T12:00:00");
            fm.when(() -> FlightsModel.getArrivalTime("FL123")).thenReturn("2030-01-01T16:00:00");

            String reason = flight.handleDelays();
            assertEquals("Weather", reason);
            assertEquals("2030-01-01T12:00:00", flight.getDepartureTime());
            assertEquals("2030-01-01T16:00:00", flight.getArrivalTime());

            
            fm.verify(() -> FlightsModel.checkIfDelayed("FL123"), Mockito.times(1));
            fm.verify(() -> FlightsModel.getDelayReason("FL123"), Mockito.times(1));
            fm.verify(() -> FlightsModel.getDepartureTime("FL123"), Mockito.times(1));
            fm.verify(() -> FlightsModel.getArrivalTime("FL123"), Mockito.times(1));
        }
    }

    @Test
    public void testIsCancelled_NoCancellation(){
        Passenger Passenger = new Passenger(); 
        try(MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class);
        MockedStatic<Session> sm = Mockito.mockStatic(Session.class)){

            fm.when(() -> FlightsModel.checkIfCancelled("FL123")).thenReturn(false);

            // Stub Session to return our dummy passenger
            sm.when(Session::getPassenger).thenReturn(Passenger);

            boolean cancelled = flight.isCancelled();
            assertFalse(cancelled);
            fm.verify(() -> FlightsModel.checkIfCancelled("FL123"), Mockito.times(1));
            // since not cancelled , no deletion of booking or decrementing of booked seats
        }
    }

    @Test
    public void testIsCancelled_WithCancellation(){
        // add for a passenger with 2 bookings , 1 includes the flight FL123
        Passenger passenger = new Passenger();
        passenger.setBookings(new ArrayList<>());
        Booking b1 = new Booking(1,flight,passenger,new Seat("1A",true,"Economy"));
        Booking b2 = new Booking(2,new Flight("XX", 10, "A", "B", "t", "t", 10.0), passenger, new Seat("1B",true,"Economy"));

        passenger.addBooking(b1);
        passenger.addBooking(b2);

        try(MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class);
            MockedStatic<Session> sm = Mockito.mockStatic(Session.class);
            MockedStatic<BookingsModel> bm = Mockito.mockStatic(BookingsModel.class);
            MockedStatic<SeatModel> smodel = Mockito.mockStatic(SeatModel.class)){

                // Flight Model reports cancelled
                fm.when(() -> FlightsModel.checkIfCancelled("FL123")).thenReturn(true);

                // Session returns our passenger
                sm.when(Session::getPassenger).thenReturn(passenger);

                bm.when(() -> BookingsModel.deleteBooking(1)).thenReturn(true);
                fm.when(() -> FlightsModel.decrementBookedSeats("FL123")).thenReturn(true);
                smodel.when(() -> SeatModel.updateSeatAvailability("FL123","1A",true)).thenReturn(true);

                boolean cancelled = flight.isCancelled();

                assertTrue(cancelled);

                fm.verify(() -> FlightsModel.checkIfCancelled("FL123"), Mockito.times(1));
                bm.verify(() -> BookingsModel.deleteBooking(1), Mockito.times(1));
                fm.verify(() -> FlightsModel.decrementBookedSeats("FL123"), Mockito.times(1));
                smodel.verify(() -> SeatModel.updateSeatAvailability("FL123", "1A", true), Mockito.times(1));

        }
    }

    @Test
    public void testDelayFlight_InvalidDateFormat() {
        String result = flight.delayFlight("2030-01-01 10:00", "2030-01-01 14:00", "Maintenance");
        assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", result);
    }

    @Test
    public void testDelayFlight_DepartureAfterArrival() {
        String result = flight.delayFlight("2030-01-01 15:00:00", "2030-01-01 14:00:00", "Maintenance");
        assertEquals("Departure time cannot be later than arrival time.", result);
    }

    @Test
    public void testDelayFlight_DepartureBeforeCurrentTime() {
        String pastTime = LocalDateTime.now().minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String result = flight.delayFlight(pastTime, "2030-01-01 14:00:00", "Maintenance");
        assertEquals("New departure time cannot be earlier than the current time.", result);
    }
    @Test
    public void testDelayFlight_NewArivalBeforeCurrentTime() {
        String pastTime = LocalDateTime.now().minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String result = flight.delayFlight(pastTime, "2030-01-01 14:00:00", "Maintenance");
        assertEquals("New departure time cannot be earlier than the current time.", result);
    }

    @Test
    public void testDelayFlight_ArrivalBeforeCurrentTime() {
        String pastTime = LocalDateTime.now().minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String result = flight.delayFlight("2030-01-01 10:00:00", pastTime, "Maintenance");
        assertEquals("Departure time cannot be later than arrival time.", result);
    }

    @Test
    public void testDelayFlight_DepartureEqualsArrival() {
        String result = flight.delayFlight("2030-01-01 10:00:00", "2030-01-01 10:00:00", "Maintenance");
        assertEquals("New departure time cannot be equal to new arrival time.", result);
    }

    @Test
    public void testDelayFlight_SameAsCurrentTimes() {
        String result = flight.delayFlight("2030-01-01 10:00:00", "2030-01-01 14:00:00", "Maintenance");
        assertEquals("New departure and arrival times cannot be the same as the current times.", result);
    }

    @Test
    public void testDelayFlight_Success() {
        try (MockedStatic<FlightsModel> fm = Mockito.mockStatic(FlightsModel.class)) {
            fm.when(() -> FlightsModel.delayFlight("FL123", "Maintenance", "2030-01-01 12:00:00", "2030-01-01 16:00:00")).thenAnswer(invocation -> null);

            String result = flight.delayFlight("2030-01-01 12:00:00", "2030-01-01 16:00:00", "Maintenance");
            assertEquals("success", result);
            assertEquals("2030-01-01 12:00:00", flight.getDepartureTime());
            assertEquals("2030-01-01 16:00:00", flight.getArrivalTime());

            fm.verify(() -> FlightsModel.delayFlight("FL123", "Maintenance", "2030-01-01 12:00:00", "2030-01-01 16:00:00"), Mockito.times(1));
        }
    }

    @Test
    public void testDisplayFlightDetails(){
        flight = new Flight();
        flight.displayFlightDetails();
    }

    // FOR WHITE BOX TESTING PURPOSE
    @Test
    public void white_box_testing_1(){
        flight = new Flight();
        assertNotNull(flight);
    }

    @Test
    public void testingTimeFormat(){

    String validDeparture = "2025-04-22 14:00:00"; // correct format
    String invalidArrival = "2025/04/22 18:00";   // wrong format

    String result = flight.delayFlight(validDeparture, invalidArrival, "Test");
    assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", result);

    String invalidDeparture = "2025/04/22 14:00"; // wrong format
    String validArrival = "2025-04-22 18:00:00";   // correct format

    result = flight.delayFlight(invalidDeparture, validArrival, "Test");
    assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", result);

    invalidDeparture = "2025/04/22 14:00"; // wrong format
    invalidArrival = "2025/04/22 18:00";   // wrong format
    
    result = flight.delayFlight(invalidDeparture, invalidArrival, "Test");
    assertEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", result);

    validDeparture = "2025-04-22 14:00:00"; // correct format
    validArrival = "2025-04-22 18:00:00";   // correct format
        
    result = flight.delayFlight(validDeparture, validArrival, "Test");
    assertNotEquals("Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.", result);
    }

    @Test
    public void testingDelayTimes(){
        String result = flight.delayFlight("2030-01-01 10:00:00", "2030-01-01 14:00:00", "Test"); // same departure and arrival times
        assertEquals("New departure and arrival times cannot be the same as the current times.", result);

        result = flight.delayFlight("2030-01-01 10:00:00", "2030-01-01 17:00:00", "Test"); // same departure time but diferent arrival time
        assertEquals("New departure and arrival times cannot be the same as the current times.", result);

        result = flight.delayFlight("2030-01-01 12:00:00", "2030-01-01 14:00:00", "Test"); // different departure time but same arrival time
        assertEquals("New departure and arrival times cannot be the same as the current times.", result);
    }

}
