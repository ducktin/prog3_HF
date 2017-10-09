package loim;

import java.io.*;
import java.util.*;
import javax.json.*;

/**
 * A k�rd�sadatb�zist megval�s�t� oszt�ly.
 * 
 * @author Martin Borbola
 */
public class QuestionData {

	ArrayList<Question> questions = new ArrayList<>();

	/**
	 * L�trehozza az k�rd�sadatb�zist a megfelel� f�jlb�l. <br>
	 * Egy k�rd�s fel�p�t�se:<br>
	 * <code>
	 * {<br>
	 * 	"question": "K�rd�s",<br>
	 * 	"choices": ["V�laszlehet�s�gA","V�laszlehet�s�gB","V�laszlehet�s�gC","V�laszlehet�s�gD"],<br>
	 * 	"difficulty": neh�zs�g,<br>
	 * 	"correct": j�v�lasz,<br>
	 * 	"category": "KATEG�RIA"<br>
	 * }
	 * </code>
	 * 
	 * @param jsonfile az adatb�zist t�rol� json file
	 * @throws FileNotFoundException Ha nem tal�lhat� a "questions.json" f�jl
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
	 * Lek�r egy adott neh�zs�g�, v�letlenszer� k�rd�st az adatb�zisb�l.<br>
	 * Az algoritmus el�sz�r kiv�logatja az adott neh�zs�g� k�rd�seket, majd ezekb�l v�laszt egyet.
	 * 
	 * @param difficulty	a k�rt k�rd�s neh�zs�ge
	 * @return				a visszaadott k�rd�s
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
 * Egy k�rd�st reprezent�l� oszt�ly.
 * T�rolja az egy k�rd�sre vonatkoz� inform�ci�kat.
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
