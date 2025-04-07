//package org.example;

import java.util.ArrayList;

public class Booking {
    private int bookingID;
    private Flight flight;
    private Passenger passenger;
    private Boolean isConfirmed;
    private Seat seat;

    public Booking(int bookingID, Flight flight, Passenger passenger, Seat seat) {
        this.bookingID = bookingID;
        this.flight = flight;
        this.passenger = passenger;
        this.seat = seat;
        this.isConfirmed = false;
    }

    public Booking(int bookingID, Flight flight, Passenger passenger, Seat seat, Boolean isConfirmed) {
        this.bookingID = bookingID;
        this.flight = flight;
        this.passenger = passenger;
        this.isConfirmed = isConfirmed;
        this.seat = seat;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
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

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public boolean confirmBooking(String cardNumber, int ccv) {
        boolean paymentSuccess = Payment.processPayment(cardNumber, ccv);
        if (paymentSuccess) {
            this.isConfirmed = true;
            BookingsModel.updateBookingStatus(this.bookingID, true);
            return true;
        } else {
            System.out.println("Payment failed. Please try again.");
            return false;
        }
    }
        


    
}
