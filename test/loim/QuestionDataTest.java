package loim;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuestionDataTest {
	
	QuestionData qd;
	
	@Before
	public void setUp() throws FileNotFoundException {
		qd = new QuestionData("questions.json"); 
	}
	@Test(expected = FileNotFoundException.class)
	public void testNoQuestionFile() throws FileNotFoundException{
		new QuestionData("q.json");
	}
	
	@Test
	public void testGetQuestionByDifficulty() {
		Question q = qd.getQuestionByDifficulty(1);
		Assert.assertEquals(1, q.getDifficulty());
	}

}
