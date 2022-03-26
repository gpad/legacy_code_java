import org.junit.jupiter.api.Test;

public class LockerTest {

    @Test
    public void instantiate() {
        HttpClient c = new HttpClient();
        Locker l = new Locker("gino", c);
    }

}

