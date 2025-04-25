import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    PassengerTest.class,
    UserModelTest.class
})

public class PassengerTestSuite {
    
}
