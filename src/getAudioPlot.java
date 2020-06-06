import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class getAudioPlot {
	
	private double[] wavefrm;
	 private double FS;

	public  getAudioPlot(String filename,MatlabEngine engine) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
	
		engine.evalAsync("[wavefrm FS]=getAudioPlot('" + filename + "');");
		
		wavefrm=engine.getVariable("wavefrm");
		FS=engine.getVariable("FS");
	}
	
	public double[] getWavefrm() {
		return wavefrm;
	}
	
	public double getFS() {
		return FS;
	}
	
}
