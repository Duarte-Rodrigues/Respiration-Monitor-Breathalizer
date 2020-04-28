import java.util.logging.Logger;
public class TestPathLib {
	static Logger logger = Logger.getLogger(TestPathLib.class.getName());
	public static void main(String[] args) {
		logger.info(System.getProperty("java.library.path"));

	}

}