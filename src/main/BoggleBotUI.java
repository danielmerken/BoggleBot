package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Applet for BoggleBot. Users can pick the board dimensions, manually input
 * letters into the board, or pick a random board. Once filled out, user can
 * specify the minimum length of the solution to find, then solve the board.
 * Solution words can be sorted by score or alphabetically.
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BoggleBotUI extends JApplet implements ActionListener, ListSelectionListener {
	private static final Integer[] DIMENSION_OPTIONS = {3, 4, 5, 6, 7, 8};
	private static final Integer[] MIN_WORD_LENGTH_OPTIONS = {1, 2, 3, 4};
	private static final String[] SORT_OPTIONS = {"Score", "Alphabet"};
	private static final int DEFAULT_BOARD_WIDTH = 3;
	private static final int DEFAULT_BOARD_HEIGHT = 3;
	private static final int DEFAULT_MIN_LENGTH = 3;
	
	/**
	 * Buttons for managing the board
	 */
	private JButton clearButton;
	private JButton randomButton;
	private JButton solveButton;
	
	/**
	 * Drop down boxes for declaring settings
	 */
	private JComboBox<Integer> widthBox;
	private JComboBox<Integer> heightBox;
	private JComboBox<Integer> minLengthBox;
	
	/**
	 * Containers for board
	 */
	private JPanel boardPanel;
	private JPanel boardContainer;
	
	/**
	 * List of solutions and drop down box to determine its sorting method
	 */
	private JList<BogglePath> solutionsList;
	private JComboBox<String> sortBox;
	
	/**
	 * Dimensions of board (in spaces)
	 */
	private int boardWidth;
	private int boardHeight;
	
	/**
	 * Contents of solution list
	 */
	private SortableListModel<BogglePath> solutionsModel;
	
	/**
	 * Initializes this applet with its default settings
	 */
	public void init() {		
		setSize(1000, 500);
		
		getContentPane().setLayout(new GridBagLayout());
		Font textFont = new Font("SansSerif", Font.PLAIN, (int) (getHeight() * .03));
		GridBagConstraints constraint = new GridBagConstraints();

		JPanel dashboardPanel = new JPanel();
		clearButton = new JButton("Clear Board");
		clearButton.setFont(textFont);
		randomButton = new JButton("Randomize Board");
		randomButton.setFont(textFont);
		solveButton = new JButton("Solve Board");
		solveButton.setFont(textFont);
		widthBox = new JComboBox<Integer>(DIMENSION_OPTIONS);
		widthBox.setFont(textFont);
		heightBox = new JComboBox<Integer>(DIMENSION_OPTIONS);
		heightBox.setFont(textFont);
		minLengthBox = new JComboBox<Integer>(MIN_WORD_LENGTH_OPTIONS);
		minLengthBox.setFont(textFont);
		minLengthBox.setSelectedItem(DEFAULT_MIN_LENGTH);
		
		JLabel currLabel = new JLabel("Board Dimensions:");
		currLabel.setFont(textFont);
		dashboardPanel.add(currLabel);
		dashboardPanel.add(widthBox);
		currLabel = new JLabel("x");
		currLabel.setFont(textFont);
		dashboardPanel.add(currLabel);
		dashboardPanel.add(heightBox);
		currLabel = new JLabel("Minimum Word Length:");
		currLabel.setFont(textFont);
		dashboardPanel.add(currLabel);
		dashboardPanel.add(minLengthBox);
		dashboardPanel.add(clearButton);
		dashboardPanel.add(randomButton);
		dashboardPanel.add(solveButton);
		
		widthBox.addActionListener(this);
		heightBox.addActionListener(this);
		clearButton.addActionListener(this);
		randomButton.addActionListener(this);
		solveButton.addActionListener(this);

		boardContainer = new JPanel();
		
		JPanel solutionsPanel = new JPanel();
		solutionsPanel.setLayout(new GridBagLayout());
		sortBox = new JComboBox<String>(SORT_OPTIONS);
		sortBox.addActionListener(this);
		constraint.gridx = 0;
		constraint.gridy = 0;
		solutionsPanel.add(sortBox, constraint);
		solutionsList = new JList<BogglePath>();
		solutionsList.setFont(textFont);
		solutionsList.setLayoutOrientation(JList.VERTICAL_WRAP);
		solutionsList.setVisibleRowCount(-1);
		solutionsList.addListSelectionListener(this);
		solutionsModel = new SortableListModel<BogglePath>();
		solutionsList.setModel(solutionsModel);
		JScrollPane solutionsPane = new JScrollPane(solutionsList, 
				JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		constraint.gridy = 1;
		constraint.weightx = 1;
		constraint.weighty = 1;
		constraint.fill = GridBagConstraints.BOTH;
		solutionsPanel.add(solutionsPane, constraint);
		
		constraint.weightx = 0;
		constraint.weighty = 0;
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 0;
		add(dashboardPanel, constraint);
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridy = 1;
		constraint.weighty = 1.0;
		add(boardContainer, constraint);
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridy = 2;
		constraint.weighty = .6;
		add(solutionsPanel, constraint);
		boardContainer.setPreferredSize(boardContainer.getSize());
		boardContainer.setLayout(new GridBagLayout());
		
		validate();
		
		boardPanel = new JPanel();
		boardWidth = DEFAULT_BOARD_WIDTH;
		boardHeight = DEFAULT_BOARD_HEIGHT;
		updateBoard();
		boardContainer.add(boardPanel, new GridBagConstraints());
	}
	
	/**
	 * Removes the current board and solution, then creates a new board with
	 * the current settings for width and height
	 */
	private void updateBoard() {
		boardPanel.removeAll();
		clearSolutions();
		int spaceSize = Math.min(boardContainer.getWidth() / boardWidth, 
				 boardContainer.getHeight() / boardHeight);
		boardPanel.setPreferredSize(new Dimension(spaceSize * boardWidth, spaceSize * boardHeight));
		GridLayout l = new GridLayout(boardHeight, boardWidth);
		boardPanel.setLayout(l);
		Font boardFont = new Font("SansSerif", Font.BOLD, (int) (spaceSize * .6));
		for (int i = 0; i < boardHeight * boardWidth; i++) {
			JTextField currField = new JTextField();
			currField.setHorizontalAlignment(JTextField.CENTER);
			currField.setFont(boardFont);
			boardPanel.add(currField);
		}
		validate();
	}
	
	/**
	 * Clears the solutions list
	 */
	public void clearSolutions() {
		for (Component c : boardPanel.getComponents()) {
			((JTextField) c).setBackground(Color.WHITE);
		}
		solutionsList.clearSelection();
		solutionsList.setListData(new BogglePath[0]);
	}
	
	/**
	 * Sets all the boards spaces to be blank
	 */
	public void clearBoard() {
		clearSolutions();
		for (Component c : boardPanel.getComponents()) {
			((JTextField) c).setText("");
		}
	}
	
	/**
	 * Fills all the boards spaces with random Boggle letters (capital letters,
	 * except for "Q", and "Qu")
	 */
	public void randomBoard() {
		clearSolutions();
		for (Component c : boardPanel.getComponents()) {
			((JTextField) c).setText(BoggleBoard.generateRandomBoggleLetter());
		}
	}

	/**
	 * Finds solutions to the current board if all spaces are filled with valid
	 * Boggle letters, otherwise notifies user and highlights the first space
	 * that is improperly filled out
	 */
	public void solveBoard() {
		int i = 0;
		String[][] letters = new String[boardHeight][boardWidth];
		for (Component c : boardPanel.getComponents()) {
			String letter = ((JTextField) c).getText();
			/* If the current space is not filled out properly, highlight it
			   and notify user*/
			if ((letter.length() > 1 && !letter.toLowerCase().equals("qu")) ||
					letter.length() == 0 || letter.toLowerCase().equals("q") ||
					!Character.isAlphabetic(letter.charAt(0))) {
				c.setBackground(Color.RED);
				JOptionPane.showMessageDialog(null, "Each square must contain one letter from the alphabet,"
						+ " except for \"Q\", or \"Qu\"");
				return;
			}
			if (letter.toLowerCase().equals("qu")) {
				letters[i / boardWidth][i % boardWidth] = "Qu";
			} else {
				letters[i / boardWidth][i % boardWidth] = letter.toUpperCase();
			}
			i++;
		}
		Trie dictionary = DictionaryParser.parseDictionary((int) minLengthBox.getSelectedItem());
		BoggleBoard board = new BoggleBoard(letters);
		BoggleBot.solveBoard(dictionary, board);
		System.out.println(board);
		System.out.println(board.getSolutionWords());
		solutionsModel.set(board.getSolutions(), new BogglePathScoreComparator());
		solutionsList.setModel(solutionsModel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == widthBox) {
			boardWidth = (int) widthBox.getSelectedItem();
			updateBoard();
		} else if (e.getSource() == heightBox) {
			boardHeight = (int) heightBox.getSelectedItem();
			updateBoard();
		} else if (e.getSource() == clearButton) {
			clearBoard();
		} else if (e.getSource() == randomButton) {
			randomBoard();
		} else if (e.getSource() == solveButton) {
			solveBoard();
		} else if (e.getSource() == sortBox) {
			if (sortBox.getSelectedItem().equals("Score")) {
				solutionsModel.sort(new BogglePathScoreComparator());
			} else if (sortBox.getSelectedItem().equals("Alphabet")) {
				solutionsModel.sort(new BogglePathWordComparator());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == solutionsList && solutionsList.getSelectedIndex() != -1) {
			for (Component c : boardPanel.getComponents()) {
				c.setBackground(Color.WHITE);
			}
			List<Point> path = solutionsList.getSelectedValue().getPoints();
			for (Point p : path) {
				boardPanel.getComponents()[(boardWidth * p.getY()) + p.getX()].setBackground(Color.YELLOW);
			}
		}
	}
}
