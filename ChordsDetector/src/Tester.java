import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Tester {
	
	
	public static void main(String[] args) {
		Double x = 110.0;
		ArrayList<String> blah = null;
		blah = Frequencies.matchFreqToNote(x);
		System.out.println("Actual Frequency: " + x + "\nApproximate Frequency: " + blah.get(0) + "\nPitch: " + blah.get(1) + "\nOctave: " + blah.get(2) + "\nPiano Key: " + blah.get(3));
	}
}