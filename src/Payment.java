//package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class Payment {
    private String paymentId;
    private Passenger passenger;
    private Flight flight;
    private Booking booking;
    private double amount;
    private String paymentMethod;
    private String cardNumber;
    private int cCV;
    private LocalDateTime paymentDate;
    private boolean paymentStatus;
    private static List<Payment> payments = new ArrayList<Payment>();

    public Payment(Booking booking, double amount) {
        this.paymentId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.passenger = booking.getPassenger();
        this.flight = booking.getFlight();
        this.booking = booking;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.paymentStatus = false; //Initially set to false until processed.
    }

    public String getPaymentId() {return paymentId;}

    public Passenger getPassenger() {return passenger;}

    public Flight getFlight() {return flight;}

    public double getAmount() {return amount;}

    public String getPaymentMethod() {return paymentMethod;}

    public String getCardNumber() {return cardNumber;}

    public LocalDateTime getPaymentDate() {return paymentDate;}

    public boolean isPaymentSuccessful() {return paymentStatus;}

    public int getCCV() {return cCV;}

    public void setAmount(double amount) {this.amount = amount;}

    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}

    public void setcCV(int cCV) {this.cCV = cCV;}

    public static List<Payment> getPayments() {return new ArrayList<>(payments);}

    private boolean validateCreditCard(){
        if (cardNumber == null || cardNumber.length() < 16){
            return false;
        }
        char firstDigit = cardNumber.charAt(0);
        return firstDigit == '5'           // Mastercard
                || firstDigit == '4';      // Visa
    }

    public boolean processPayment() {
        // if (flight == null || amount !=flight.calculateFare(booking.getSeatType())) {
        //     System.out.println("Payment failed: Amount does not match flight fare.");
        //     return false;
        // }

        if (paymentMethod == null || (!paymentMethod.equalsIgnoreCase("Credit Card") && !paymentMethod.equalsIgnoreCase("Debit Card"))) {
            System.out.println("Payment failed: Invalid payment method.");
            return false;
        }
        if (amount > 0 && validateCreditCard()) {
            this.paymentStatus = true;
            payments.add(this);
            System.out.println("Payment successful for passenger " + passenger.getFirstName() + " " + passenger.getLastName() + " on flight " + flight.getFlightNumber());
        } else {
            System.out.println("Payment failed for passenger " + passenger.getFirstName() + " " + passenger.getLastName() + " on flight " + flight.getFlightNumber());
        }
        return paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", passenger='" + passenger.getFirstName() + " " + passenger.getLastName() + '\'' +
                ", flight='" + flight.getFlightNumber() + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", cardNumber='**** **** **** " + (cardNumber.length() == 16 ? cardNumber.substring(cardNumber.length() - 4) : "****") + '\'' +
                ", cCV=" + "***" +
                ", paymentDate=" + paymentDate +
                ", paymentStatus=" + (paymentStatus ? "Successful" : "failed") +
                '}';
    }
}
