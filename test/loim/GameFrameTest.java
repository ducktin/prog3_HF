package loim;

import org.junit.Assert;
import org.junit.Test;

public class GameFrameTest {

	@Test
	public void testGameFrame() {
		Game g = new Game();
		GameFrame gf = new GameFrame(g);
		Assert.assertEquals(g, gf.getG());
	}

}
