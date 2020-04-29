import com.mathworks.engine.MatlabEngine;

public class StartMatlabDu {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MatlabEngine eng = MatlabEngine.startMatlab();
        eng.eval("[bpm hard soft mild]=breathingClass_Rate('C:\\Users\\dtrdu\\Desktop\\Duarte\\audio_wav\\A_rale_vesicular.wav')");
        double[] a = {2.0 ,4.0, 6.0};
        double[] roots = eng.feval("sqrt", a);
        for (double e: roots) {
            System.out.println(e);
        }
        eng.close();
	}

}
