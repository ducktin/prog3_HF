package loim;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

/**
 * A program grafikus ablakát valósítja meg. Kezeli a felhasználói
 * interakciókat.
 * 
 * Tárolja az éppen futó játékot. Tárolja a dicsõségtábla adatait.
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
	 * Elõkészíti az ablak menüsávját, menüket és a menük elemeit. Ezekhez
	 * hozzárendeli a megfelelõ figyelõket, majd hozzáadja õket az ablakhoz.
	 */
	private void initMenu() {
		JMenuBar menuBar;
		JMenu menuJatek, menuSegitseg, menuSugo;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build menus.
		menuJatek = new JMenu("Játék");
		menuSegitseg = new JMenu("Segítségek");
		menuSugo = new JMenu("Súgó");

		// Build menu items.
		menuNewGame = new JMenuItem("Új játék");
		menuNewGame.addActionListener(this);
		menuStopGame = new JMenuItem("Játék befejezése");
		menuStopGame.setEnabled(false);
		menuStopGame.addActionListener(this);
		menuHighScore = new JMenuItem("Dicsõségtábla");
		menuHighScore.addActionListener(this);
		menuQuit = new JMenuItem("Kilépés");
		menuQuit.addActionListener(this);

		menuSplit = new JMenuItem("Felezés");
		menuSplit.addActionListener(this);
		menuCrowd = new JMenuItem("Közönség");
		menuCrowd.addActionListener(this);
		menuPhone = new JMenuItem("Telefon");
		menuPhone.addActionListener(this);

		menuDoc = new JMenuItem("Dokumentáció");
		menuDoc.addActionListener(this);
		menuInfo = new JMenuItem("Névjegy");
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
	 * Felépíti az ablakot. Meghívja az ablakot felépítõ segédfüggvényeket.
	 * Fájlból betölti a dicsõségtábla adatait.
	 * 
	 * @param _g
	 *            az ablakhoz tartozó játék
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

		setTitle("Legyen Ön is milliomos");
		setLocation(400, 100);
		setSize(800, 600);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Az ablak konstruktor segédfüggvénye. Elkészíti az ablak bal oldalhoz
	 * szükséges kérdésmezõt.
	 * 
	 * @return a készített kérdésmezõ
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
	 * Az ablak konstruktor segédfüggvénye. Elkészíti az ablak bal oldalhoz
	 * szükséges válaszgomb területet.
	 * 
	 * @return a készített válaszgomb terület
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
	 * Az ablak konstruktor segédfüggvénye. Elkészíti az ablak jobb oldalhoz
	 * szükséges nyeremények területét.
	 * 
	 * @return a készített nyeremény területs
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
	 * Az ablak konstruktor segédfüggvénye. Elkészíti az ablak jobb oldalhoz
	 * szükséges segítségek és feladás gombok területét.
	 * 
	 * @return a készített gombok területe
	 */
	private JComponent initHelpPane() {
		JPanel helpP = new JPanel(new GridLayout(4, 1, 10, 30));

		btnSplit = new JButton("Felez");
		btnSplit.addActionListener(this);
		btnCrowd = new JButton("Közönség");
		btnCrowd.addActionListener(this);
		btnPhone = new JButton("Telefon");
		btnPhone.addActionListener(this);
		btnStop = new JButton("Megáll");
		btnStop.addActionListener(this);

		helpP.add(btnSplit);
		helpP.add(btnCrowd);
		helpP.add(btnPhone);
		helpP.add(btnStop);

		return helpP;
	}

	/**
	 * Fájlból betölti a dicsõségtábla tartalmát. Használt fájl:
	 * "highscores.dat"<br>A program indulásakor hívódik meg.
	 * 
	 * @throws IOException				ha hiba történet a beolvasáskor
	 * @throws FileNotFoundException	ha nem található a "highscores.dat" fájl
	 * @throws ClassNotFoundException	ha nem található az HighScoreData osztály
	 */
	private void loadHighScores() throws IOException, FileNotFoundException, ClassNotFoundException {
		d = new HighScoreData();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("highscores.dat"));
		d = (HighScoreData) ois.readObject();
		ois.close();
	}

	/**
	 * Fájlba elmenti a dicsõségtábla tartalmát. Használt fájl: "highscores.dat"
	 * A program bezárásakor hívódik meg.
	 * 
	 * @throws IOException				ha hiba történet az íráskor
	 * @throws FileNotFoundException	ha nem található a "highscores.dat" fájl
	 */
	private void saveHighScores() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("highscores.dat"));
		oos.writeObject(d);
		oos.close();
	}

	/**
	 * Kezeli a felhasználói interakciókat.
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
	 * A Felezés segítséget valósítja meg. Kiválaszt két véletlenszerû rossz
	 * opciót, és letiltja a hozzájuk tartozó gombokat, ezzel segítve a Játékos
	 * válaszát. A segítséget csak egyszer lehet használni.
	 * 
	 * Akkor hívódik meg, ha a Játékos felhasználj a Felezés segítséget.
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
	 * A Közönség segítséget valósítja meg. Egy felugró ablakban értesíti a
	 * Játékost, hogyan döntött a közönség; A segítséget csak egyszer lehet
	 * használni.
	 * 
	 * Akkor hívódik meg, ha a Játékos felhasználj a Közönség segítséget.
	 */
	private void crowd() {
		g.useCrowd();
		menuCrowd.setEnabled(false);
		btnCrowd.setEnabled(false);
		int[] votes = getVotes();
		String message = "A közönség szavazott:\n" + "A : " + votes[0] + "%\n" + "B : " + votes[1] + "%\n" + "C : "
				+ votes[2] + "%\n" + "D : " + votes[3] + "%\n";
		JOptionPane.showMessageDialog(this, message, "Közönség", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Megkérdezi a közönséget az aktuális kérdésrõl.
	 * 
	 * @return a közönség szavazatinak eloszlása százalékban
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
	 * A Telefonos segítséget valósítja meg. Egy felugró ablakban értesíti a
	 * Játékost, hogyan válaszolt a barátja. A segítséget csak egyszer lehet
	 * használni.
	 * 
	 * Akkor hívódik meg, ha a Játékos felhasználj a Telefonos segítséget.
	 */
	private void phone() {
		String[] a = {"A","B","C","D"};
		String[] options = {"Biztosan a(z) ", "Szerintem a(z) ","Talán a(z) ", "Nem biztos, de a(z) "};
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
	 * Akkor hívódik meg, ha a Játékos jó választ adott egy kérdésre. A program
	 * elõkészíti a következõ kérdést, és frissíti az ablakot. Ha ez a 15.
	 * kérdés volt a Játékos nyert.
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
	 * Frissíti az kérdést, és a válaszokat az aktuális kérdés alapján.
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
	 * Frissíti a nyereménytáblát a játék aktuális állásától függõen.
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
	 * Frissíti a kattintható gombokat a játék aktuális állásától függõen.
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
	 * Elindít egy új játékot. Akkor hívódik meg, ha a Játékos a menübõl elindít
	 * egy új játékot.
	 */
	private void newGame() {
		g.start();
		updateEnabled();
		updateQuiz();
	}

	/**
	 * Megállítja a jelenleg futó játékot. Akkor hívódik meg, ha a Játékos
	 * feladja játékot. <br>
	 * Az aktuális nyeremény beíródik a Dicsõségtáblába, a Játékos által adott
	 * névvel.
	 */
	private void stopGame() {
		g.stop();
		updateEnabled();
		showGoodAnswer();
		String name = (String) JOptionPane.showInputDialog(this,
				"Megállítottad a játékot!\nNyereményed: " + stopPrize() + "\nKérlek add meg a neved:", "Megálltál",
				JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "Példa Pál";
		d.add(name, stopPrize());
	}

	/**
	 * A Játékos megnyeri a jelenleg futó játékot. Akkor hívódik meg, ha a
	 * Játékos megnyeri játékot. (Helyesen válaszol 15 kérdésre) <br>
	 * Az fõnyeremény beíródik a Dicsõségtáblába, a Játékos által adott névvel.
	 */
	private void winGame() {
		g.stop();
		updateEnabled();
		String name = (String) JOptionPane.showInputDialog(this,
				"Gratulálok, megnyerted a 40.000.000 Ft-ot!" + "\nKérlek add meg a neved:", "Nyertél!!",
				JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "Példa Pál";
		d.add(name, "40.000.000 Ft");
	}

	/**
	 * A Játékos elvesztette a jelenleg futó játékot. Akkor hívódik meg, ha a
	 * Játékos rosszul válaszol egy kérdésre játékot. <br>
	 * Az legutóbbi garantált nyeremény beíródik a Dicsõségtáblába, a Játékos
	 * által adott névvel.
	 */
	private void loseGame() {
		g.stop();
		updateEnabled();
		showGoodAnswer();
		String name = (String) JOptionPane.showInputDialog(this,
				"Rosszul válaszoltál, ezért vesztettél!\nNyereményed: " + lostPrize() + "\nKérlek add meg a neved:",
				"Vesztettél", JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.isEmpty()) name = "Példa Pál";
		d.add(name, lostPrize());
	}
	/**
	 * Megmutatja a jó és rossz megoldásokat
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
	 * Meghatározza a legutóbbi garatnált nyereményt.
	 * 
	 * @return a legutóbbi garatnált nyeremény
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
	 * Meghatározza a legutóbbi nyereményt.
	 * 
	 * @return az aktuális nyeremény
	 */
	private String stopPrize() {
		return PRIZES[g.getCurrentQuestionNumber() - 1];
	}

	/**
	 * Egy felugró ablakban megjeleníti a Dicsõségtáblát.
	 * 
	 * Akkor hívódik meg, ha a Játékos a menüben megnyomta a dicsõségtáblát.
	 */
	private void openHighScore() {
		JOptionPane.showMessageDialog(this, d.toString(), "Dicsõséglista", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Egy külsõ programban megjeleníti a Dokumentációt.
	 * 
	 * Akkor hívódik meg, ha a Játékos a menüben megnyomta a dokumentációt.
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
	 * Egy felugró ablakban megjeleníti a Névjegyet.
	 * 
	 * Akkor hívódik meg, ha a Játékos a menüben megnyomta a névjegyet.
	 */
	private void openInfo() {
		JOptionPane.showMessageDialog(this, "A programot készítette: Borbola Martin\n" + "2016 - Programozás alapjai 3",
				"Névjegy", JOptionPane.PLAIN_MESSAGE);
	}
	
	public Game getG(){
		return g;
	}
}
