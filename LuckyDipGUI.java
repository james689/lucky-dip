import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class LuckyDipGUI extends JPanel {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Lucky Dip");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LuckyDipGUI content = new LuckyDipGUI();
		window.setContentPane(content);
		window.pack();
		window.setVisible(true);
	}
	
	// --------------------------------------------------
	
	public LuckyDipGUI() {
		setPreferredSize(new Dimension(300,300));
	}
	
}