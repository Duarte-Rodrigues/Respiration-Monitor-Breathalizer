/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * getWheeze: class that calls the MATALAB algorthim ("wheezeDetect.m") to extract the information 
 * about the wheezes detection and patient diagnostic, whether it is healthy or not.
 */

import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getWheeze {
	
	private double[] x;
	private String state;
	private double fs;
	private String normSpectrumPath;
	private String wheezeSpectrumPath;
	private double[] wheezeActivity;
	
	/**
	 * Constructor where the MATLAB function is called and the wheezes detection and patient diagnostic are extracted.
	 * 
	 * @param filename  path for the selected audio.
	 * @param eng       MATLAB shared engine initialized in GUI.
	 * 
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws ExecutionException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 */
	public getWheeze(String filename,MatlabEngine eng) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		//Running step where the MATLAB "wheezeDetect.m" function is called
		eng.eval("[x state fs normSpectrum wheezeSpectrum wheezeAct]=wheezeDetect('"+filename+"');");
		
	    x=eng.getVariable("x");
	    state=eng.getVariable("state");
	    fs=eng.getVariable("fs");
	    normSpectrumPath=eng.getVariable("normSpectrum");
	    wheezeSpectrumPath=eng.getVariable("wheezeSpectrum");
	    wheezeActivity=eng.getVariable("wheezeAct");
	}
	
	/**
	 * Method to get the wheeze activity waveform data.
	 * 
	 * @return x  wheeze activity waveform data.
	 */
	public double[] getX() {
		return x;
	}

	/**
	 * Method to get the sampling frequency of the audio.
	 * 
	 * @return fs  sampling frequency.
	 */
	public double getFs() {
		return fs;
	}

	/**
	 * Method to get the path relative to the spectrum content of the signal.
	 * 
	 * @return normSpectrumPath  path relative to the spectrum content of the signal.
	 */
	public String getNormSpectrumPath() {
		return normSpectrumPath;
	}

	/**
	 * Method to get the path relative to the spectrum content of the wheeze activity.
	 * 
	 * @return wheezeSpectrumPath  path relative to the spectrum content of the wheeze activity.
	 */
	public String getWheezeSpectrumPath() {
		return wheezeSpectrumPath;
	}

	/**
	 * Method to get the wheeze activity intervals to be represented over the audio waveform.
	 * this reperesented in binary wave from, where 1 means that a wheeze was detected and 0 means normal sound.
	 * 
	 * @return x  wheeze activity intervals.
	 */
	public double[] getWheezeActivity() {
		return wheezeActivity;
	}

	/**
	 * Method to get the diagnostic (healthy or unhelathy).
	 * 
	 * @return state  diagnostic of the patient from the algorithm.
	 */
	public String getState() {
		return state;
	}
}
