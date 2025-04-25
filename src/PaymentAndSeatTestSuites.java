import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    PaymentTest.class,
    SeatTest.class,
    SeatModelTest.class
})

public class PaymentAndSeatTestSuites {
    
}
