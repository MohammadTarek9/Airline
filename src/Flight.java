

import java.time.LocalDateTime;
import java.util.ArrayList;
public class Flight {
    private String flightNumber;
    private int capacity;
    private int bookedSeats;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double baseFare;
    private ArrayList<Seat> seats;

    public Flight(String flightNumber, int capacity, int bookedSeats, String source, String destination, String departureTime, String arrivalTime ,double fare) {
        this.flightNumber = flightNumber;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.baseFare = fare;
        this.seats = new ArrayList<>();
    }

    public Flight(String flightNumber, int capacity, String source, String destination, String departureTime, String arrivalTime, double fare) {
        this(flightNumber, capacity, 0, source, destination, departureTime, arrivalTime, fare);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity >= bookedSeats) {
            this.capacity = capacity;
        } else {
            System.out.println("Error: Capacity cannot be less than booked seats.");
        }
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        if (bookedSeats <= capacity) {
            this.bookedSeats = bookedSeats;
        } else {
            System.out.println("Error: Booked seats cannot be more than capacity.");
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setFare(double fare) {
        this.baseFare = fare;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    public boolean Delay(String newDepartureTime, String newArrivalTime) {
        if (LocalDateTime.parse(newDepartureTime).isAfter(LocalDateTime.now()) && LocalDateTime.parse(newArrivalTime).isAfter(LocalDateTime.parse(newDepartureTime))) {
            this.departureTime = newDepartureTime;
            this.arrivalTime = newArrivalTime;
            return true;
        }
        return false;
    }

    public void bookSeat() {
        if (bookedSeats < capacity) {
            bookedSeats++;
        }
    }

    public void cancelBooking() {
        if (bookedSeats > 0) {
            bookedSeats--;
        }
    }

    public double calculateFare(String seatType) {
        double finalFare = baseFare;

        switch (seatType.toLowerCase()) {
            case "business":
                finalFare *= 1.5; 
                break;
            case "first":
                finalFare *= 2.0; 
                break;
            case "economy":
            default:
                finalFare *= 1.0; 
                break;
        }
        return finalFare;
    }

    public static ArrayList<Flight> getAllFlights() {
        ArrayList<Flight> flights = new ArrayList<>();
        flights = FlightsModel.getAllFlights();
        return flights;
    }

    public String handleDelays() {
        boolean isDelayed = FlightsModel.checkIfDelayed(this.flightNumber);
        if(isDelayed) {
            String delayReason = FlightsModel.getDelayReason(this.flightNumber);
            return delayReason;
        }
        return "nothing";
    }

    public boolean isCancelled() {
        Flight cancelledFlight = FlightsModel.getFlightDetails(this.flightNumber);
        return cancelledFlight == null;
    }

    public void displayFlightDetails() {
        System.out.println("Flight Number: " + flightNumber);
        System.out.println("Capacity: " + capacity);
        System.out.println("Booked Seats: " + bookedSeats);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Time: " + departureTime);
        System.out.println("Arrival Time: " + arrivalTime);
        System.out.println("baseFare: " + baseFare);
    }

}
