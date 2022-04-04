package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class VirtualGuitar extends JFrame {
	private final String[] guitarChords = { ".//res//audio//am7.wav", ".//res//audio//e7.wav", ".//res//audio//c7.wav",
			".//res//audio//f7.wav" };
	private String filePath;

	private JPanel mainPanel;
	private SampleMusicPlayer music_player;
	private CustomPresets custom_presets;

	private JLabel mainLabel;
	private final String mainLabelText = "Below are 4 sample guitar chord sounds to mess around with.<br>"
			+ "Feel free to tap the buttons or press the number associated with them to play the audio!";

	public VirtualGuitar() {
		// Label properties
		mainLabel = new JLabel("<html><div style='text-align: center;'>" + mainLabelText + "</div></html>", SwingConstants.CENTER);
		mainLabel.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 15));
		mainLabel.setForeground(new Color(0, 0, 0));

		mainLabel.setIcon(new ImageIcon(".//res//image//panda.png"));
		mainLabel.setIconTextGap(-25);
		mainLabel.setHorizontalTextPosition(JLabel.CENTER);
		mainLabel.setVerticalTextPosition(JLabel.BOTTOM);

		// initialize instance of inner panel classes
		music_player = new SampleMusicPlayer();
		addKeyListener(music_player);

		custom_presets = new CustomPresets();

		// initialize MAIN panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		// add other class panels to main panel
		mainPanel.add(mainLabel, BorderLayout.NORTH);
		mainPanel.add(music_player, BorderLayout.CENTER);
		mainPanel.add(custom_presets, BorderLayout.SOUTH);

		// add main panel to the frame
		add(mainPanel);

		// Frame properties
		setTitle("Virtual Guitar");
		setIconImage(new ImageIcon(".//res//image//neon_guitar.png").getImage());
		setSize(800, 420);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}
	/*
	 * helper method shared with both inner classes to output audio
	 */
	private void playAudio(String filePath) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filePath)));
			clip.start();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/*
	 * CLASS
	 * handles sample buttons/key press
	 */
	private class SampleMusicPlayer extends JPanel implements ActionListener, KeyListener {
		private JButton[] buttons;
		private final int NUM_BUTTONS = 5;

		public SampleMusicPlayer() {
			setBackground(new Color(18, 102, 100));
			displayButtons();
		}

		private void displayButtons() {
			buttons = new JButton[NUM_BUTTONS]; // initialized
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new JButton();
				buttons[i].setForeground(Color.BLACK);
				buttons[i].setFocusable(false);
				buttons[i].addActionListener(this);
				buttons[i].setEnabled(true);
				this.add(buttons[i]);

			}
			buttons[0].setText("(1) Am7");
			buttons[1].setText("(2) E7");
			buttons[2].setText("(3) C7");
			buttons[3].setText("(4) F7");
			buttons[4].setText("(5) Random 1-4");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttons[0]) { // Am7
				filePath = guitarChords[0];
				playAudio(filePath);
			}
			if (e.getSource() == buttons[1]) { // E7
				filePath = guitarChords[1];
				playAudio(filePath);
			}
			if (e.getSource() == buttons[2]) { // C7
				filePath = guitarChords[2];
				playAudio(filePath);
			}
			if (e.getSource() == buttons[3]) { // F7
				filePath = guitarChords[3];
				playAudio(filePath);
			}
			if (e.getSource() == buttons[4]) { // Random
				filePath = guitarChords[new Random().nextInt(guitarChords.length)];
				playAudio(filePath);
			}

		} // end of actionPerformed method

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_1) { // Am7
				filePath = guitarChords[0];
				playAudio(filePath);
			}
			if (e.getKeyChar() == KeyEvent.VK_2) { // E7
				filePath = guitarChords[1];
				playAudio(filePath);
			}
			if (e.getKeyChar() == KeyEvent.VK_3) { // C7
				filePath = guitarChords[2];
				playAudio(filePath);
			}
			if (e.getKeyChar() == KeyEvent.VK_4) { // F7
				filePath = guitarChords[3];
				playAudio(filePath);
			}
			if (e.getKeyChar() == KeyEvent.VK_5) { // Random
				filePath = guitarChords[new Random().nextInt(guitarChords.length)];
				playAudio(filePath);
			}
		}
		// UNNEEDED METHODS
		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	} // end of SampleMusicPlayer
	/*
	 * CLASS
	 * handles custom preset buttons
	 */
	private class CustomPresets extends JPanel implements ActionListener {
		private JTextField presetName;
		private JTextField removeButtonName;

		private JButton[] mainButtons;

		private HashMap<String, JButton> dynamicPresetButtons = new HashMap<>();
		private final int MAX_PRESETS = 3;
		private int presetCount = 0;

		private JButton[] presetChordOptions = new JButton[4]; // Am7, E7, C7, F7
		private ArrayList<String> userInputPresetChords = new ArrayList<>(); 

		private int presetFileNum = 1;
		private int buttonNum = 1;

		public CustomPresets() {
			displayPresetButtons();
			setBackground(new Color(18, 102, 100));
		}
		/*
		 * user input in multiple fields to create a preset
		 */
		private void displayCustomPresetPane() throws UnsupportedAudioFileException, IOException {
			presetName = new JTextField();
			Object[] message = { "Name your custom preset", presetName };

			if (presetCount < MAX_PRESETS) {
				int option = JOptionPane.showConfirmDialog(null, message, "Create a Preset",
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					while (true) {
						if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
							break;
						} else if (presetName.getText().isEmpty() || dynamicPresetButtons.containsKey(presetName.getText())) {
							handleInvalidInput(message, option);
						} else {
							createPreset(presetName.getText());

							if (presetCount == 3)
								disableAddButton();
							if (presetCount >= 1)
								makeRemoveButtonVisible();
							else {
								toggleRemoveButton(true); // disabled since no presets exist
							}
						}
						break; // end of while
					}
				}
			}
		}
		/*
		 * HELPER METHODS
		 */
		private void handleInvalidInput(Object[] message, int option) {
			if (presetName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(mainPanel, "One or more fields are empty", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else if (dynamicPresetButtons.containsKey(presetName.getText())) { // checks if there's already a preset
																					// with the same name
				JOptionPane.showMessageDialog(mainPanel, "Please input a preset name not already created", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		private void prepareNextPreset() {
			userInputPresetChords.clear();
			presetFileNum++;
		}

		private void createPreset(String name) throws UnsupportedAudioFileException, IOException {
			displayPresetChordOptions();
			addButton(name);
			prepareNextPreset();
		}

		private void makeRemoveButtonVisible() {
			mainButtons[1].setVisible(true);
			mainButtons[1].setEnabled(true);
		}

		private void disableAddButton() {
			// if max presets are reached
			mainButtons[0].setEnabled(false);
			mainButtons[0].setText("You've reached the limit");
		}

		private void toggleRemoveButton(boolean b) {
			if (b == true) {
				mainButtons[1].setVisible(false);
			}
			else{
				mainButtons[1].setVisible(true);
			}
		}
		/*
		 * displays the chords the user is able to create a sequence out of
		 */
		private void displayPresetChordOptions() {
			initializePresetOptions();

			int options = JOptionPane.showOptionDialog(null,
					"<html><div style='text-align: center;'>Tap the chords you'd like to be played in order.<br> When finished, press the X in the upper right corner to proceed</div></html>",
					"Pick your chords", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					presetChordOptions, presetChordOptions[0]);

			while (true) {
				if (options == JOptionPane.CANCEL_OPTION || options == JOptionPane.CLOSED_OPTION) {
					break;
				}
			}
		}
		/*
		 * iterates through the user input chord sequence stored in an array list to
		 * merge audio files
		 */
		@SuppressWarnings({ "resource" })
		private void createCustomAudioFile() throws UnsupportedAudioFileException, IOException {
			AudioInputStream clip = null;
			for (String path : userInputPresetChords) {
				if (clip == null) {
					clip = AudioSystem.getAudioInputStream(new File(path));
					continue;
				}
				AudioInputStream nextClip = AudioSystem.getAudioInputStream(new File(path));
				AudioInputStream appendedFiles = new AudioInputStream(new SequenceInputStream(clip, nextClip), clip.getFormat(), clip.getFrameLength() + nextClip.getFrameLength());
				clip = appendedFiles;
			}
			AudioSystem.write(clip, AudioFileFormat.Type.WAVE, new File(".//res//user_created_files//preset" + String.valueOf(presetFileNum) + ".wav"));
			clip.close();
		}
		/*
		 * Used in JOptionPane to show chord options for custom preset
		 */
		private void initializePresetOptions() {
			for (int i = 0; i < presetChordOptions.length; i++) {
				presetChordOptions[i] = new JButton();
				presetChordOptions[i].setForeground(Color.black);
				presetChordOptions[i].addActionListener(this);
				presetChordOptions[i].setEnabled(true);
				presetChordOptions[i].setFocusable(false);
				this.add(presetChordOptions[i]);
			}
			presetChordOptions[0].setText("Am7");
			presetChordOptions[1].setText("E7");
			presetChordOptions[2].setText("C7");
			presetChordOptions[3].setText("F7");
		}
		/*
		 * removes only existing preset buttons based on user input
		 */
		private void removePresetButtons() {
			removeButtonName = new JTextField();
			Object[] message = { "Which preset would you like to remove?", removeButtonName };

			int option = JOptionPane.showConfirmDialog(null, message, "Remove a Preset", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				while (true) {
					if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
						break;
					} else if (removeButtonName.getText().isEmpty() || !dynamicPresetButtons.containsKey(removeButtonName.getText())) {																		
						JOptionPane.showMessageDialog(mainPanel, "Please input an already existing preset name", "Warning", JOptionPane.WARNING_MESSAGE);
						option = JOptionPane.showConfirmDialog(null, message, "Remove a Preset", JOptionPane.OK_CANCEL_OPTION);
					} else {
						// removes button existing in map
						removeButton(removeButtonName.getText());
						// makes create a preset button visible after making space
						if (presetCount < 1) {
							mainButtons[1].setVisible(false);
						}
						break;
					}
				}
			}
		}
		/*
		 * add/remove preset buttons and clear directory
		 */
		private void displayPresetButtons() {
			mainButtons = new JButton[3]; // initialized the add/remove/CLEAR FILE buttons

			for (int i = 0; i < mainButtons.length; i++) {
				mainButtons[i] = new JButton();
				mainButtons[i].setFocusable(false);
				mainButtons[i].addActionListener(this);
				mainButtons[i].setEnabled(true);
				add(mainButtons[i]);
			}

			// create preset button
			mainButtons[0].setText("Create a Preset");
			mainButtons[0].setForeground(Color.BLUE);

			// remove preset button
			mainButtons[1].setText("Remove a Preset");
			mainButtons[1].setForeground(Color.RED);
			mainButtons[1].setVisible(false);

			// clear files button
			mainButtons[2].setText("Clear created Audio Files");
			mainButtons[2].setForeground(Color.DARK_GRAY);
		}

		// helper method used to add the custom preset with a unique action listener
		private void addButton(String presetName) throws UnsupportedAudioFileException, IOException {
			JButton newButton = new JButton(presetName);
			newButton.setVisible(true);
			newButton.setEnabled(true);
			newButton.setForeground(Color.BLACK);
			newButton.setFocusable(false);

			createCustomAudioFile();
			final int buttonIndex = buttonNum++; //keeps track of each button with custom audio file

			newButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					playAudio(".//res//user_created_files//preset" + String.valueOf(buttonIndex) + ".wav");
				}
			});
			dynamicPresetButtons.put(presetName, newButton); // store into hash map to access later (remove)
			add(newButton);
			++presetCount;
		}

		// helper method used to remove custom presets and re-enables "create a preset" button
		private void removeButton(String presetName) {
			JButton removePreset = dynamicPresetButtons.get(presetName);
			dynamicPresetButtons.remove(presetName); // removes button from map
			remove(removePreset);

			mainButtons[0].setEnabled(true);
			mainButtons[0].setText("Create a Preset");
			--presetCount;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == mainButtons[0]) { // ADD BUTTON
				try {
					displayCustomPresetPane();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				revalidate();
				repaint();
			}
			if (e.getSource() == mainButtons[1]) { // REMOVE BUTTON
				removePresetButtons();
				revalidate();
				repaint();
			}
			if (e.getSource() == mainButtons[2]) { // CLEAR BUTTON
				if (presetCount >= 1) {
					JOptionPane.showMessageDialog(mainPanel, "Please remove any presets before proceeding", "Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					deleteDirectory();
				}
			}

			// buttons to store the sequence the user input for their custom preset
			if (e.getSource() == presetChordOptions[0]) { // am7
				userInputPresetChords.add(".//res//audio//am7.wav");
			}
			if (e.getSource() == presetChordOptions[1]) { // e7
				userInputPresetChords.add(".//res//audio//e7.wav");
			}
			if (e.getSource() == presetChordOptions[2]) { // C7
				userInputPresetChords.add(".//res//audio//c7.wav");
			}
			if (e.getSource() == presetChordOptions[3]) { // F7
				userInputPresetChords.add(".//res//audio//f7.wav");
			}
		}
		/*
		 * clear created Audio Files
		 */
		private void deleteDirectory() {
			File directory = new File(".//res//user_created_files");

			for (File file : directory.listFiles()) {
				file.delete();
			}
		}
		
	} // end of CustomPresets (inner class)
} // end of MusicPlayer (Frame)