import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatTest {

    @Test
    public void noArgCons_Default(){
        Seat seat = new Seat();
        assertNull(seat.getSeat_id(), "Default seat_id should be null");
        assertFalse(seat.isAvailable(),"Default avaliability should be false");
        assertNull(seat.getSeatType(),"Default seatType should be null");
    }
    @Test
    public void oneArgCons_IdAndAvaliability(){
        Seat seat = new Seat("1A");
        assertEquals("1A", seat.getSeat_id(),"seat_id should be set from constructor");
        assertTrue(seat.isAvailable(),"setting seat_id should default set avaliable to true");
        assertNull(seat.getSeatType(),"seatType should still be null");
    }
    @Test
    public void allArgCons_testall(){
        Seat seat = new Seat("2B", false, "Economy");
        assertEquals("2B",seat.getSeat_id());
        assertFalse(seat.isAvailable());
        assertEquals("Economy", seat.getSeatType());
    }
    @Test
    public void testGettersAndSetters(){
        Seat seat = new Seat();
        seat.setSeat_id("3C");
        assertEquals("3C", seat.getSeat_id());
        seat.setAvailable(true);
        assertTrue(seat.isAvailable());
        seat.setSeatType("Business");
        assertEquals("Business", seat.getSeatType());
        seat.setAvailable(false);
        assertFalse(seat.isAvailable());
    }
}
