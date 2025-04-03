//package org.example;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Passenger {
    private String firstName;
    private String lastName;
    private String phone;
    private ArrayList <Booking> bookings;
    private String email;
    private String password;
    private int age;
    private ArrayList <Payment> payments;

    public Passenger(String firstName, String lastName, String phone, String email, String password, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.age = age;
        this.bookings = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean Pay(Payment payment){
        boolean success = payment.processPayment();
        if (success) {
            payments.add(payment);
        }
        return success;
    }

    public ArrayList <Booking> getBookings(){
        return bookings;
    }

    public boolean BookFlight(String bookingID, Flight flight, String seatType) {
        Booking booking = new Booking(bookingID, flight, this, seatType);
        if (booking.confirmBooking()) {
            bookings.add(booking);
            return true;
        }
        return false;
    }

    public boolean cancelBooking(Booking booking) {
        if (bookings.contains(booking) && booking.cancelBooking()) {
            bookings.remove(booking);
            return true;
        }
        return false;
    }

    public void createAccount(String email, String password, String firstName, String lastName, String phone, String ageStr) {
        if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        if (!ageStr.matches("\\d+")) {
            throw new IllegalArgumentException("Age must be a number!");
        }
        int age = Integer.parseInt(ageStr);

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        
        String phoneRegex = "^\\+\\d{1,3}\\d{9,13}$"; 
        if (!Pattern.matches(phoneRegex, phone)) {
            throw new IllegalArgumentException("Invalid phone number format!");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long!");
        }

        UserModel.storeUserData(email, password, firstName, lastName, phone, age);
        
    }

    public void login(){
        // login
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
