import java.util.ArrayList;

public class Session {
    private static Passenger passenger;
    private static ArrayList<Booking> bookings = new ArrayList<>();
    private static Flight flight;


    public static void setPassenger(Passenger passenger) {
        Session.passenger = passenger;
    }

    public static Passenger getPassenger() {
        return Session.passenger;
    }

    public static ArrayList<Booking> getBookings() {
        return Session.bookings;
    }

    public static void setBookings(ArrayList<Booking> bookings) {
        Session.bookings = bookings;
    }

    public static Flight getFlight() {
        return flight;
    }

    public static void setFlight(Flight flight) {
        Session.flight = flight;
    }


    
}
