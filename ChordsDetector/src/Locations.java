import java.util.ArrayList;
import java.util.Collections;

public class Locations {

	// Calculate and store the mean of every x samples.
	public static ArrayList<Integer[]> quantize(short[] pcmArray, int x) {
		int limit = x;
		ArrayList<Integer[]> means = new ArrayList<Integer[]>();
		
		for (int i = 0; i < pcmArray.length; i += limit) {
			ArrayList<Integer> temp1 = new ArrayList<Integer>();
			int range = 0;
			if (pcmArray.length - i < limit) {
				range = pcmArray.length - i;	
			}
			else {range = limit;}
			for (int j = 0; j < range; j++) {
				temp1.add((int)Math.abs(pcmArray[i + j]));
			}
			Integer[] temp2 = new Integer[2];
			temp2[0] = i;
			temp2[1] = (findMean(temp1));
			means.add(temp2);
		}
		return means;
	}
	
	public static int findMean(ArrayList<Integer> arr) {
		int sum = 0;
		for (int i = 0; i < arr.size(); i++) {
			sum += arr.get(i);
		}
		sum /= arr.size();
		return sum;
	}
	
	public static boolean powerOfTwoCheck(int x) {
		if (x <= 0) {return false;}
		if ((x == 1) || (x == 2)) {return true;}
		if ((x / 2) % 2 != 0) {return false;}
		else {return powerOfTwoCheck(x / 2);}
	}
	
	public static int changeToPowerOfTwo(int x) {
		if (powerOfTwoCheck(x) == false) {
			// Convert to binary, change all 1s to 0 except the very first, convert back to decimal. 
			String binx = Integer.toBinaryString(x);
			for (int i = 1; i < binx.length(); i++) {
				if (binx.substring(i, i + 1).equals("1")) {
					binx = binx.substring(0, i) + "0" + binx.substring(i + 1);
				}
			}
			x = Integer.parseInt(binx, 2);
			return x;
		}
		else {return x;}
	}
	
	// Return the locations that are the highest distance from the previous point. 
    public static ArrayList<Integer> highestDiffs(ArrayList<Integer[]> arr, int maxAmp) {
		//int num = x;
		ArrayList<Integer[]> allDiffs = arr;
		ArrayList<Integer[]> highDiffs = new ArrayList<Integer[]>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		// Copy the input ArrayList and add another element to each array. 
		for (int i = 0; i < allDiffs.size(); i++) {
			Integer[] a = new Integer[3];
			a[0] = allDiffs.get(i)[0];
			a[1] = allDiffs.get(i)[1];
			a[2] = 0;
			highDiffs.add(a);
		}
		allDiffs.clear();
		
		// Change the last element of each array to the difference between the absolute values of i[1] and (i - 1)[1]. 
		for (int i = 1; i < highDiffs.size(); i++) {
			int a = Math.abs(highDiffs.get(i - 1)[1]);
			int b = Math.abs(highDiffs.get(i)[1]);
			highDiffs.get(i)[2] = b - a;
		}
		
		// Sort the arrays by highest last element. 
		Collections.sort(highDiffs, new ColumnComparatorI(2));
		Collections.reverse(highDiffs);
		
		int highestPeak = highDiffs.get(0)[2];
		double min = highestPeak * (maxAmp / 100);
		int minPercent = (int) min;
		
		Collections.sort(highDiffs, new ColumnComparatorI(0));
		
		int buffer = 8192; 							//
		result.add(0);
		for (int i = 0; i < highDiffs.size(); i++) {
			if ((highDiffs.get(i)[2] >= minPercent) && (highDiffs.get(i)[0] - result.get(result.size() - 1) >= buffer)) {
				result.add(highDiffs.get(i)[0]);
			}
		}
		//result.remove(0);
		
		/*
		// Add the x highest elements to the result ArrayList.
		for (int i = 0; i < num; i++) {
			Integer a = highDiffs.get(i)[0];
			result.add(a);
		}
		
		// Sort the result ArrayList.
		Collections.sort(result); */
		return result;
	}
}
