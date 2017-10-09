package loim;

import java.io.*;
import java.util.*;
import javax.json.*;

/**
 * A kérdésadatbázist megvalósító osztály.
 * 
 * @author Martin Borbola
 */
public class QuestionData {

	ArrayList<Question> questions = new ArrayList<>();

	/**
	 * Létrehozza az kérdésadatbázist a megfelelõ fájlból. <br>
	 * Egy kérdés felépítése:<br>
	 * <code>
	 * {<br>
	 * 	"question": "Kérdés",<br>
	 * 	"choices": ["VálaszlehetõségA","VálaszlehetõségB","VálaszlehetõségC","VálaszlehetõségD"],<br>
	 * 	"difficulty": nehézség,<br>
	 * 	"correct": jóválasz,<br>
	 * 	"category": "KATEGÓRIA"<br>
	 * }
	 * </code>
	 * 
	 * @param jsonfile az adatbázist tároló json file
	 * @throws FileNotFoundException Ha nem található a "questions.json" fájl
	 */
	public QuestionData(String jsonfile) throws FileNotFoundException{
		FileReader fr = new FileReader(jsonfile);
		JsonReader reader = Json.createReader(fr);
		JsonObject obj = reader.readObject();

		JsonArray jsonQuestions = obj.getJsonArray("questions");
		for (int i = 0; i < jsonQuestions.size(); i++) {
			JsonObject o = jsonQuestions.getJsonObject(i);

			String question = o.getString("question");
			JsonArray jsonChoices = o.getJsonArray("choices");
			String[] choices = new String[4];
			for (int j = 0; j < jsonChoices.size(); j++) {
				choices[j] = jsonChoices.getString(j);
			}
			int difficulty = o.getInt("difficulty");
			int correct = o.getInt("correct");
			String category = o.getString("category");

			questions.add(new Question(question, choices, difficulty, correct, category));
		}

		reader.close();
	}
	/**
	 * Lekér egy adott nehézségû, véletlenszerû kérdést az adatbázisból.<br>
	 * Az algoritmus elõször kiválogatja az adott nehézségû kérdéseket, majd ezekbõl választ egyet.
	 * 
	 * @param difficulty	a kért kérdés nehézsége
	 * @return				a visszaadott kérdés
	 */
	public Question getQuestionByDifficulty(int difficulty) {
		List<Question> rightQuestions = new ArrayList<>();
		for (Question q : questions)
			if (q.getDifficulty() == difficulty)
				rightQuestions.add(q);
		return rightQuestions.get(new Random().nextInt(rightQuestions.size()));
	}
}

/**
 * Egy kérdést reprezentáló osztály.
 * Tárolja az egy kérdésre vonatkozó információkat.
 * 
 * @author Martin Borbola
 */
class Question {
	public String getQuestion() {
		return question;
	}

	public String[] getChoices() {
		return choices;
	}

	public int getCorrectAnwserIdx() {
		return correctIdx;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public String getCategory() {
		return category;
	}

	public Question(String question, String[] choices, int difficulty, int correctIdx, String category) {
		this.question = question;
		this.choices = choices.clone();
		this.difficulty = difficulty;
		this.correctIdx = correctIdx;
		this.category = category;
	}

	private String question;
	private String[] choices = new String[4];
	private int correctIdx;
	private int difficulty;
	private String category;
}
