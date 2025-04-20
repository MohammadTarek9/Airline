import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
public class Admin {
    private String email;
    private String password;
    private String name;

    public Admin(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return "Email and password cannot be empty!";
        }
        if(!AdminModel.isAdmin(email)){
            return "You do not have an account!";
        }
        if (AdminModel.getAdminDetails(email) == null) {
            return "Invalid email or password!";
        } else {
            Admin admin = AdminModel.getAdminDetails(email);
            if (admin.getPassword().equals(password)) {
                return "success";
            } else {
                return "Incorrect password!";
            }
        }
    }

    public String addFlight(Flight flight){
        if (flight.getFlightNumber().isEmpty() || flight.getSource().isEmpty() || flight.getDestination().isEmpty() || flight.getDepartureTime().isEmpty() || flight.getArrivalTime().isEmpty() || flight.getBaseFare() == 0 || flight.getCapacity() == 0) {
            return "Please fill in all fields correctly.";
        } else {
            if (!flight.getDepartureTime().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") || !flight.getArrivalTime().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                return "Please enter the date and time in the format yyyy-mm-dd hh:mm:ss.";
            }

            LocalDateTime departureDateTime = LocalDateTime.parse(flight.getDepartureTime().replace(" ", "T"));
            LocalDateTime arrivalDateTime = LocalDateTime.parse(flight.getArrivalTime().replace(" ", "T"));
            if (departureDateTime.isAfter(arrivalDateTime)) {
                return "Departure time cannot be later than arrival time.";
            }
            if (flight.getBaseFare() < 0) {
                return "Base fare cannot be negative.";
            }
            if (flight.getCapacity() < 0) {
                return "Capacity cannot be negative.";
            }
            else {
                String result = FlightsModel.addFlight(flight);
                if (result.equals("success")) {
                    return "success";
                } else {
                    return "Error adding flight" ;
                }
            }
        }
    }


    
}
