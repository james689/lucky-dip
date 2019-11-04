import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.Collections;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

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
	
	private JLabel randomNumberLabel; // displays the random number generated by the random number generator
	private JTextField minNumberTextField, maxNumberTextField; // text fields where user enters the range for the random number generator
	
	public LuckyDipGUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // stack components vertically on top of each other
		
		// lottery panels
		JPanel euroMillionsPanel = new EuroMillionsLotteryPanel("EuroMillions: 5 numbers from 1 to 50 and 2 Lucky Stars from 1 to 12");
		euroMillionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel thunderballPanel = new ThunderballLotteryPanel("Thunderball: 5 numbers from 1 to 39 and 1 Thunderball from 1 to 14");
		thunderballPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel lottoPanel = new LottoLotteryPanel("Lotto: 6 numbers from 1 to 59");
		lottoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// random number generator
		JLabel randomNumberGeneratorLabel = new JLabel("Random Number Generator");
		randomNumberGeneratorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel randomNumberControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
		randomNumberControlPanel.add(new JLabel("From: "));
		minNumberTextField = new JTextField(5);
		randomNumberControlPanel.add(minNumberTextField);
		randomNumberControlPanel.add(new JLabel(" To: "));
		maxNumberTextField = new JTextField(5);
		randomNumberControlPanel.add(maxNumberTextField);
		JButton randomNumberButton = new JButton("Generate");
		randomNumberButton.addActionListener(new RandomNumberButtonListener());
		randomNumberControlPanel.add(randomNumberButton);
		randomNumberLabel = new JLabel("Number: ");
		
		add(euroMillionsPanel);
		add(thunderballPanel);
		add(lottoPanel);
		add(randomNumberGeneratorLabel);
		add(randomNumberControlPanel);
		add(randomNumberLabel);
	}
	
	private class RandomNumberButtonListener implements ActionListener {
		// called when user presses the button to generate a new random number
		public void actionPerformed(ActionEvent ae) {
			try {
				int minNum = Integer.parseInt(minNumberTextField.getText());
				int maxNum = Integer.parseInt(maxNumberTextField.getText());
				if (maxNum < minNum) {
					JOptionPane.showMessageDialog(null, "minimum number must be less than or equal to maximum number");
					return;
				}
				int randomNum = HelperMethods.mathRandom(minNum,maxNum+1);
				randomNumberLabel.setText("Number: " + randomNum);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid number");
			}
		}
	}
	
	// -------------- lottery panels ------------------------------------
	
	// houses all of the stuff LotteryPanel's have in common
	private abstract class LotteryPanel extends JPanel implements ActionListener {
		
		private JLabel titleLabel;
		private JButton luckyDipButton;
		protected List<Integer> numbers;
		protected JLabel[] numberLabels;
		protected JPanel labelsPanel;
		
		public LotteryPanel(String title) {
			setLayout(new BorderLayout());
			titleLabel = new JLabel(title);
			titleLabel.setFont(new Font("Serif", Font.PLAIN, 20));
			luckyDipButton = new JButton("Lucky Dip");
			luckyDipButton.addActionListener(this); // panel listens for its own events
			labelsPanel = new JPanel();
			numbers = generateNumbers();
			Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border margin =  BorderFactory.createEmptyBorder(10,10,10,10);
			Border compoundBorder = new CompoundBorder(outerBorder, margin); // https://stackoverflow.com/questions/22384414/how-can-i-set-the-margin-of-a-jlabel
			// create the number labels
			numberLabels = new JLabel[numbers.size()];
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i] = new JLabel("" + numbers.get(i)); 
				numberLabels[i].setBorder(compoundBorder);
				labelsPanel.add(numberLabels[i]);
			}
			
			add(titleLabel, BorderLayout.NORTH);
			add(luckyDipButton, BorderLayout.WEST);
			add(labelsPanel, BorderLayout.CENTER);
		}
		
		public void actionPerformed(ActionEvent e) {
			numbers = generateNumbers();
			for (int i = 0; i < numberLabels.length; i++) {
				numberLabels[i].setText("" + numbers.get(i));
			}
		}
		
		public abstract List<Integer> generateNumbers(); // generates the random numbers for the lottery. Each subclass
		// must override this method so that it can generate numbers for its particular lottery.
	}
	
	// The unique behaviour a LotteryPanel subclass such as EuroMillionsLotteryPanel has 
	// is in generating its lucky dip numbers (different lotteries have different number ranges etc), 
	// and potentially having unique borders for the number labels e.g. a EuroMillionsLotteryPanel has
	// special borders for the lucky stars.
	private class EuroMillionsLotteryPanel extends LotteryPanel {
		
		public EuroMillionsLotteryPanel(String title) {
			super(title);
			Border luckyStarBorder = new LuckyStarBorder(15);
			// set special borders for the lucky star numbers
			numberLabels[5].setBorder(luckyStarBorder); 
			numberLabels[6].setBorder(luckyStarBorder); 
		}
		
		@Override
		public List<Integer> generateNumbers() {
			List<Integer> mainNumbers = HelperMethods.getRandomNumbers(5,1,50); // 5 numbers from 1-50
			Collections.sort(mainNumbers);
			List<Integer> luckyStars = HelperMethods.getRandomNumbers(2,1,12); // 2 lucky stars from 1-12
			Collections.sort(luckyStars);
			mainNumbers.addAll(luckyStars);
			return mainNumbers;
		}
	}
	
	private class ThunderballLotteryPanel extends LotteryPanel {
		
		public ThunderballLotteryPanel(String title) {
			super(title);
			Border thunderballBorder = new ThunderballBorder(15);
			numberLabels[5].setBorder(thunderballBorder); // set special border for thunderball label
		}
		
		@Override
		public List<Integer> generateNumbers() {
			List<Integer> mainNumbers = HelperMethods.getRandomNumbers(5,1,39); // 5 numbers from 1 to 39
			Collections.sort(mainNumbers);
			List<Integer> thunderball = HelperMethods.getRandomNumbers(1,1,14); // 1 number from 1 to 14
			mainNumbers.addAll(thunderball);
			return mainNumbers;
		}
	}
	
	private class LottoLotteryPanel extends LotteryPanel {
		
		public LottoLotteryPanel(String title) {
			super(title);
		}
		
		@Override
		public List<Integer> generateNumbers() {
			List<Integer> mainNumbers = HelperMethods.getRandomNumbers(6,1,59); // 6 numbers from 1 to 59
			Collections.sort(mainNumbers);
			return mainNumbers;
		}
	}
	
	// -------------- custom borders ------------------------------------
	
	private class ThunderballBorder extends AbstractBorder {
		private int ballRadius = 15;
		
		public ThunderballBorder() {} // use default ball radius
		public ThunderballBorder(int ballRadius) { this.ballRadius = ballRadius; }
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Insets borderInsets = getBorderInsets(c);
			
			/* highlights the border insets area available to draw in (helps with debugging, see pg 409
			Java Swing 2nd Edition)
			g.setColor(Color.BLUE);
			g.fillRect(x,y,width,borderInsets.top); // top border inset
			g.setColor(Color.GREEN);
			g.fillRect(x,y,borderInsets.left,height); // left border inset
			g.setColor(Color.RED);
			g.fillRect(x+width-borderInsets.right,y,borderInsets.right,height); // right border inset
			g.setColor(Color.BLACK);
			g.fillRect(x,y+height-borderInsets.bottom,width,borderInsets.bottom); // bottom border inset
			*/
			
			// draw the outline of a rectangle (this is the border outline) within the BorderInsets region.
			// the border outline will be drawn in the centre of the BorderInsets region
			// (uncomment the code above to visualise this)
			int rectX = borderInsets.left/2;
			int rectY = borderInsets.top/2;
			int rectWidth = c.getWidth() - borderInsets.left/2 - borderInsets.right/2;
			int rectHeight = c.getHeight() - borderInsets.top/2 - borderInsets.bottom/2;
			g.setColor(Color.BLACK);
			g.drawRect(rectX,rectY,rectWidth,rectHeight);
			
			/* old implementation for drawing the border outline:
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
			
			// draw the thunderball
			int thunderballX = width-ballRadius;
			int thunderballY = 0;
			g.setColor(Color.RED);
			g.fillOval(thunderballX,thunderballY,ballRadius,ballRadius); 
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
	
	private class LuckyStarBorder extends AbstractBorder {
		private int starWidth = 15;
		
		public LuckyStarBorder() {} // use default star width
		public LuckyStarBorder(int starWidth) { this.starWidth = starWidth; }
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Insets borderInsets = getBorderInsets(c);
			
			/* highlights the border insets area available to draw in (helps with debugging, see pg 409
			Java Swing 2nd Edition)
			g.setColor(Color.BLUE);
			g.fillRect(x,y,width,borderInsets.top); // top border inset
			g.setColor(Color.GREEN);
			g.fillRect(x,y,borderInsets.left,height); // left border inset
			g.setColor(Color.RED);
			g.fillRect(x+width-borderInsets.right,y,borderInsets.right,height); // right border inset
			g.setColor(Color.BLACK);
			g.fillRect(x,y+height-borderInsets.bottom,width,borderInsets.bottom); // bottom border inset
			*/
			
			// draw the outline of a rectangle (this is the border outline) within the BorderInsets region.
			// the border outline will be drawn in the centre of the BorderInsets region
			// (uncomment the code above to visualise this)
			int rectX = borderInsets.left/2;
			int rectY = borderInsets.top/2;
			int rectWidth = c.getWidth() - borderInsets.left/2 - borderInsets.right/2;
			int rectHeight = c.getHeight() - borderInsets.top/2 - borderInsets.bottom/2;
			g.setColor(Color.BLACK);
			g.drawRect(rectX,rectY,rectWidth,rectHeight);
			
			// draw the lucky star
			int centreX = width - borderInsets.right/2;
			int centreY = borderInsets.top/2;
			int outerCircleRadius = borderInsets.right/2;
			int innerCircleRadius = outerCircleRadius/2;
			int vertexCount = 6;
			StarPolygon star = new StarPolygon(centreX,centreY,outerCircleRadius,innerCircleRadius,vertexCount);
			g.setColor(Color.RED);
			g.fillPolygon(star);
		}
		
		// to keep things simple all border insets have the same size as the star width,
		// even though the left and bottom insets don't need to be star width in size.
		public Insets getBorderInsets(Component c) {
			int top = starWidth;
			int left = starWidth;
			int bottom = starWidth;
			int right = starWidth;
			return new Insets(top,left,bottom,right); 
		}
		
		public Insets getBorderInsets(Component c, Insets i) {
			i.top = i.left = i.bottom = i.right = starWidth;
			return i;
		}
		
		public boolean isBorderOpaque() { return true; }
	}
} // end LuckyDipGUI