import java.util.Scanner;

public class Main {
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
	while(true) {
		// reference string input
		System.out.print("Enter the reference string size : ");
		int[] referenceString = new int[input.nextInt()];
		System.out.print("Enter the reference string : ");
		for(int i = 0; i < referenceString.length; i++)
			referenceString[i] = input.nextInt();
		
		// Frame input
		System.out.print("Enter frame size : ");
		int frameSize = input.nextInt();
		int[] frame = new int[frameSize];
		for(int i = 0; i  < frameSize; i++)
			frame[i] = -1;
		
		// Ask user for choice
		System.out.print("Enter a choice : 1. LRU 2. Optimal : ");
		int choice = input.nextInt();
		if(choice !=1 && choice != 2) {
			System.out.println("Incorrect choice");
			return;
		}
		
		int numberOfFaults = 0;
		
		// Code for replacement
		for(int i = 0; i < referenceString.length; i++) {
			int currentPage = referenceString[i];
			// Find Index of current page in frame
			int index = getIndexOfValue(frame, currentPage);
			if(index == -1) { // Page Miss
				numberOfFaults++;
				int emptyIndex = getIndexOfValue(frame, -1);
				if(emptyIndex != -1) {
					// Empty slot found
					frame[emptyIndex] = currentPage;
				}else {
					// Empty slot not found, Apply replacement policy
					int replacementIndex;
					if(choice == 1)
						replacementIndex = getLRUReplacementIndex(frame, referenceString, i);
					else
						replacementIndex = getOptimalReplacementIndex(frame, referenceString, i);
					frame[replacementIndex] = currentPage;
				}
			}
			
			// Display the output
			System.out.print(referenceString[i] + "\t");
			for(int page : frame)
				System.out.print(page + "\t");
			if(index == -1)
				System.out.print("*\t");
			System.out.println();
		}
		System.out.println("Total number of faults : " + numberOfFaults);
		System.out.println();
	}
	}
	
	// Returns -1 for not found
	private static int getIndexOfValue(int[] array, int value) {
		int index = -1;
		for(int i = 0; i < array.length; i++) {
			if(array[i] == value) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	// Get LRU replacement Index
	private static int getLRUReplacementIndex(int[] frame, int[] referenceString, int index) {
		int[] distances = new int[frame.length];
		for(int i = 0; i < distances.length; i++)
			distances[i] = referenceString.length;
		
		for(int i = 0; i < frame.length; i++) {
			for(int j = index-1; j >= 0; j--) {
				if(referenceString[j] == frame[i]) {
					distances[i] = index-j;
					break;
				}
			}
		}
		
		return getMaxIndex(distances);
	}
	
	// Get Optimal replacement Index
	private static int getOptimalReplacementIndex(int[] frame, int[] referenceString, int index) {
		int[] distances = new int[frame.length];
		for(int i = 0; i < distances.length; i++)
			distances[i] = referenceString.length;
		
		for(int i = 0; i < frame.length; i++) {
			for(int j = index+1; j < referenceString.length; j++) {
				if(referenceString[j] == frame[i]) {
					distances[i] = j-index;
					break;
				}
			}
		}
		
		return getMaxIndex(distances);
	}
	
	// Max index
	private static int getMaxIndex(int[] array) {
		int max = array[0];
		int index = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] > max) {
				max = array[i];
				index = i;
			}
		}
		return index;
	}
}