import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BoxLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Graphics2D;

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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // stack components vertically on top of each other
		JPanel euroMillionsPanel = new EuroMillionsLotteryPanel("EuroMillions: 5 numbers from 1 to 50 and 2 Lucky Stars from 1 to 12");
		JPanel thunderBallPanel = new ThunderballLotteryPanel("Thunderball: 5 numbers from 1 to 39 and 1 Thunderball from 1 to 14");
		add(euroMillionsPanel);
		add(thunderBallPanel);
	}
	
	private abstract class LotteryPanel extends JPanel implements ActionListener {
		
		private JLabel titleLabel;
		private JButton luckyDipButton;
		
		public LotteryPanel(String title) {
			setLayout(new BorderLayout());
			//setBackground(Color.BLUE);
			titleLabel = new JLabel(title);
			titleLabel.setFont(new Font("Serif", Font.PLAIN, 20));
			luckyDipButton = new JButton("lucky dip");
			luckyDipButton.addActionListener(this); // panel listens for its own events
			add(titleLabel, BorderLayout.NORTH);
			add(luckyDipButton, BorderLayout.WEST);
		}
		
		public void actionPerformed(ActionEvent e) {
			System.out.println("lottery panel button clicked");
		}
	}
	
	private class EuroMillionsLotteryPanel extends LotteryPanel {
		
		private JPanel labelsPanel;
		private List<Integer> numbers;
		private JLabel[] numberLabels;
		
		public EuroMillionsLotteryPanel(String title) {
			super(title);
			Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
			Border margin = new EmptyBorder(10,10,10,10);
			Border compoundBorder = new CompoundBorder(outerBorder, margin); // https://stackoverflow.com/questions/22384414/how-can-i-set-the-margin-of-a-jlabel
			numbers = generateEuroMillionsNumbers();
			labelsPanel = new JPanel();
			//labelsPanel.setBackground(Color.RED);
			numberLabels = new JLabel[7];
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i] = new JLabel("" + numbers.get(i)); // random number between 1 and 50
				numberLabels[i].setBorder(compoundBorder);
				labelsPanel.add(numberLabels[i]);
			}
			add(labelsPanel, BorderLayout.CENTER);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("euro millions panel button clicked");
			numbers = generateEuroMillionsNumbers();
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i].setText("" + numbers.get(i));
			}
		}
		
		private List<Integer> generateEuroMillionsNumbers() {
			List<Integer> mainNumbers = getRandomNumbers(5,1,50);
			List<Integer> luckyStars = getRandomNumbers(2,1,12);
			List<Integer> newList = new ArrayList<>();
			newList.addAll(mainNumbers);
			newList.addAll(luckyStars);
			return newList;
		}
	}
	
	private class ThunderballLotteryPanel extends LotteryPanel {
		
		private JPanel labelsPanel;
		private List<Integer> numbers;
		private JLabel[] numberLabels;
		
		public ThunderballLotteryPanel(String title) {
			super(title);
			
			numbers = generateThunderballNumbers();
			
			Border mainNumberOuterBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border thunderballBorder = new ThunderballBorder(15);
			Border margin = new EmptyBorder(10,10,10,10);
			Border mainNumberCompoundBorder = new CompoundBorder(mainNumberOuterBorder, margin); // https://stackoverflow.com/questions/22384414/how-can-i-set-the-margin-of-a-jlabel
			//Border thunderballCompoundBorder = new CompoundBorder(thunderballBorder, margin);
			
			labelsPanel = new JPanel();
			//labelsPanel.setBackground(Color.RED);
			numberLabels = new JLabel[6];
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i] = new JLabel("" + numbers.get(i)); 
				numberLabels[i].setBorder((i != 5) ? mainNumberCompoundBorder : thunderballBorder);
				labelsPanel.add(numberLabels[i]);
			}
			add(labelsPanel, BorderLayout.CENTER);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("thunderball panel button clicked");
			numbers = generateThunderballNumbers();
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i].setText("" + numbers.get(i));
			}
		}
		
		private List<Integer> generateThunderballNumbers() {
			List<Integer> mainNumbers = getRandomNumbers(5,1,39);
			List<Integer> thunderBall = getRandomNumbers(1,1,14);
			List<Integer> newList = new ArrayList<>();
			newList.addAll(mainNumbers);
			newList.addAll(thunderBall);
			return newList;
		}
	}
	
	private class ThunderballBorder extends AbstractBorder {
		private int ballRadius = 15;
		
		public ThunderballBorder() {} // use default ball radius
		public ThunderballBorder(int ballRadius) { this.ballRadius = ballRadius; }
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);
			Insets borderInsets = getBorderInsets(c);
			
			// highlights the border insets area available to draw in (helps with debugging)
			// The last four parameters of this method can be used to calculate the total screen area of
			// the component (including the border insets) (see pg 409 swing book)
			/*
			g.setColor(Color.BLUE);
			g.fillRect(x,y,width,borderInsets.top); // top border inset
			g.setColor(Color.GREEN);
			g.fillRect(x,y,borderInsets.left,height); // left border inset
			g.setColor(Color.RED);
			g.fillRect(x+width-borderInsets.right,y,borderInsets.right,height); // right border inset
			g.setColor(Color.BLACK);
			g.fillRect(x,y+height-borderInsets.bottom,width,borderInsets.bottom); // bottom border inset
			*/
			
			g.setColor(Color.BLACK);
			// draw the outline of a rectangle within the BorderInsets region
			// the width of the rectangle will be the width of the component minus
			// half of the left border inset and half of the right border inset.
			// similarly, the height of the rectangle will be the height of the component
			// minus half of the top and bottom border insets.
			int rectX = borderInsets.left/2;
			int rectY = borderInsets.top/2;
			int rectWidth = c.getWidth() - borderInsets.left/2 - borderInsets.right/2;
			int rectHeight = c.getHeight() - borderInsets.top/2 - borderInsets.bottom/2;
			g.drawRect(rectX,rectY,rectWidth,rectHeight);
			
			/* old implementation for drawing the border lines:
			int topLineYPos = y+(borderInsets.top/2);
			int rightLineXPos = width-(borderInsets.right/2);
			int leftLineXPos = x+(borderInsets.left/2);
			int bottomLineYPos = height-(borderInsets.bottom/2);
			g.setColor(Color.WHITE);
			g.drawLine(leftLineXPos,topLineYPos,width,topLineYPos); // draw top line
			g.drawLine(rightLineXPos,y,rightLineXPos,bottomLineYPos); // draw right line
			g.drawLine(leftLineXPos,topLineYPos,leftLineXPos,bottomLineYPos); // draw left line
			g.drawLine(leftLineXPos,bottomLineYPos,rightLineXPos,bottomLineYPos); // draw bottom line
			*/
			
			int thunderballX = width-ballRadius;
			int thunderballY = 0;
			g.setColor(Color.RED);
			g.fillOval(thunderballX,thunderballY,ballRadius,ballRadius); // draw thunderball
		}
		
		// to keep things simple all border insets have the same size as the ball radius,
		// even though the left and bottom insets don't need to be ball radius in size.
		public Insets getBorderInsets(Component c) {
			int top = ballRadius;
			int left = ballRadius;
			int bottom = ballRadius;
			int right = ballRadius;
			return new Insets(top,left,bottom,right); 
		}
		
		public Insets getBorderInsets(Component c, Insets i) {
			i.top = i.left = i.bottom = i.right = ballRadius;
			return i;
		}
		
		public boolean isBorderOpaque() { return true; }
	}
	
	// returns a list containing listSize unique random numbers, each within
	// the range minBound (inclusive) to maxBound (inclusive)
	private List<Integer> getRandomNumbers(int listSize, int minBound, int maxBound) {
		List<Integer> randomNumbersList = new ArrayList<>();
		while (randomNumbersList.size() < listSize) {
			int randomNum = mathRandom(minBound,maxBound+1);
			if (!randomNumbersList.contains(randomNum)) {
				randomNumbersList.add(randomNum);
			}
		}
		return randomNumbersList;
	}
	
	// returns a random number between min(inclusive) and max(exclusive)
    // equivalent to lua math.random(min,max) except the max is also inclusive in lua
    public static int mathRandom(int min, int max) {
        Random r = new Random();
        int result = r.nextInt(max - min) + min;
        return result;
    }
	
}