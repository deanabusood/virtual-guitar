
public class RUN_GUI {

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new LaunchPage();
			}
		});

	}

}
