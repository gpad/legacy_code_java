import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TimedLockerTest {
    @Test
    void test() throws InterruptedException {
        TimedLocker l = new TimedLocker("gino");
        l.close();
        l.addDigit('g').addDigit('i').addDigit('n').addDigit('o');
        l.addDigit('x').addDigit('x').addDigit('x');
        l.close();
        Thread.sleep(3500);
        l.close();
        fail();
    }
}
