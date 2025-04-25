import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BookingTest.class,
    BookingsModelTest.class
})

public class BookingTestSuite {
    
}
