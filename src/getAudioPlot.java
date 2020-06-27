/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * getAudioPlot: class that extracts the waveform data of the audio file selected. This is accomplished by using the MATLAB API,
 *  with the algorithm "getAudioPlot.m"
 */

import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getAudioPlot {

	private double[] wavefrm;
	private double FS;

	/**
	 * Constructor where the MATLAB function is called and the filtered signal waveform data is extracted.
	 * 
	 * @param filename  path for the selected audio.
	 * @param engine    MATLAB shared engine initialized in GUI.
	 * 
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws ExecutionException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 */
	public getAudioPlot(String filename,MatlabEngine engine) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		//Running step where the MATLAB "getAudioPlot.m" function is called
		engine.evalAsync("[wavefrm FS]=getAudioPlot('" + filename + "');");

		wavefrm=engine.getVariable("wavefrm");
		FS=engine.getVariable("FS");
	}

	/**
	 * Method to get the filtered signal waveform data.
	 * 
	 * @return waveform  filtered signal waveform data.
	 */
	public double[] getWavefrm() {
		return wavefrm;
	}

	/**
	 * Method to get the sampling frequency of the audio.
	 * 
	 * @return FS  sampling frequency.
	 */
	public double getFS() {
		return FS;
	}
}