public class Session {
    private static Passenger passenger;
    private static Flight flight;
    public static void setPassenger(Passenger passenger) {
        Session.passenger = passenger;
    }

    public static Passenger getPassenger() {
        return Session.passenger;
    }

    public static void setFlight(Flight flight) {
        Session.flight = flight;
    }

    public static Flight getFlight() {
        return Session.flight;
    }

    
}
