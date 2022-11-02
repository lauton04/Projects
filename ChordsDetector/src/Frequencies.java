import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Frequencies {
	
	public static ArrayList<Double[]> detectPeaks(Complex[] data, double freqResolution) {
		ArrayList<Double[]> peakFreqs = new ArrayList<Double[]>();
		double peakF = 0.0;
		double peakAmp = 0.0;
		boolean increasing = true;
		
		for (int i = 0; i * freqResolution < data.length / 2; i++) {
			if (data[i].abs() >= peakAmp) {
				peakF = i * freqResolution;
				peakAmp = data[i].abs();
				increasing = true;
			}
			// Store value of the input array whenever it stops increasing.
			else if ((data[i].abs() < peakAmp) && (increasing == true)) {
				Double[] temp = new Double[2];
				temp[0] = peakF;
				temp[1] = (Double) peakAmp;
				peakFreqs.add(temp);
				peakF = i * freqResolution;
				peakAmp = (int) data[i].abs();
				increasing = false;
			}
			else {
				peakF = i * freqResolution;
				peakAmp = (int) data[i].abs();
			}
		}
		return peakFreqs;
	}
	
	public static ArrayList<Double> highestDiffs(ArrayList<Double[]> arr, int x) {
		int num = x;
		ArrayList<Double[]> allDiffs = arr;
		ArrayList<Double[]> highDiffs = new ArrayList<Double[]>();
		ArrayList<Double> result = new ArrayList<Double>();
		
		// Copy the input ArrayList and add another element to each array. 
		for (int i = 0; i < allDiffs.size(); i++) {
			Double[] a = new Double[3];
			a[0] = allDiffs.get(i)[0];
			a[1] = allDiffs.get(i)[1];
			a[2] = 0.0;
			highDiffs.add(a);
		}
		allDiffs.clear();
		
		// Change the last element of each array to the difference between the absolute values of i[1] and (i - 1)[1]. 
		for (int i = 1; i < highDiffs.size(); i++) {
			double a = Math.abs(highDiffs.get(i - 1)[1]);
			double b = Math.abs(highDiffs.get(i)[1]);
			highDiffs.get(i)[2] = b - a;
		}
		
		// Sort the arrays by highest last element. 
		Collections.sort(highDiffs, new ColumnComparatorD(2));
		Collections.reverse(highDiffs);
		
		// Add the x highest elements to the result ArrayList.
		if (num > highDiffs.size()) {
			num = highDiffs.size();
		}
		
		for (int i = 0; i < num; i++) {
			Double a = highDiffs.get(i)[0];
			result.add(a);
		}
		
		// Sort the result ArrayList.
		Collections.sort(result);
		return result;
	}
	
	// Test peaks to see which one is the fundamental
	public static ArrayList<Double> findFundamentals(ArrayList<Double> totalFreqs, int n) {
		// find how many harmonics are present for each frequency
		// keep the frequencies with n or more harmonics in an ArrayList
		ArrayList<Double> fundamentals = new ArrayList<Double>();
		for (int i = 0; i < totalFreqs.size() - n; i++) {								// candidate fundamentals
			int count = 1;
			ArrayList<Double> mult = multiples(totalFreqs.get(i), n);
			int j = 0;
			while (j < mult.size() && count < n) {										// multiples of candidate fundamentals
				int k = i + 1;
				while (k < totalFreqs.size() && count < n) {							// comparisons to higher harmonics
					if (Math.abs(mult.get(j) - totalFreqs.get(k)) / mult.get(j) < .03) {
						double test = totalFreqs.get(k) / mult.get(j);
						test = cutBeforeDecimalPt(test);
						if (withinRange(test)) {
							count += 1;
							//System.out.println(totalFreqs.get(i) + ", " + mult.get(j) + ", " + totalFreqs.get(k) + ", " + test + ", " + count);
							break;
						}
					}
					k++;
				}
				j++;
			}
			//System.out.println(count);
			if (count >= n) fundamentals.add(totalFreqs.get(i));
		}
		
		//Check for octaves and delete them.
		if (fundamentals.size() <= 1) {
			return fundamentals;
		}
		for (int i = fundamentals.size() - 1; i > 0; i--) {
			
			double test = fundamentals.get(i) / fundamentals.get(i - 1);
			if (withinRange(test)) {
				fundamentals.remove(i);
			}
		}
		return fundamentals;
	}
	
	/*
	 * Do this with the strings instead
	public static void repeatsCheck(ArrayList<ArrayList<Double>> arr) {
		//check for repeat chords and delete them
		if (arr.size() <= 1) {
			return;
		} 
		for (int i = 0; i < arr.size() - 1; i++) {
			double test = arr.get(i) / arr.get(i - 1);
			if (withinRange(test)) {
				arr.remove(i);
			}
			if (!(arr.size() > 1)) {
				break;
			}
		}
	}
	*/
	
	public static ArrayList<Double> multiples(double num1, int n) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = 2; i < n + 1; i++) {
			result.add(num1 * i);
		}
		return result;
	}
	
	
	public static boolean withinRange(double num1) {
		double test = num1;
		if (test <= .028873105 || test >= 0.971126895) {
			return true;
		}
		else {return false;}
	}
	
	public static double cutBeforeDecimalPt(double x) {
		String str = Double.toString(x);
		int decPt = str.indexOf(".");
		str = str.substring(decPt);
		double result = Double.parseDouble(str);
		return result;
	}
	
	public static ArrayList<String> matchFundamentals(ArrayList<Double> fnds) {
		ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		ArrayList<String> result = new ArrayList<String>();
		
		for (int i = 0; i < fnds.size(); i++) {
			temp.add(matchFreqToNote(fnds.get(i)));
		}
		if (fnds.size() == 2) {
			double num = Double.parseDouble(temp.get(0).get(0));
			double denom = Double.parseDouble(temp.get(1).get(0));
			double ratio = num / denom;
			result.add(temp.get(0).get(0) + ", " + temp.get(1).get(0));
			result.add(temp.get(0).get(1) + ", " + temp.get(1).get(1));
			result.add(temp.get(0).get(2) + ", " + temp.get(1).get(2));
			result.add(temp.get(0).get(3) + ", " + temp.get(1).get(3));
			ArrayList<String> temp2 = matchRatioTo2Chord(ratio);
			for (int i = 0; i < temp2.size(); i++) {
				result.add(temp2.get(i));
			}
		}
		else if (fnds.size() == 3) {
			double num1 = Double.parseDouble(temp.get(0).get(0));
			double denom1 = Double.parseDouble(temp.get(1).get(0));
			double num2 = denom1;
			double denom2 = Double.parseDouble(temp.get(2).get(0));
			double ratio1 = num1 / denom1;
			double ratio2 = num2 / denom2;
			result.add(temp.get(0).get(0) + ", " + temp.get(1).get(0) + ", " + temp.get(2).get(0));
			result.add(temp.get(0).get(1) + ", " + temp.get(1).get(1) + ", " + temp.get(2).get(1));
			result.add(temp.get(0).get(2) + ", " + temp.get(1).get(2) + ", " + temp.get(2).get(2));
			result.add(temp.get(0).get(3) + ", " + temp.get(1).get(3) + ", " + temp.get(2).get(3));
			ArrayList<String> temp2 = matchRatiosTo3Chord(ratio1, ratio2);
			for (int i = 0; i < temp2.size(); i++) {
				result.add(temp2.get(i));
			}
		}
		return result;
	}
	
	// function to match fundamental frequencies to notes
	public static ArrayList<String> matchFreqToNote(double freq) {
		//match the frequency to the notes/frequencies table.
		//round to the note within 2.8% or less
		ArrayList<String> noteData = new ArrayList<String>();
		String line = "";
	    String cvsSplitBy = ",";
		String csvFile = "C:/Users/Tony/Desktop/Organization Binder/2 Science & Math/1 Computer Science/Programming Languages/Java/Workspace/ChordsDetector/src/table1.csv";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] read = line.split(cvsSplitBy);
                double test = Double.parseDouble(read[0]) / freq;
                if (withinRange(test)) {
                	for (int i = 0; i < read.length; i++) {
                		noteData.add(read[i]);
                	}
	                break;
                }
            }
	    } catch (FileNotFoundException e) {
	            e.printStackTrace();
	    } catch (IOException e) {
	            e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
	    			br.close();
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }
	        }
	    }
		return noteData;
	}
	
	// Match note ratio to 2-note chord.
	public static ArrayList<String> matchRatioTo2Chord(double rat) {
		ArrayList<String> data = new ArrayList<String>();
		String line = "";
	    String cvsSplitBy = ",";
		String csvFile = "C:/Users/Tony/Desktop/Organization Binder/2 Science & Math/1 Computer Science/Programming Languages/Java/Workspace/ChordsDetector/src/table2.csv";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] read = line.split(cvsSplitBy);
                double test = Double.parseDouble(read[0]) / rat;
                if (withinRange(test)) {
                	for (int i = 0; i < read.length; i++) {
                		data.add(read[i]);
                	}
	                break;
                }
            }
	    } catch (FileNotFoundException e) {
	            e.printStackTrace();
	    } catch (IOException e) {
	            e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
	    			br.close();
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }
	        }
	    }
		return data;
	} 
	
	// Match note ratios to 3-note chords.
	public static ArrayList<String> matchRatiosTo3Chord(double rat1, double rat2) {
		ArrayList<String> data = new ArrayList<String>();
		String line = "";
	    String cvsSplitBy = ",";
		String csvFile = "C:/Users/Tony/Desktop/Organization Binder/2 Science & Math/1 Computer Science/Programming Languages/Java/Workspace/ChordsDetector/src/table3.csv";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] read = line.split(cvsSplitBy);
                double test1 = Double.parseDouble(read[0]) / rat1;
                double test2 = Double.parseDouble(read[1]) / rat2;
                if (withinRange(test1) && withinRange(test2)) {
                	for (int i = 0; i < read.length; i++) {
                		data.add(read[i]);
                	}
	                break;
                }
            }
	    } catch (FileNotFoundException e) {
	            e.printStackTrace();
	    } catch (IOException e) {
	            e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
	    			br.close();
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }
	        }
	    }
		return data;
	} 
	
	// function to display notes and chords and other information for user
	public static void displayInfo() {
		
	} 
}
