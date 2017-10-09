package loim;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

/**
 * A program grafikus ablak�t val�s�tja meg. Kezeli a felhaszn�l�i
 * interakci�kat.
 * 
 * T�rolja az �ppen fut� j�t�kot. T�rolja a dics�s�gt�bla adatait.
 * 
 * @author Martin Borbola
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements ActionListener {

	public final String[] PRIZES = { "0 Ft", "5.000 Ft", "10.000 Ft", "25.000 Ft", "50.000 Ft", "100.000 Ft",
			"200.000 Ft", "300.000 Ft", "500.000 Ft", "800.000 Ft", "1.500.000 Ft", "3.000.000 Ft", "5.000.000 Ft",
			"10.000.000 Ft", "20.000.000 Ft", "40.000.000 Ft" };

	private JPanel quizP, prizeP;
	private JTextArea txtQuestion;
	private JButton[] btnAnswers;
	private JLabel[] prizes;
	private JButton btnSplit, btnCrowd, btnPhone, btnStop;

	private JMenuItem menuNewGame, menuStopGame, menuHighScore, menuQuit;
	private JMenuItem menuSplit, menuCrowd, menuPhone;
	private JMenuItem menuDoc, menuInfo;

	private Game g;
	private HighScoreData d;

	/**
	 * El�k�sz�ti az ablak men�s�vj�t, men�ket �s a men�k elemeit. Ezekhez
	 * hozz�rendeli a megfelel� figyel�ket, majd hozz�adja �ket az ablakhoz.
	 */
	private void initMenu() {
		JMenuBar menuBar;
		JMenu menuJatek, menuSegitseg, menuSugo;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build menus.
		menuJatek = new JMenu("J�t�k");
		menuSegitseg = new JMenu("Seg�ts�gek");
		menuSugo = new JMenu("S�g�");

		// Build menu items.
		menuNewGame = new JMenuItem("�j j�t�k");
		menuNewGame.addActionListener(this);
		menuStopGame = new JMenuItem("J�t�k befejez�se");
		menuStopGame.setEnabled(false);
		menuStopGame.addActionListener(this);
		menuHighScore = new JMenuItem("Dics�s�gt�bla");
		menuHighScore.addActionListener(this);
		menuQuit = new JMenuItem("Kil�p�s");
		menuQuit.addActionListener(this);

		menuSplit = new JMenuItem("Felez�s");
		menuSplit.addActionListener(this);
		menuCrowd = new JMenuItem("K�z�ns�g");
		menuCrowd.addActionListener(this);
		menuPhone = new JMenuItem("Telefon");
		menuPhone.addActionListener(this);

		menuDoc = new JMenuItem("Dokument�ci�");
		menuDoc.addActionListener(this);
		menuInfo = new JMenuItem("N�vjegy");
		menuInfo.addActionListener(this);

		// Add menu items to menus
		menuJatek.add(menuNewGame);
		menuJatek.add(menuStopGame);
		menuJatek.add(menuHighScore);
		menuJatek.add(menuQuit);

		menuSegitseg.add(menuSplit);
		menuSegitseg.add(menuCrowd);
		menuSegitseg.add(menuPhone);

		menuSugo.add(menuDoc);
		menuSugo.add(menuInfo);

		// Add menus to menu bar
		menuBar.add(menuJatek);
		menuBar.add(menuSegitseg);
		menuBar.add(menuSugo);

		// Set menu bar.
		this.setJMenuBar(menuBar);
	}

	/**
	 * Fel�p�ti az ablakot. Megh�vja az ablakot fel�p�t� seg�df�ggv�nyeket.
	 * F�jlb�l bet�lti a dics�s�gt�bla adatait.
	 * 
	 * @param _g
	 *            az ablakhoz tartoz� j�t�k
	 */
	public GameFrame(Game _g) {
		g = _g;

		try {
			loadHighScores();
		} catch (ClassNotFoundException | IOException e2) {
			e2.printStackTrace();
		}

		initMenu();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new GridLayout(1, 2));

		quizP = new JPanel(new GridLayout(2, 1, 10, 10));
		quizP.setPreferredSize(new Dimension(600, 600));
		txtQuestion = initTxtQuestion();
		quizP.add(txtQuestion);
		quizP.add(initAnswerPane());
		c.add(quizP);

		prizeP = new JPanel(new GridLayout(2, 1, 10, 10));
		prizeP.setPreferredSize(new Dimension(200, 600));
		prizeP.add(initPrizesPane());
		prizeP.add(initHelpPane());
		c.add(prizeP);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					saveHighScores();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		updateEnabled();

		setTitle("Legyen �n is milliomos");
		setLocation(400, 100);
		setSize(800, 600);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Az ablak konstruktor seg�df�ggv�nye. Elk�sz�ti az ablak bal oldalhoz
	 * sz�ks�ges k�rd�smez�t.
	 * 
	 * @return a k�sz�tett k�rd�smez�
	 */
	private JTextArea initTxtQuestion() {
		JTextArea jta = new JTextArea(4, 50);
		jta.setFont(new Font("default", Font.PLAIN, 20));
		jta.setBackground(this.getBackground());
		jta.setWrapStyleWord(true);
		jta.setEditable(false);
		return jta;
	}

	/**
	 * Az ablak konstruktor seg�df�ggv�nye. Elk�sz�ti az ablak bal oldalhoz
	 * sz�ks�ges v�laszgomb ter�letet.
	 * 
	 * @return a k�sz�tett v�laszgomb ter�let
	 */
	private JComponent initAnswerPane() {
		JPanel answersP = new JPanel(new GridLayout(2, 2, 30, 30));
		answersP.setSize(new Dimension(600, 200));
		btnAnswers = new JButton[4];
		for (int i = 0; i < 4; i++) {
			btnAnswers[i] = new JButton();
			btnAnswers[i].setEnabled(false);
			btnAnswers[i].addActionListener(this);
			answersP.add(btnAnswers[i], i);
		}

		return answersP;
	}

	/**
	 * Az ablak konstruktor seg�df�ggv�nye. Elk�sz�ti az ablak jobb oldalhoz
	 * sz�ks�ges nyerem�nyek ter�let�t.
	 * 
	 * @return a k�sz�tett nyerem�ny ter�lets
	 */
	private JComponent initPrizesPane() {

		JPanel prizesP = new JPanel(new GridLayout(15, 1, 10, 0));
		prizes = new JLabel[15];
		for (int i = 15; i >= 1; i--) {
			prizes[i - 1] = new JLabel(Integer.toString(i) + " " + PRIZES[i]);
			prizes[i - 1].setHorizontalAlignment(SwingConstants.CENTER);
			prizesP.add(prizes[i - 1]);
		}
		return prizesP;
	}

	/**
	 * Az ablak konstruktor seg�df�ggv�nye. Elk�sz�ti az ablak jobb oldalhoz
	 * sz�ks�ges seg�ts�gek �s felad�s gombok ter�let�t.
	 * 
	 * @return a k�sz�tett gombok ter�lete
	 */
	private JComponent initHelpPane() {
		JPanel helpP = new JPanel(new GridLayout(4, 1, 10, 30));

		btnSplit = new JButton("Felez");
		btnSplit.addActionListener(this);
		btnCrowd = new JButton("K�z�ns�g");
		btnCrowd.addActionListener(this);
		btnPhone = new JButton("Telefon");
		btnPhone.addActionListener(this);
		btnStop = new JButton("Meg�ll");
		btnStop.addActionListener(this);

		helpP.add(btnSplit);
		helpP.add(btnCrowd);
		helpP.add(btnPhone);
		helpP.add(btnStop);

		return helpP;
	}

	/**
	 * F�jlb�l bet�lti a dics�s�gt�bla tartalm�t. Haszn�lt f�jl:
	 * "highscores.dat"<br>A program indul�sakor h�v�dik meg.
	 * 
	 * @throws IOException				ha hiba t�rt�net a beolvas�skor
	 * @throws FileNotFoundException	ha nem tal�lhat� a "highscores.dat" f�jl
	 * @throws ClassNotFoundException	ha nem tal�lhat� az HighScoreData oszt�ly
	 */
	private void loadHighScores() throws IOException, FileNotFoundException, ClassNotFoundException {
		d = new HighScoreData();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("highscores.dat"));
		d = (HighScoreData) ois.readObject();
		ois.close();
	}

	/**
	 * F�jlba elmenti a dics�s�gt�bla tartalm�t. Haszn�lt f�jl: "highscores.dat"
	 * A program bez�r�sakor h�v�dik meg.
	 * 
	 * @throws IOException				ha hiba t�rt�net az �r�skor
	 * @throws FileNotFoundException	ha nem tal�lhat� a "highscores.dat" f�jl
	 */
	private void saveHighScores() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("highscores.dat"));
		oos.writeObject(d);
		oos.close();
	}

	/**
	 * Kezeli a felhaszn�l�i interakci�kat.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 4; i++) {
			if (e.getSource().equals(btnAnswers[i])) {
				if (g.getCorrectAnswer() == i) {
					goodAnswer();
				} else {
					loseGame();
				}
			}
		}
		if (e.getSource().equals(menuSplit) || e.getSource().equals(btnSplit)) {
			split();
		} else if (e.getSource().equals(menuCrowd) || e.getSource().equals(btnCrowd)) {
			crowd();
		} else if (e.getSource().equals(menuPhone) || e.getSource().equals(btnPhone)) {
			phone();
		} else if (e.getSource().equals(menuNewGame)) {
			newGame();
		} else if (e.getSource().equals(menuStopGame) || e.getSource().equals(btnStop)) {
			stopGame();
		} else if (e.getSource().equals(menuQuit)) {
			try {
				saveHighScores();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		} else if (e.getSource().equals(menuHighScore)) {
			openHighScore();
		} else if (e.getSource().equals(menuDoc)) {
			openDoc();
		} else if (e.getSource().equals(menuInfo)) {
			openInfo();
		}
	}

	/**
	 * A Felez�s seg�ts�get val�s�tja meg. Kiv�laszt k�t v�letlenszer� rossz
	 * opci�t, �s letiltja a hozz�juk tartoz� gombokat, ezzel seg�tve a J�t�kos
	 * v�lasz�t. A seg�ts�get csak egyszer lehet haszn�lni.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos felhaszn�lj a Felez�s seg�ts�get.
	 */
	private void split() {
		g.useSplit();
		menuSplit.setEnabled(false);
		btnSplit.setEnabled(false);
		Random r = new Random();
		int correct = g.getCorrectAnswer();
		int stay = r.nextInt(4);
		while (stay == correct) {
			stay = r.nextInt(4);
		}
		for (int i = 0; i < 4; i++) {
			if (!(i == correct || i == stay)) {
				btnAnswers[i].setEnabled(false);
			}
		}
	}

	/**
	 * A K�z�ns�g seg�ts�get val�s�tja meg. Egy felugr� ablakban �rtes�ti a
	 * J�t�kost, hogyan d�nt�tt a k�z�ns�g; A seg�ts�get csak egyszer lehet
	 * haszn�lni.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos felhaszn�lj a K�z�ns�g seg�ts�get.
	 */
	private void crowd() {
		g.useCrowd();
		menuCrowd.setEnabled(false);
		btnCrowd.setEnabled(false);
		int[] votes = getVotes();
		String message = "A k�z�ns�g szavazott:\n" + "A : " + votes[0] + "%\n" + "B : " + votes[1] + "%\n" + "C : "
				+ votes[2] + "%\n" + "D : " + votes[3] + "%\n";
		JOptionPane.showMessageDialog(this, message, "K�z�ns�g", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Megk�rdezi a k�z�ns�get az aktu�lis k�rd�sr�l.
	 * 
	 * @return a k�z�ns�g szavazatinak eloszl�sa sz�zal�kban
	 */
	private int[] getVotes() {
		Random rnd = new Random();
		int[] dispersion = new int[15];
		for(int i = 0; i<15; i++){
			int bound = (i+2)*4;
			dispersion[i] = 100-rnd.nextInt(bound);
		}
		int[] votes = new int[4];
		for (int i = 0; i < 4; i++) {
			if (i == g.getCorrectAnswer()) {
				votes[i] = dispersion[g.getCurrentQuestionNumber()];
			} else {
				votes[i] = (100-dispersion[g.getCurrentQuestionNumber()])/3;
			}
		}
		return votes;
	}

	/**
	 * A Telefonos seg�ts�get val�s�tja meg. Egy felugr� ablakban �rtes�ti a
	 * J�t�kost, hogyan v�laszolt a bar�tja. A seg�ts�get csak egyszer lehet
	 * haszn�lni.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos felhaszn�lj a Telefonos seg�ts�get.
	 */
	private void phone() {
		String[] a = {"A","B","C","D"};
		String[] options = {"Biztosan a(z) ", "Szerintem a(z) ","Tal�n a(z) ", "Nem biztos, de a(z) "};
		int[] optionsChance = {90,60,40,30};
		int idx = new Random().nextInt(4);
		g.usePhone();
		menuPhone.setEnabled(false);
		btnPhone.setEnabled(false);
		String anw;
		if(new Random().nextInt(100)<optionsChance[idx]){
			anw = a[g.getCorrectAnswer()];
		} else {
			int r = new Random().nextInt(4);
			while(r!=g.getCorrectAnswer())
				r = new Random().nextInt(4);
			anw = a[r];
		}
		JOptionPane.showMessageDialog(this, options[idx] + anw + ".", "Telefon", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Akkor h�v�dik meg, ha a J�t�kos j� v�laszt adott egy k�rd�sre. A program
	 * el�k�sz�ti a k�vetkez� k�rd�st, �s friss�ti az ablakot. Ha ez a 15.
	 * k�rd�s volt a J�t�kos nyert.
	 * 
	 */
	private void goodAnswer() {
		if (g.getCurrentQuestionNumber() == 15) {
			winGame();
			return;
		}
		updateEnabled();
		g.nextQuestion();
		updateQuiz();
	}

	/**
	 * Friss�ti az k�rd�st, �s a v�laszokat az aktu�lis k�rd�s alapj�n.
	 */
	private void updateQuiz() {
		String[] anw = { "A", "B", "C", "D" };
		txtQuestion.setText(g.getCurrentQuestion());
		String[] a = g.getChoices();
		for (int j = 0; j < 4; j++) {
			btnAnswers[j].setText(anw[j] + ": " + a[j]);
			btnAnswers[j].setBackground(Color.getColor("default"));
			btnAnswers[j].setEnabled(g.isRunning());
		}
		updatePrizes();
	}

	/**
	 * Friss�ti a nyerem�nyt�bl�t a j�t�k aktu�lis �ll�s�t�l f�gg�en.
	 */
	private void updatePrizes() {
		for (int i = 0; i < 15; i++) {
			if (i < g.getCurrentQuestionNumber() - 1) {
				prizes[i].setForeground(Color.green);
			} else if (i == g.getCurrentQuestionNumber() - 1) {
				prizes[i].setForeground(Color.red);
			} else {
				prizes[i].setForeground(Color.black);
			}
		}
	}

	/**
	 * Friss�ti a kattinthat� gombokat a j�t�k aktu�lis �ll�s�t�l f�gg�en.
	 */
	private void updateEnabled() {
		menuStopGame.setEnabled(g.isRunning());
		menuNewGame.setEnabled(!g.isRunning());

		btnSplit.setEnabled(g.isRunning() && g.hasSplit());
		menuSplit.setEnabled(g.isRunning() && g.hasSplit());
		btnCrowd.setEnabled(g.isRunning() && g.hasCrowd());
		menuCrowd.setEnabled(g.isRunning() && g.hasCrowd());
		btnPhone.setEnabled(g.isRunning() && g.hasPhone());
		menuPhone.setEnabled(g.isRunning() && g.hasPhone());
		btnStop.setEnabled(g.isRunning());
		for (int j = 0; j < 4; j++) {
			btnAnswers[j].setEnabled(g.isRunning());
		}
	}

	/**
	 * Elind�t egy �j j�t�kot. Akkor h�v�dik meg, ha a J�t�kos a men�b�l elind�t
	 * egy �j j�t�kot.
	 */
	private void newGame() {
		g.start();
		updateEnabled();
		updateQuiz();
	}

	/**
	 * Meg�ll�tja a jelenleg fut� j�t�kot. Akkor h�v�dik meg, ha a J�t�kos
	 * feladja j�t�kot. <br>
	 * Az aktu�lis nyerem�ny be�r�dik a Dics�s�gt�bl�ba, a J�t�kos �ltal adott
	 * n�vvel.
	 */
	private void stopGame() {
		g.stop();
		updateEnabled();
		showGoodAnswer();
		String name = (String) JOptionPane.showInputDialog(this,
				"Meg�ll�tottad a j�t�kot!\nNyerem�nyed: " + stopPrize() + "\nK�rlek add meg a neved:", "Meg�llt�l",
				JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "P�lda P�l";
		d.add(name, stopPrize());
	}

	/**
	 * A J�t�kos megnyeri a jelenleg fut� j�t�kot. Akkor h�v�dik meg, ha a
	 * J�t�kos megnyeri j�t�kot. (Helyesen v�laszol 15 k�rd�sre) <br>
	 * Az f�nyerem�ny be�r�dik a Dics�s�gt�bl�ba, a J�t�kos �ltal adott n�vvel.
	 */
	private void winGame() {
		g.stop();
		updateEnabled();
		String name = (String) JOptionPane.showInputDialog(this,
				"Gratul�lok, megnyerted a 40.000.000 Ft-ot!" + "\nK�rlek add meg a neved:", "Nyert�l!!",
				JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "P�lda P�l";
		d.add(name, "40.000.000 Ft");
	}

	/**
	 * A J�t�kos elvesztette a jelenleg fut� j�t�kot. Akkor h�v�dik meg, ha a
	 * J�t�kos rosszul v�laszol egy k�rd�sre j�t�kot. <br>
	 * Az legut�bbi garant�lt nyerem�ny be�r�dik a Dics�s�gt�bl�ba, a J�t�kos
	 * �ltal adott n�vvel.
	 */
	private void loseGame() {
		g.stop();
		updateEnabled();
		showGoodAnswer();
		String name = (String) JOptionPane.showInputDialog(this,
				"Rosszul v�laszolt�l, ez�rt vesztett�l!\nNyerem�nyed: " + lostPrize() + "\nK�rlek add meg a neved:",
				"Vesztett�l", JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "P�lda P�l";
		d.add(name, lostPrize());
	}
	/**
	 * Megmutatja a j� �s rossz megold�sokat
	 */
	private void showGoodAnswer(){
		for (int j = 0; j < 4; j++) {
			if (j == g.getCorrectAnswer()) {
				btnAnswers[j].setBackground(Color.green);
			} else {
				btnAnswers[j].setBackground(Color.red);
			}
		}
	}

	/**
	 * Meghat�rozza a legut�bbi garatn�lt nyerem�nyt.
	 * 
	 * @return a legut�bbi garatn�lt nyerem�ny
	 */
	private String lostPrize() {
		if (g.getCurrentQuestionNumber() <= 5)
			return PRIZES[0];
		else if (g.getCurrentQuestionNumber() <= 10)
			return PRIZES[5];
		else
			return PRIZES[10];
	}

	/**
	 * Meghat�rozza a legut�bbi nyerem�nyt.
	 * 
	 * @return az aktu�lis nyerem�ny
	 */
	private String stopPrize() {
		return PRIZES[g.getCurrentQuestionNumber() - 1];
	}

	/**
	 * Egy felugr� ablakban megjelen�ti a Dics�s�gt�bl�t.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos a men�ben megnyomta a dics�s�gt�bl�t.
	 */
	private void openHighScore() {
		JOptionPane.showMessageDialog(this, d.toString(), "Dics�s�glista", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Egy k�ls� programban megjelen�ti a Dokument�ci�t.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos a men�ben megnyomta a dokument�ci�t.
	 */
	private void openDoc() {
		try {
			File myFile = new File("dokumentacio.pdf");
			Desktop.getDesktop().open(myFile);
		} catch (IOException ex) {
			// no application registered for PDFs
		}
	}

	/**
	 * Egy felugr� ablakban megjelen�ti a N�vjegyet.
	 * 
	 * Akkor h�v�dik meg, ha a J�t�kos a men�ben megnyomta a n�vjegyet.
	 */
	private void openInfo() {
		JOptionPane.showMessageDialog(this, "A programot k�sz�tette: Borbola Martin\n" + "2016 - Programoz�s alapjai 3",
				"N�vjegy", JOptionPane.PLAIN_MESSAGE);
	}
	
	public Game getG(){
		return g;
	}
}
