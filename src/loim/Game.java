package loim;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
/**
 * A j�t�kot reprezent�l� oszt�ly.
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
	 * Inicializ�lja a k�rd�sek adatb�zis�t.
	 */
	public Game(){
		try {
			data = new QuestionData("questions.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inicializ�lja a j�t�kot.
	 * Akkor h�v�dik meg, ha a J�t�kos �j j�t�kot ind�t.
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
	 * Meg�l�tja a j�t�kot.
	 * Akkor h�v�dik meg, ha a J�t�kos nyert, feladta vagy vesz�tett.
	 */
	public void stop(){
		running = false;
	}
	/**
	 * El�k�sz�ti a quiz k�vetkez� k�rd�s�t.
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
	 * Megform�zza a k�rd�st, hogy belef�rjen a k�rd�s mez�be.
	 * Bizonyos id�k�z�nk�nt besz�r egy sort�r�st.
	 * 
	 * @param rawQuestion	a form�land� k�rd�s
	 * @return a			a megform�lt k�rd�s
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
