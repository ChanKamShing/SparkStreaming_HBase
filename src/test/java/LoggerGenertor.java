import org.apache.log4j.Logger;

public class LoggerGenertor {
    private static Logger logger = Logger.getLogger(LoggerGenertor.class.getName());

    public static void main(String[] args) throws InterruptedException {
        int index = 0;
        while (true) {

            Thread.sleep(1000);
            logger.info("current value is : "+index++);
        }
    }
}
