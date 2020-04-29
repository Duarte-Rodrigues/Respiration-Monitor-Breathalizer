
import java.util.concurrent.ExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getWheeze {
	
	public getWheeze(String filename) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		MatlabEngine eng = MatlabEngine.startMatlab(); 
		eng.eval("[x fs Xw Xwo delta_t nf]=wheezeDetect('C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Matlab\\A_rale_bronchial.wav');");
		
	    x=eng.getVariable("x");
	    fs=eng.getVariable("fs");
	    Xw=eng.getVariable("Xw");
	    Xwo=eng.getVariable("Xwo");
	    delta_t=eng.getVariable("delta_t");
	    nf=eng.getVariable("nf");
	    
	    eng.close();
	}
	
	private double[] x;
	private double fs;
	private double[] Xw;
	private double[] Xwo;
	private double[] delta_t;
	private double[] nf;
}
