package logSender;

import com.google.inject.Singleton;
import logSender.senders.Sender;

import javax.inject.Inject;

@Singleton
public class LogSender {
    private Sender sender;
    @Inject
    public LogSender(Sender sender){
        this.sender = sender;
    }

    public String sendLog(String logMsg) {
        sender.sendLog(logMsg);
        return logMsg;
    }
}
