
/**
 * Solution to field and stone problem using both dynamic and
 * brute force algorithms
 * @author Winfield Brooks
 *
 */
public class challenge {

	
	int[][] field;
	
	/**
	 * Creates the 2D array representing the field with stones
	 * dispersed per the command line arguments.
	 * @param theStones
	 */
	
	public challenge(int[][] theStones) {
		field = new int[theStones.length][theStones.length];
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field.length; j++) {
				if(theStones[i][j] == 1) {
					field[i][j] = 0;
				} else {
					field[i][j] = 1;
				}
			}
		}
		findMaxSquare(field);
		bruteForceMaxSquare();
	}
	
	/**
	 * Takes the field created in the constructor and solves for the largest
	 * square not containing any stones.  Prints out size and location of the
	 * top left corner of the square.
	 * @param thefield
	 */
	public void findMaxSquare(int[][] thefield) {
		int max = 0;
		int maxCol = 0;
		int maxRow = 0;
		int sum[][] = new int[thefield.length][thefield.length];
		for(int i = 0; i < sum.length ; i++) {
			sum[i][sum.length - 1] = thefield[i][sum.length - 1];
		}
		for(int i = 0; i < sum.length ; i++) {
			sum[sum.length - 1][i] = thefield[sum.length - 1][i];
		}
		for(int i = sum.length - 2; i >= 0 ; i--) {
			for(int j = sum.length - 2; j >= 0 ; j--) {
				if(thefield[i][j] == 1) {
					sum[i][j] = Math.min(Math.min(sum[i][j+1], sum[i+1][j])
							, sum[i+1][j+1]) + 1;
					if(max < sum[i][j]) {
						max = sum[i][j];
						maxRow = i;
						maxCol = j;
					}
				}
				else {
					sum[i][j] = 0;
				}
			}
		}
		System.out.println("The largest square is size " + max +
				", and its top left corner is located at row: " + maxRow +
				" column: " + maxCol + ".");
	}
	
	
	public boolean bruteForceSpecificSquare(int a, int b, int size) {
		for(int i = a; i < a + size; i++) {
			for(int j = b; j < b + size; j++) {
				if (i > field.length - 1 || j > field.length - 1 || field[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void bruteForceMaxSquare() {
		int max = 0;
		int maxCol = 0;
		int maxRow = 0;
	
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field.length; j++) {
				for(int k = 1; k < field.length; k++) {
					if(bruteForceSpecificSquare(i, j , k) && k > max) {
						max = k;
						maxRow = i;
						maxCol = j;
					}
				}
			}
		}
		System.out.println("The largest square is size " + max +
				", and its top left corner is located at row: " + maxRow +
				" column: " + maxCol + ". Brute force");
	}
	
	/**
	 * Main takes in command line arguments to construct a 2D array of 
	 * the location of the stones to be added 
	 * @param args
	 */
	public static void main (String args[]) {
		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		int[][] stones = new int[n][n];
		for(int i = 2; i < args.length; i+=2) {
			stones[Integer.parseInt(args[i])][Integer.parseInt(args[i+1])] = 1;
		}
		challenge chal = new challenge(stones);
	}
}



