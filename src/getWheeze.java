
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import sun.awt.image.ToolkitImage;

public class getWheeze {
	
	public getWheeze(String filename,MatlabEngine eng) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		eng.eval("[x state fs normSpectrum wheezeSpectrum delta_t nf]=wheezeDetect('"+filename+"');");
		
	    x=eng.getVariable("x");
	    state=eng.getVariable("state");
	    fs=eng.getVariable("fs");
	    normSpectrumPath=eng.getVariable("normSpectrum");
	    wheezeSpectrumPath=eng.getVariable("wheezeSpectrum");
	    delta_t=eng.getVariable("delta_t");
	    nf=eng.getVariable("nf");
	    
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

	public double[] getDelta_t() {
		return delta_t;
	}

	public double getNf() {
		return nf;
	}

	public String getState() {
		return state;
	}

	private double[] x;
	private String state;
	private double fs;
	private String normSpectrumPath;
	private String wheezeSpectrumPath;
	private double[] delta_t;
	private double nf;
}
