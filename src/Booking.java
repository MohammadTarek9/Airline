//package org.example;
public class Booking {
    private String bookingID;
    private Flight flight;
    private Passenger passenger;
    private Boolean isConfirmed;
    private String seatType;

    public Booking(String bookingID, Flight flight, Passenger passenger, String seatType) {
        this.bookingID = bookingID;
        this.flight = flight;
        this.passenger = passenger;
        this.isConfirmed = false;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean confirmBooking() {
        Payment payment = new Payment(this, flight.calculateFare(seatType));
        if (payment.processPayment() && flight.bookSeat()) {
            this.isConfirmed = true;
            return true;
        }
        return false;
    }

    public boolean cancelBooking() {
        if (isConfirmed && flight.cancelBooking()) {
            this.isConfirmed = false;
            return true;
        }
        return false;
    }
}
