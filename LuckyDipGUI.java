import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
		JPanel euroMillionsPanel = new JPanel(new BorderLayout());
		//euroMillionsPanel.setBackground(Color.BLUE);
		
		JLabel euroMillionsLabel = new JLabel("EuroMillions: 5 numbers from 1 to 50 and 2 Lucky Stars from 1 to 12");
		euroMillionsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		
		JPanel labelsPanel = new JPanel();
		//labelsPanel.setBackground(Color.RED);
		
		Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		Border margin = new EmptyBorder(10,10,10,10);
		Border compoundBorder = new CompoundBorder(outerBorder, margin);
		
		JLabel label1 = new JLabel("1");
		label1.setBorder(compoundBorder);
		JLabel label2 = new JLabel("59");
		label2.setBorder(compoundBorder);
		JLabel label3 = new JLabel("39");
		label3.setBorder(compoundBorder);
		JLabel label4 = new JLabel("50");
		label4.setBorder(compoundBorder);
		JLabel label5 = new JLabel("22");
		label5.setBorder(compoundBorder);
		JLabel label6 = new JLabel("1");
		label6.setBorder(compoundBorder);
		JLabel label7 = new JLabel("10");
		label7.setBorder(compoundBorder);
		labelsPanel.add(label1);
		labelsPanel.add(label2);
		labelsPanel.add(label3);
		labelsPanel.add(label4);
		labelsPanel.add(label5);
		labelsPanel.add(label6);
		labelsPanel.add(label7);
		
		euroMillionsPanel.add(euroMillionsLabel, BorderLayout.NORTH);
		euroMillionsPanel.add(new JButton("lucky dip"), BorderLayout.WEST);
		euroMillionsPanel.add(labelsPanel, BorderLayout.CENTER);
		
		add(euroMillionsPanel);
	}
	
}