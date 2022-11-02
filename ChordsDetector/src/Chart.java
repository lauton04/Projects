import java.io.File;
import java.io.FileWriter;

import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.stage.Stage; 
import javafx.scene.chart.LineChart; 
import javafx.scene.chart.NumberAxis; 
import javafx.scene.chart.XYChart; 

public class Chart extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		int nSamples = 111991;
	    double sampleRate = 44100; 
		double freqResolution = sampleRate / nSamples;
		
		File PCMFile = new File("PCM test files/multinotes.pcm");
		short[] PCMArray = GUI.PCMtoArray(PCMFile);
		/*
	  	for (int i = 0; i < nSamples; i++) {
		  	series.getData().add(new XYChart.Data(i, PCMArray[i]));
	  	} */
	  
		Complex[] frequencyArray = new Complex[nSamples];
	    for (int i = 0; i < nSamples; i++) {
	    	frequencyArray[i] = new Complex(PCMArray[i], 0);
		}
	    
	    fft.fft2(frequencyArray, nSamples, 0);
	  
	    //Defining the x axis             
	    final NumberAxis xAxis = new NumberAxis(0, nSamples /* / 2*/, (nSamples /*/ 2*/) / 20);
	    xAxis.setLabel("Sample"); 
	    
	    //Defining the y axis   
	    final NumberAxis yAxis = new NumberAxis(-3000, 3000, 3000 / 20);
	    yAxis.setLabel("Amplitude"); 
	    
	    //Creating the line chart 
	    final LineChart<Number,Number> linechart = new LineChart<Number,Number>(xAxis, yAxis);  
	    
	    //Prepare XYChart.Series objects by setting data 
	    XYChart.Series series = new XYChart.Series(); 
	    //series.setName("No. of schools in a year"); 
	    	      
	    for (int i = 0; i < nSamples /*/ 2 */; i++) {
	    	// series.getData().add(new XYChart.Data(i * freqResolution, frequencyArray[i].abs()));
	    	series.getData().add(new XYChart.Data(i, PCMArray[i]));
	    }
	  
	    //Setting the data to Line chart    
	    linechart.getData().add(series); 
	  
	    linechart.setCreateSymbols(false);
	    
	    //Creating a Group object  
	    //Group root = new Group(linechart); 
	     
	    //Creating a scene object 
	    Scene scene = new Scene(linechart, 402, 300);  
	  
	    //Setting title to the Stage 
	    //stage.setTitle("Line Chart"); 
	     
	    //Adding scene to the stage 
	    stage.setScene(scene);
	   
	    //Displaying the contents of the stage 
	    stage.show(); 
	  
	    FileWriter out = null;
	    out = new FileWriter("outputX.txt");
					
	    for (int i = 0; i < nSamples/2; i++) {
	    	out.write(Double.toString(frequencyArray[i].abs()));
	    	out.write(", ");
	    }
	    out.close();
	}
	
	public static void main(String args[]) {   
		launch(args);   
	} 
	
	/* Problems: 
	 * length of time to load chart */
		
}