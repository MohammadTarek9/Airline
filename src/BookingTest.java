import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
public class BookingTest {

    private Flight mockFlight;
    private Passenger mockPassenger;
    private Seat mockSeat;
    private Booking booking;

    
    @BeforeEach
    public void setUp(){
        mockFlight = new Flight("FL123", 10, "A", "B", "2030-01-01T10:00:00", "2030-01-01T12:00:00" , 100.0); // flight (dummy constructor)
        mockPassenger = new Passenger(); //mock (dummy constructor)
        mockSeat = new Seat("1A",true,"Economy"); //mock (dummy constructor)
        booking = new Booking(1, mockFlight, mockPassenger, mockSeat);
    }

    @Test
    public void testConstructor_DefaultFalse() {
        assertFalse(booking.getIsConfirmed(), "Booking should be unconfirmed by default.");
    }

    @Test
    public void testConstructor_IsConfirmed(){
        Booking confirmedBooking = new Booking(2, mockFlight, mockPassenger, mockSeat, true);
        assertTrue(confirmedBooking.getIsConfirmed(),"Booking should be confirmed as passed.");
    }

    @Test
    public void testGettersAndSetters() {
        booking.setBookingID(42);
        assertEquals(42, booking.getBookingID());

        Flight newFlight = new Flight("XX",5,"C","D","2030-02-01T10:00:00", "2030-02-01T12:00:00", 50.0);
        booking.setFlight(newFlight);
        assertEquals(newFlight, booking.getFlight());

        Passenger newPassenger = new Passenger();
        booking.setPassenger(newPassenger);
        assertEquals(newPassenger, booking.getPassenger());

        Seat newSeat = new Seat("2B",true,"Business");
        booking.setSeat(newSeat);
        assertEquals(newSeat, booking.getSeat());

        booking.setIsConfirmed(true);
        assertTrue(booking.getIsConfirmed());

    }

    @Test
    public void testConfirmBooking_PastDate(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        boolean result = booking.confirmBooking("5593208126249566", 123, yesterday);
        assertFalse(result,"Should reject bookings with a past date");
        assertFalse(booking.getIsConfirmed());
    }

    @Test
    public void testConfirmBooking_SuccessfulPayment() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        try(
            MockedStatic<Payment> paymentMock = Mockito.mockStatic(Payment.class);
            MockedStatic<BookingsModel> bookingModelMock = Mockito.mockStatic(BookingsModel.class);
        ){
            paymentMock.when(() -> Payment.processPayment("5592208149249866", 123)).thenReturn(true);
            bookingModelMock.when(() -> BookingsModel.updateBookingStatus(1, true)).thenReturn(true);
            boolean result = booking.confirmBooking("5592208149249866", 123,tomorrow);

            assertTrue(result, "Booking should be confirmed on successful Payment");
            assertTrue(booking.getIsConfirmed(), "isConfirmed should be true after Payment success");

            paymentMock.verify(() -> Payment.processPayment("5592208149249866", 123), Mockito.times(1));
            bookingModelMock.verify(() -> BookingsModel.updateBookingStatus(1, true), Mockito.times(1));

        }

    }

    @Test
    public void testConfirmBooking_FailedPayment() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        try(
            MockedStatic<Payment> paymentMock = Mockito.mockStatic(Payment.class);
            MockedStatic<BookingsModel> bookingModelMock = Mockito.mockStatic(BookingsModel.class);
        ){
            paymentMock.when(() -> Payment.processPayment("0000000000000000", 432)).thenReturn(false);
            boolean result = booking.confirmBooking("0000000000000000", 432,tomorrow);

            assertFalse(result, "Booking should return false on failed Payment");
            assertFalse(booking.getIsConfirmed(), "isConfirmed should remain false after failure");

            paymentMock.verify(() -> Payment.processPayment("0000000000000000", 432), Mockito.times(1));

        }

    }
    // WHITE BOX TESTING
    @Test
    public void white_box_testing_1(){
        new Booking();
    }
}
