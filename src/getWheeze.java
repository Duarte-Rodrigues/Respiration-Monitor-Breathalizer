import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getWheeze {
	
	public getWheeze(String filename,MatlabEngine eng) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		eng.eval("[x state fs normSpectrum wheezeSpectrum wheezeAct]=wheezeDetect('"+filename+"');");
		
	    x=eng.getVariable("x");
	    state=eng.getVariable("state");
	    fs=eng.getVariable("fs");
	    normSpectrumPath=eng.getVariable("normSpectrum");
	    wheezeSpectrumPath=eng.getVariable("wheezeSpectrum");
	    wheezeActivity=eng.getVariable("wheezeAct");
	    
	}
	
	public double[] getX() {
		return x;
	}

	public double getFs() {
		return fs;
	}

	public String getNormSpectrumPath() {
		return normSpectrumPath;
	}

	public String getWheezeSpectrumPath() {
		return wheezeSpectrumPath;
	}

	public double[] getWheezeActivity() {
		return wheezeActivity;
	}

	public String getState() {
		return state;
	}

	private double[] x;
	private String state;
	private double fs;
	private String normSpectrumPath;
	private String wheezeSpectrumPath;
	private double[] wheezeActivity;
}
