

public class Seat {
    private boolean available;
    private String seat_id;
    private String seatType;

    public Seat(String seat_id) {
        this.seat_id = seat_id;
        this.available = true;
    }

    public Seat(String seat_id, boolean available, String seatType) {
        this.seat_id = seat_id;
        this.available = available;
        this.seatType = seatType;
    }

    public Seat(){
        
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }


}
