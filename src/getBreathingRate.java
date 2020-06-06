import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;



public class getBreathingRate {
	
	public  getBreathingRate(String filename,MatlabEngine engine) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		//------------------------------------------GET FILTERED AUDIO AND INTENSITY MATLAB DATA-------------------------------
	    //MatlabEngine eng = MatlabEngine.startMatlab();        
	    engine.evalAsync("[bpm wave env Fs hard soft mild]=breathingClass_Rate('" + filename + "');");
	    
	    bpm=engine.getVariable("bpm");
	    wave=engine.getVariable("wave");
	    env=engine.getVariable("env");
	    fs=engine.getVariable("Fs");
	    hard=engine.getVariable("hard");
	    soft=engine.getVariable("soft");
	    mild=engine.getVariable("mild");
	    

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

	public double[] getEnv() {
		return env;
	}


	private double bpm;
    private double[] wave;
    private double[] env;
    private double fs;
    private double[][] hard;
    private double[][] soft;
    private double[][] mild;
}
