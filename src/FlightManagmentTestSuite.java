import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    FlightTest.class,
    FlightModelTest.class
})

public class FlightManagmentTestSuite {
    
}
