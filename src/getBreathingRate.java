/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * getBreathingRate: class that calls the MATALAB algorthim ("breathingClass_Rate.m") to extract the information 
 * about the breathing rate and intensity, to later on displayed on the pop-up.
 */

import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getBreathingRate {
	
	private double bpm;
    private double[] wave;
    private double[] env;
    private double fs;
    private double[][] hard;
    private double[][] soft;
    private double[][] mild;
	
    /**
	 * Constructor where the MATLAB function is called and the breathing rate and intensity data is extracted.
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
	public  getBreathingRate(String filename,MatlabEngine engine) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
    
		//Running step where the MATLAB "breathingClass_Rate.m" function is called
	    engine.evalAsync("[bpm wave env Fs hard soft mild]=breathingClass_Rate('" + filename + "');");
	    
	    bpm=engine.getVariable("bpm");
	    wave=engine.getVariable("wave");
	    env=engine.getVariable("env");
	    fs=engine.getVariable("Fs");
	    hard=engine.getVariable("hard");
	    soft=engine.getVariable("soft");
	    mild=engine.getVariable("mild");
	}
	
	/**
	 * Method to get the breaths per minute output information.
	 * 
	 * @return bpm  breaths per minute estimation of the audio.
	 */
	public double getBpm() {
		return bpm;
	}

	/**
	 * Method to get the filtered signal waveform data.
	 * 
	 * @return wave  filtered signal waveform data.
	 */
	public double[] getWave() {
		return wave;
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
	 * Method to get the location of the hard breaths, realtive to the signal envelope.
	 * 
	 * @return hard  hard breath coordinates.
	 */
	public double[][] getHard() {
		return hard;
	}

	/**
	 * Method to get the location of the soft breaths, realtive to the signal envelope.
	 * 
	 * @return soft  soft breath coordinates.
	 */
	public double[][] getSoft() {
		return soft;
	}

	/**
	 * Method to get the location of the mild breaths, realtive to the signal envelope.
	 * 
	 * @return mild  mild breath coordinates.
	 */
	public double[][] getMild() {
		return mild;
	}

	/**
	 * Method to get the signal envelope waveform data.
	 * 
	 * @return env  signal envelope waveform data.
	 */
	public double[] getEnv() {
		return env;
	}
}
