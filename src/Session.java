

public class Session {

    private static Passenger passenger;
    private static Booking booking;
    private static Flight flight;


    public static void setPassenger(Passenger passenger) {
        Session.passenger = passenger;
    }

    public static Passenger getPassenger() {
        return Session.passenger;
    }

    public static Booking getBookings() {
        return Session.booking;
    }

    public static void setBookings(Booking booking) {
        Session.booking = booking;
    }

    public static Flight getFlight() {
        return flight;
    }

    public static void setFlight(Flight flight) {
        Session.flight = flight;
    }


    
}
