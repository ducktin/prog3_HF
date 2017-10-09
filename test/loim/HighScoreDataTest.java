package loim;

import org.junit.Assert;
import org.junit.Test;

public class HighScoreDataTest {

	@Test
	public void testHighScoreData() {
		HighScoreData hsd = new HighScoreData();
		Assert.assertEquals(0, hsd.getHighScores().size());
	}
	
	@Test
	public void testAdd() {
		HighScoreData hsd = new HighScoreData();
		HighScore hs = new HighScore("Jani", "1.000");
		hsd.add("Jani", "1.000");
		Assert.assertTrue(hsd.getHighScores().contains(hs));
	}
	@Test
	public void testHighScoreToString() {
		HighScore hs = new HighScore("Jani", "1.000");
		Assert.assertTrue("Jani - 1.000".equals(hs.toString()));
	}

}
