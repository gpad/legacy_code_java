import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TablesTest{
	@Test
	public void singleton(){
		assertNotEquals(Tables.getInstance(), null);
	}
}