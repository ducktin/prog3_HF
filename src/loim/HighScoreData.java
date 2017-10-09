package loim;

import java.io.*;
import java.util.*;

/**
 * Egy dicsõségtábla adatait reprezentálja.
 * 
 * @author Martin Borbola
 */
@SuppressWarnings("serial")
public class HighScoreData implements Serializable {
	private ArrayList<HighScore> highscores;
	
	/**
	 * Visszaadja a dicsõséglistát, sorokba tördelve, elöl megjelölve a helyezéseket.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < highscores.size(); i++){
			sb.append((i+1)+". "+highscores.get(i).toString()+"\n");
		}
		return sb.toString();
	}
	
	public List<HighScore> getHighScores(){
		return highscores;
	}
	
	public HighScoreData(){
		highscores = new ArrayList<>();
	}
	
	/**
	 * Hozzáad egy új bejegyzést, amennyiben a list nincs tele(15).
	 * Ha tele van és az új bejegyzés jobb mint a listában lévõ legrosszabb.
	 * 
	 * @param name	az új játékos neve
	 * @param prize	az új játékos nyereménye
	 */
	public void add(String name, String prize){
		HighScore hs = new HighScore(name, prize);
		if(isFull() && hs.getPrizeInt()>=highscores.get(14).getPrizeInt()){
			highscores.remove(14);
		}
		highscores.add(hs);
		Collections.sort(highscores);
	}
	/**
	 * Visszaadja, hogy tele van-e a Dicsõségtábla
	 * 
	 * @return	<code>true</code> ha a listában 15 elem van; 
     *          <code>false</code> egyébként.
	 */
	private boolean isFull(){
		return highscores.size() == 15;
	}
}

/**
 * Egy bejegyzést reprezentál a dicsõségtáblában.
 * 
 * @author Martin Borbola
 */
@SuppressWarnings("serial")
class HighScore implements Serializable, Comparable<HighScore>{
	private String name;
	private String prize;
	
	HighScore(String name, String prize){
		this.name = name;
		this.prize = prize;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrize() {
		return prize;
	}
	/**
	 * Integerként adja vissza a nyeremény értékét. Az összehasonlításhoz kell.
	 * 
	 * @return	a  nyeremény értéke
	 */
	public Integer getPrizeInt(){
		return Integer.parseInt(prize.replace(" Ft", "").replace(".", ""));
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	@Override
	public String toString() {
		return name + " - " + prize;
	}

	@Override
	public int compareTo(HighScore o) {
		return o.getPrizeInt()-this.getPrizeInt();
	}
	
	@Override
	public boolean equals(Object obj) {
		HighScore hs = (HighScore) obj;
		return (hs.name.equals(this.name) && hs.prize.equals(this.prize));
	}
}
