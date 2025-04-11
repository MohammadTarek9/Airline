import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    public void testValidVisaCardWithValidCCV() {
        assertTrue(Payment.processPayment("4123456789012345", 123));
    }

    @Test
    public void testValidMasterCardWithValidCCV() {
        assertTrue(Payment.processPayment("5123456789012345", 4567));
    }

    @Test
    public void testInvalidCardNumber_TooShort() {
        assertFalse(Payment.processPayment("41234567", 123));
    }

    @Test
    public void testInvalidCardNumber_InvalidPrefix() {
        assertFalse(Payment.processPayment("6123456789012345", 123));
    }

    @Test
    public void testInvalidCCV_TooShort() {
        assertFalse(Payment.processPayment("4123456789012345", 12));
    }

    @Test
    public void testInvalidCCV_TooLong() {
        assertFalse(Payment.processPayment("5123456789012345", 12345));
    }

    @Test
    public void testInvalidCardNumberAndCCV() {
        assertFalse(Payment.processPayment("3123456", 1));
    }
}
