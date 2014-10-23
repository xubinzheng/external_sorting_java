package hw2;

import java.util.Random;

/**
 * Simulates a tape drive
 */
public class TapeDrive {

    private int[] tape;
    private int currentPos = 0;

    public TapeDrive(int capacity) {
        tape = new int[capacity];
    }

    public void write(int i) {
        tape[currentPos] = i;
        currentPos = (currentPos + 1) % tape.length;
    }

    public int read() {
        int i = tape[currentPos];
        currentPos = (currentPos + 1) % tape.length;
        return i;
    }

    public void reset() {
        currentPos = 0;
    }

    /**
     * Create a new TapeDrive that can hold `capacity` numbers, fill it with random numbers, and return it.
     * The numbers must be random in the full integer range.
     */
    public static TapeDrive generateRandomTape(int capacity) {
        TapeDrive randomTape = new TapeDrive(capacity);
        for(int i=0 ; i<=capacity ; i++){
        	Random rand = new Random();
        	int randomNum =rand.nextInt();
        	randomTape.write(randomNum);
        }
		return randomTape;
    }
    
    
    public class Tester {
    	public void main(String args[]){ 		
    		System.out.println(generateRandomTape(10).read());
    	}
    }

}
