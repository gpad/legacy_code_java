
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LockerTest {

	@Test
	public void instantiate() {
		HttpClient c = new HttpClient();
		Locker l = new Locker("gino", c);
	}

}

