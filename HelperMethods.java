import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class HelperMethods {
	// returns a list containing listSize unique random numbers, each number falling within
	// the range minBound (inclusive) to maxBound (inclusive)
	public static List<Integer> getRandomNumbers(int listSize, int minBound, int maxBound) {
		
		int maxPossibleUniqueNumbers = Math.abs(maxBound-minBound)+1;
		if (maxPossibleUniqueNumbers < listSize) {
			throw new IllegalArgumentException("listSize is greater than the number of possible unique numbers between minBound and maxBound");
		}
		
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
    public static int mathRandom(int min, int max) {
        Random r = new Random();
        int result = r.nextInt(max - min) + min;
        return result;
    }
}