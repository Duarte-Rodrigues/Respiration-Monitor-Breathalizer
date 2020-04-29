

import java.util.concurrent.ExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;



public class getBreathingRate {
	
	public  getBreathingRate(String filename) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		//------------------------------------------GET FILTERED AUDIO AND INTENSITY MATLAB DATA-------------------------------
	    MatlabEngine eng = MatlabEngine.startMatlab();        
	    eng.eval("[bpm wave Fs hard soft mild]=breathingClass_Rate(" + filename + ");");
	    
	    bpm=eng.getVariable("bpm");
	    wave=eng.getVariable("wave");
	    fs=eng.getVariable("Fs");
	    hard=eng.getVariable("hard");
	    soft=eng.getVariable("soft");
	    mild=eng.getVariable("mild");
	    
	    eng.close();

	}
	
	public double getBpm() {
		return bpm;
	}

	public double[] getWave() {
		return wave;
	}

	public double getFs() {
		return fs;
	}

	public double[][] getHard() {
		return hard;
	}

	public double[][] getSoft() {
		return soft;
	}

	public double[][] getMild() {
		return mild;
	}

	private double bpm;
    private double[] wave;
    private double fs;
    private double[][] hard;
    private double[][] soft;
    private double[][] mild;
}
