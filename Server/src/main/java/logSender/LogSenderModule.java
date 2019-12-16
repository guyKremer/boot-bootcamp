package logSender;

import com.google.inject.AbstractModule;
import logSender.senders.Log4j;
import logSender.senders.Sender;

public class LogSenderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Sender.class).to(Log4j.class);
    }
}
