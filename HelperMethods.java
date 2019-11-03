import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class HelperMethods {
	// returns a list containing listSize unique random numbers, each within
	// the range minBound (inclusive) to maxBound (inclusive)
	public static List<Integer> getRandomNumbers(int listSize, int minBound, int maxBound) {
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