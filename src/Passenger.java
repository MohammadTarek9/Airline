//package org.example;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;

public class Passenger {
    private String firstName;
    private String lastName;
    private String phone;
    private ArrayList <Booking> bookings;
    private String email;
    private String password;
    private int age;

    public Passenger(String firstName, String lastName, String phone, String email, String password, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.age = age;
        this.bookings = new ArrayList<>();
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

    public ArrayList <Booking> getBookings(){
        return bookings;
    }

    public void setBookings(ArrayList <Booking> bookings){
        this.bookings = bookings;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    
    public ArrayList<Flight> searchFlights(String source, String destination) {
        ArrayList<Flight> availableFlights = new ArrayList<>();
        for (Flight flight : Flight.getAllFlights()) {
            if (flight.getSource().equalsIgnoreCase(source) && flight.getDestination().equalsIgnoreCase(destination)) {
                availableFlights.add(flight);
            }
        }
        return availableFlights;
    }

    public ArrayList<Flight> filterFlights(ArrayList<Flight> flights, String date) {
        ArrayList<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getDepartureTime().contains(date)) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    public ArrayList<Booking> fetchBookings() {
        ArrayList<Booking> allBookings = new ArrayList<>();
        allBookings = BookingsModel.getAllBookings(this.email);
        return allBookings;
    }

    public boolean BookFlight(String seat_id) {
        Flight flight = Session.getFlight();
        System.out.println("Flight Number: " + flight.getFlightNumber()); 
        Seat seat = SeatModel.getSeatDetails(seat_id, flight.getFlightNumber());
        System.out.println("Seat ID: " + seat.getSeat_id());
        System.out.println("Seat Type: " + seat.getSeatType());
        int bookingID = BookingsModel.addBooking(flight.getFlightNumber(), Session.getPassenger(), seat_id);
        if(bookingID == -1) {
            return false;
        }
        boolean r1 = SeatModel.updateSeatAvailability(flight.getFlightNumber(), seat_id, false);
        FlightsModel.incrementBookedSeats(flight.getFlightNumber());
        flight.bookSeat();
        flight.UpdateAvailableSeats(seat);
        if(!r1) {
            return false;
        }
        Booking booking = new Booking(bookingID, flight, Session.getPassenger(), seat);
        System.out.println("Seat id tany " + booking.getSeat().getSeat_id());
        booking.setIsConfirmed(false);
        this.bookings.add(booking);
        return true;
    }

    public String cancelBooking(ArrayList<Booking> passengerBookings, int bookingID) {
        String result = "success";
        Seat seat = BookingsModel.getBookingSeat(bookingID);
        Flight flight = BookingsModel.getBookingFlight(bookingID);
        String flightNumber = flight.getFlightNumber();
        boolean r1 = SeatModel.updateSeatAvailability(flightNumber, seat.getSeat_id(), true);
        boolean r3 = FlightsModel.decrementBookedSeats(flightNumber);
        if(!r1 || !r3){
            result = "Error updating seat availability!";
            return result;
        }
        boolean r2 = BookingsModel.deleteBooking(bookingID);
        flight.cancelBooking();
        if(!r2){
            result = "Error deleting booking!";
            return result;
        }
        return result;
    }

    public static String createAccount(String email, String password, String firstName, String lastName, String phone, String ageStr) {
        String result = "success";
        if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            result = "All fields are required!";
            return result;
        }

        if (!ageStr.matches("\\d+")) {
            result = "Age must be a number!";
            return result;
        }
        int age = Integer.parseInt(ageStr);

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            result = "Invalid email format!";
            return result;
        }

        if(UserModel.isEmailExists(email)) {
            result = "Email already exists!";
            return result;
        }
        
        String phoneRegex = "^\\+\\d{1,3}\\d{9,13}$"; 
        if (!Pattern.matches(phoneRegex, phone)) {
            result = "Invalid phone number format!";
            return result;
        }

        if (password.length() < 6) {
            result = "Password must be at least 6 characters long!";
            return result;
        }

        UserModel.storeUserData(email, password, firstName, lastName, phone, age);
        return result;
        
    }

    public void refreshBookings() {
        this.bookings = BookingsModel.getAllBookings(this.email);
    }

    public static String login(String email, String password) {
        String result = "success";
        if (email.isEmpty() || password.isEmpty()) {
            result = "Email and password cannot be empty!";
            return result;
        } else if (!UserModel.isEmailExists(email)) {
            result = "You do not have an account!";
            return result;
        }
        String dbPassword = UserModel.getPassword(email);
        if (dbPassword == null) {
            result = "Error retrieving password!";
            return result;
        } else if (!dbPassword.equals(password)) {
            result = "Incorrect password!";
            return result;
        }

        
        return result;
    }
    
    public String updateAccount(String oldEmail, String newEmail, String newPassword, String confirmPassword, String newPhoneNumber, String newAge) {
        String result = "success";
        boolean emailChanged = false;
        if(!newEmail.isEmpty()){
            if(newEmail.equals(oldEmail)){
                result = "New email cannot be the same as the old email!";
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!Pattern.matches(emailRegex, newEmail)) {
                result = "Invalid email format!";
            }
            if(UserModel.isEmailExists(newEmail)) {
                result = "Email already exists!";
            }
            String emailChangeResult = UserModel.changeEmail(oldEmail, newEmail);

            if(!emailChangeResult.equals("success")){
                result = "Failed to update email!";
            }
            else{
                emailChanged = true;
                this.setEmail(newEmail);
            }
        }

        if(!newPassword.isEmpty()){
            if(newPassword.equals(this.getPassword())){
                result = "New password cannot be the same as the old password!";
            }
            if(newPassword.length() < 6){
                result = "Password must be at least 6 characters long!";
            }
            if(newPassword.equals(confirmPassword)){
                String email = emailChanged ? newEmail : oldEmail;
                String passwordChangeResult = UserModel.changePassword(email, newPassword);
                if(!passwordChangeResult.equals("success")){
                    result = "Failed to update password!";
                }
                else{
                    this.setPassword(newPassword);
                }
            } else {
                result = "Passwords do not match!";
            }
        }
        if(!newPhoneNumber.isEmpty()){
            if(newPhoneNumber.equals(this.getPhone())){
                result = "New phone number cannot be the same as the old phone number!";
            }
            String phoneRegex = "^\\+\\d{1,3}\\d{9,13}$"; 
            if (!Pattern.matches(phoneRegex, newPhoneNumber)) {
                result = "Invalid phone number format!";
            }
            String email = emailChanged ? newEmail : oldEmail;
            String phoneChangeResult = UserModel.changePhone(email, newPhoneNumber);
            if(!phoneChangeResult.equals("success")){
                result = "Failed to update phone number!";
            }
            else{
                this.setPhone(newPhoneNumber);
            }
        }
        if(!newAge.isEmpty() ){
            if(newAge.equals(String.valueOf(this.getAge()))){
                result = "New age cannot be the same as the old age!";
            }
            if(newAge.equals(String.valueOf(this.getAge()))){
                result = "New age cannot be the same as the old age!";
            }
            if (!newAge.matches("\\d+")) {
                result = "Age must be a number!";
            }
            int age = Integer.parseInt(newAge);
            String email = emailChanged ? newEmail : oldEmail;
            String ageChangeResult = UserModel.changeAge(email, age);
            if(!ageChangeResult.equals("success")){
                result = "Failed to update age!";
            }
            else{
                this.setAge(age);
            }
        }
        return result;
    }
}
