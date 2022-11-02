// written by Tony Lauricella except the Complex and fft classes

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.ImageIcon;

import javafx.scene.chart.*;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSlider;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI {

	File PCMFile; 
	short[] PCMArray;
	final double sampleRate = 44100; 
    double freqResolution;
    private JTextField locationsTextField;
    private JTextField fundamentalFrequenciesTextField;
    private JTable table;
    private JFrame frmChordsdetector;
	private JTextField txtPCMFileAddress;
	int maxAmp = 20;
    
    public static short[] PCMtoArray(File file) throws IOException {
	    short[] result = null;
        InputStream in = new FileInputStream(file);
        int bufferSize = (int) (file.length() / 2);
        result = new short[bufferSize];
        DataInputStream is = new DataInputStream(in);
        for (int i = 0; i < bufferSize; i++) {
        	short x = is.readShort();
	    	x = Short.reverseBytes(x);
	    	result[i] = x;
        }
        is.close();
	    return result;
	}
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmChordsdetector.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChordsdetector = new JFrame();
		frmChordsdetector.setTitle("ChordsDetector");
		frmChordsdetector.setBounds(100, 100, 840, 545);
		frmChordsdetector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChordsdetector.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 91, 804, 69);
		frmChordsdetector.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblSectionBoundariesSamples = new JLabel("Note/chord locations:");
		lblSectionBoundariesSamples.setBounds(436, 3, 358, 33);
		panel.add(lblSectionBoundariesSamples);
		lblSectionBoundariesSamples.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel = new JLabel("% of Max \r\nAmplitude");
		lblNewLabel.setBounds(251, 0, 120, 38);
		panel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		locationsTextField = new JTextField();
		locationsTextField.setBounds(436, 37, 358, 20);
		panel.add(locationsTextField);
		locationsTextField.setColumns(10);
		
		JSlider maxAmpSlider = new JSlider();
		maxAmpSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//maxAmpSlider.
			}
		});
		maxAmpSlider.setBounds(251, 31, 120, 23);
		panel.add(maxAmpSlider);
		maxAmpSlider.setValue(20);
		
		JButton btnDetectLocations = new JButton("Detect locations");
		btnDetectLocations.setBounds(10, 11, 182, 47);
		panel.add(btnDetectLocations);
		btnDetectLocations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setLayout(null);
		panel_1.setBounds(10, 171, 804, 125);
		frmChordsdetector.getContentPane().add(panel_1);
		
		JSlider slider = new JSlider();
		slider.setMinimum(30);
		slider.setValue(20);
		slider.setBounds(442, 38, 120, 23);
		panel_1.add(slider);
		
		JLabel lblFundamentalFrequencies = new JLabel("Fundamental frequencies:");
		lblFundamentalFrequencies.setBounds(10, 69, 784, 25);
		panel_1.add(lblFundamentalFrequencies);
		lblFundamentalFrequencies.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblOfFrequency = new JLabel("# of Frequency Peaks");
		lblOfFrequency.setBounds(440, 0, 135, 38);
		panel_1.add(lblOfFrequency);
		lblOfFrequency.setHorizontalAlignment(SwingConstants.CENTER);
		
		JSlider slider_2 = new JSlider();
		slider_2.setBounds(662, 38, 120, 23);
		panel_1.add(slider_2);
		slider_2.setMaximum(10);
		slider_2.setMinimum(3);
		
		JLabel lblOfHarmonics = new JLabel("# of harmonics to look for");
		lblOfHarmonics.setBounds(630, 0, 164, 38);
		panel_1.add(lblOfHarmonics);
		lblOfHarmonics.setHorizontalAlignment(SwingConstants.CENTER);
		
		fundamentalFrequenciesTextField = new JTextField();
		fundamentalFrequenciesTextField.setBounds(10, 94, 784, 20);
		panel_1.add(fundamentalFrequenciesTextField);
		fundamentalFrequenciesTextField.setColumns(10);
		
		JButton btnDetectFundamentals = new JButton("Detect fundamentals");
		btnDetectFundamentals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int nSamples;
				
				// Find section start points.
			    ArrayList<Integer[]> averages;
			    averages = Locations.locate(PCMArray, 500);											//	
			    //maxAmp = maxAmpSlider.getValue();
			    ArrayList<Integer> locations = Locations.highestDiffs(averages, maxAmp);			//
			    
			    // Add the total PCM length. 
			    locations.add(PCMArray.length);
			    locationsTextField.setText(locations.toString());
			
			    // Store the note/chords from each section.
			    ArrayList<ArrayList<Double>> allSectionsFundamentals = new ArrayList<ArrayList<Double>>();
			    for (int i = 0; i < locations.size() - 1; i++) {
			    	nSamples = locations.get(i + 1) - locations.get(i);
				    if (Locations.powerOfTwoCheck(nSamples) == false) {
				    	nSamples = Locations.changeToPowerOfTwo(nSamples);
		    		}
				    freqResolution = sampleRate / nSamples;

				    // fft
				    Complex[] frequencyArray = new Complex[nSamples];
					for (int j = 0; j < nSamples; j++) {
						frequencyArray[j] = new Complex(PCMArray[locations.get(i) + j], 0);
					}
					fft.fft2(frequencyArray, nSamples, 0);
					ArrayList<Double[]> peaks = new ArrayList<Double[]>();
					peaks = Frequencies.detectPeaks(frequencyArray, freqResolution);
					ArrayList<Double> peakDiffs = new ArrayList<Double>();
					peakDiffs = Frequencies.highestDiffs(peaks, 30);				//
										//System.out.println("highest peaks " + i + ": " + peakDiffs);
					ArrayList<Double> fundamentals = new ArrayList<Double>();
					fundamentals = Frequencies.findFundamentals(peakDiffs, 7);		//
					allSectionsFundamentals.add(fundamentals);
										//System.out.println("fundamentals " + i + ": " + fundamentals);
				}
			    						//System.out.println("all fundamentals " + allSectionsFundamentals);
			    fundamentalFrequenciesTextField.setText(allSectionsFundamentals.toString());
			    
			    //Find the chord data and display it. 
			    ArrayList<ArrayList<String>> outputInfo = new ArrayList<ArrayList<String>>();
			    for (int i = 0; i < allSectionsFundamentals.size(); i++) {
			    	outputInfo.add(Frequencies.matchFundamentals(allSectionsFundamentals.get(i))); 
			    }
			    System.out.println(outputInfo);    
			}
		});
		btnDetectFundamentals.setBounds(10, 11, 182, 47);
		panel_1.add(btnDetectFundamentals);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 804, 69);
		frmChordsdetector.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblPcmFileAddress = new JLabel("PCM File address:");
		lblPcmFileAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblPcmFileAddress.setBounds(202, 0, 592, 30);
		panel_2.add(lblPcmFileAddress);
		
		JButton btnLoadPcmFile = new JButton("Load PCM file");
		btnLoadPcmFile.setBounds(10, 11, 182, 47);
		panel_2.add(btnLoadPcmFile);
		
		txtPCMFileAddress = new JTextField();
		txtPCMFileAddress.setBounds(202, 24, 592, 20);
		panel_2.add(txtPCMFileAddress);
		txtPCMFileAddress.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 304, 804, 192);
		frmChordsdetector.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JButton btnDetectChords = new JButton("Match notes/chords");
		btnDetectChords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnDetectChords.setBounds(10, 6, 182, 47);
		panel_3.add(btnDetectChords);
		
		JButton btnSaveData = new JButton("Save data");
		btnSaveData.setBounds(528, 6, 128, 47);
		panel_3.add(btnSaveData);
		
		JButton buttonClearData = new JButton("Clear data");
		buttonClearData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPCMFileAddress.setText("");
				locationsTextField.setText("");
				fundamentalFrequenciesTextField.setText("");
				table.setModel(new DefaultTableModel(
					new Object[][] {
						{"Frequencies", "Pitches", "Octaves", "Piano Keys", "Root1", "Name", "Root2", "Name", "Root3", "Name"},
						{null, null, null, null, null, null, null, null, null, null},
						{null, null, null, null, null, null, null, null, null, null},
						{null, null, null, null, null, null, null, null, null, null},
						{null, null, null, null, null, null, null, null, null, null},
						{null, null, null, null, null, null, null, null, null, null},
						{null, null, null, null, null, null, null, null, null, null},
					},
					new String[] {
						"Frequencies", "Pitches", "Octaves", "Piano Keys", "Root1", "Name", "Root2", "Name", "Root3", "Name"
					}
				));
			}
		});
		
		buttonClearData.setBounds(666, 6, 128, 47);
		panel_3.add(buttonClearData);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Frequencies", "Pitches", "Octaves", "Piano Keys", "Root1", "Name", "Root2", "Name", "Root3", "Name"},
				/*{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},*/
				{"293.665,349.228,440.000", "D,F,A", "4,4,4", "42,45,49", "1,b3,5", "minor", "6,1,3", "", "4,b6,1", ""},
				{"293.665,391.995,493.883", "D,G,B", "4,4,4", "42,47,51", "1,4,6", "", "5,1,3", "major (2nd inversion)", "b3,b6,1", ""},
				{"261.626,329.628,391.995", "C,E,G", "4,4,4", "40,44,47", "1,3,5", "major", "b6,1,b3", "", "4,6,1", ""},
			},
			new String[] {
				"Frequencies", "Pitches", "Octaves", "Piano Keys", "Root1", "Name", "Root2", "Name", "Root3", "Name"
			}
		));
		table.setFillsViewportHeight(true);
		table.setBorder(new LineBorder(new Color(192, 192, 192)));
		table.setBounds(10, 69, 784, 112);
		panel_3.add(table);
		btnLoadPcmFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				int ret = jfc.showOpenDialog(frmChordsdetector);
				if (ret != JFileChooser.APPROVE_OPTION) return;
				File theFile = jfc.getSelectedFile();
				PCMFile = theFile;
				txtPCMFileAddress.setText(theFile.getAbsolutePath());
				try {
					PCMArray = PCMtoArray(PCMFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Display graph of the waveform.
			}
		});
		
		
		
	}
}

class ColumnComparatorI implements Comparator {
	int sortColumn;

	ColumnComparatorI(int columnToSort) {
		this.sortColumn = columnToSort;
	}

	public int compare(Object o1, Object o2) {
		Integer[] row1 = (Integer[]) o1;
		Integer[] row2 = (Integer[]) o2;
		//compare the columns to sort
		return row1[sortColumn].compareTo(row2[sortColumn]);
	}
}

class ColumnComparatorD implements Comparator {
	int sortColumn;

	ColumnComparatorD(int columnToSort) {
		this.sortColumn = columnToSort;
	}

	public int compare(Object o1, Object o2) {
		Double[] row1 = (Double[]) o1;
		Double[] row2 = (Double[]) o2;
		//compare the columns to sort
		return row1[sortColumn].compareTo(row2[sortColumn]);
	}
}


