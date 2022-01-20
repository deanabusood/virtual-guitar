import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LaunchPage extends JFrame implements ActionListener{
	
	private JPanel jpMain;
	private JLabel label;
	private JButton button;
	
	public LaunchPage() {
		//Label Properties
		label = new JLabel();	
		label.setText("Tap the button to access the virtual guitar");
		label.setFont(new Font("Lucida Sans Unicode", Font.ROMAN_BASELINE, 15));
		label.setForeground(new Color(0,0,0));
		
		label.setIcon(new ImageIcon(".//res//image//frog.png")); 
		label.setIconTextGap(-15);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.TOP);

		//Button properties
		button = new JButton();
		button.setText("\"the button\"");
		button.setPreferredSize(new Dimension(150,75));
		button.setFocusable(false);
		button.addActionListener(this);
		
		//Panel properties
		jpMain = new JPanel(new FlowLayout());
		jpMain.setBackground(new Color(18,102,100));
		jpMain.add(label);
		jpMain.add(button);
		
		//adding panel to frame
		add(jpMain);
	
		//Frame properties
		setTitle("Welcome!");
		setIconImage(new ImageIcon(".//res//image//neon_guitar.png").getImage()); //change icon of frame
		
		pack();	//replaces setSize()
		setBackground(new Color(18,102,100));
		setResizable(false);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
		
	/*
	 *button press allows user access the second frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) {
			dispose();
			new VirtualGuitar();
		}
	}	
	
}//end of MusicFrame
