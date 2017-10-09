package loim;

import java.io.*;
import java.util.*;

/**
 * Egy dics�s�gt�bla adatait reprezent�lja.
 * 
 * @author Martin Borbola
 */
@SuppressWarnings("serial")
public class HighScoreData implements Serializable {
	private ArrayList<HighScore> highscores;
	
	/**
	 * Visszaadja a dics�s�glist�t, sorokba t�rdelve, el�l megjel�lve a helyez�seket.
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
	 * Hozz�ad egy �j bejegyz�st, amennyiben a list nincs tele(15).
	 * Ha tele van �s az �j bejegyz�s jobb mint a list�ban l�v� legrosszabb.
	 * 
	 * @param name	az �j j�t�kos neve
	 * @param prize	az �j j�t�kos nyerem�nye
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
	 * Visszaadja, hogy tele van-e a Dics�s�gt�bla
	 * 
	 * @return	<code>true</code> ha a list�ban 15 elem van; 
     *          <code>false</code> egy�bk�nt.
	 */
	private boolean isFull(){
		return highscores.size() == 15;
	}
}

/**
 * Egy bejegyz�st reprezent�l a dics�s�gt�bl�ban.
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
	 * Integerk�nt adja vissza a nyerem�ny �rt�k�t. Az �sszehasonl�t�shoz kell.
	 * 
	 * @return	a  nyerem�ny �rt�ke
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
