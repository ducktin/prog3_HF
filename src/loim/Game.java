package loim;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
/**
 * A játékot reprezentáló osztály.
 * 
 * @author Martin Borbola
 */
public class Game {
	private QuestionData data;
	
	private boolean hasSplit, hasCrowd, hasPhone;
	private int currentQuestionNumber = 1;
	private boolean running = false;
	private Question currentQuestion;
	
	/**
	 * Inicializálja a kérdések adatbázisát.
	 */
	public Game(){
		try {
			data = new QuestionData("questions.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inicializálja a játékot.
	 * Akkor hívódik meg, ha a Játékos új játékot indít.
	 */
	public void start(){
		running = true;
		hasSplit = true;
		hasCrowd = true;
		hasPhone = true;
		currentQuestionNumber = 1;
		currentQuestion = data.getQuestionByDifficulty(currentQuestionNumber);
	}
	/**
	 * Megálítja a játékot.
	 * Akkor hívódik meg, ha a Játékos nyert, feladta vagy veszített.
	 */
	public void stop(){
		running = false;
	}
	/**
	 * Elõkészíti a quiz következõ kérdését.
	 */
	public void nextQuestion(){
		currentQuestionNumber++;
		currentQuestion = data.getQuestionByDifficulty(currentQuestionNumber);
	}
	public int getCurrentQuestionNumber(){
		return currentQuestionNumber;
	}
	public String getCurrentQuestion(){
		return formatQuestion(currentQuestion.getQuestion());
	}
	public String[] getChoices(){
		return currentQuestion.getChoices();
	}
	public int getCorrectAnswer(){
		return currentQuestion.getCorrectAnwserIdx();
	}
	public boolean isRunning(){
		return running;
	}
	public boolean hasSplit(){
		return hasSplit;
	}
	public void useSplit(){
		hasSplit = false;
	}
	public boolean hasCrowd(){
		return hasCrowd;
	}
	public void useCrowd(){
		hasCrowd = false;
	}
	public boolean hasPhone(){
		return hasPhone;
	}
	public void usePhone(){
		hasPhone = false;
	}
	/**
	 * Megformázza a kérdést, hogy beleférjen a kérdés mezõbe.
	 * Bizonyos idõközönként beszúr egy sortörést.
	 * 
	 * @param rawQuestion	a formálandó kérdés
	 * @return a			a megformált kérdés
	 */
	private String formatQuestion(String rawQuestion) {
	    StringTokenizer tokenizer = new StringTokenizer(rawQuestion);
	    int charCount = 0;
	    String question = "";
	    String token;
	    while (tokenizer.hasMoreTokens()) {
	      token = tokenizer.nextToken();
	      if (token.length() + charCount > 40) {
	        question = question + "\r\n" + token + " ";
		charCount = token.length() + 1;
	      }
	      else if (token.length() + charCount == 40) {
	        question = question + token + "\r\n";
		charCount = 0;
	      }
	      else {
	        question = question + token + " ";
		charCount = charCount + token.length() + 1;
	      }
	    }
	    return question;
	  }

}
