package logSender.senders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j implements Sender {

    @Override
    public void sendLog(String msg) {
        Logger logger = LogManager.getLogger(Sender.class);
        logger.warn(msg);
    }
}
