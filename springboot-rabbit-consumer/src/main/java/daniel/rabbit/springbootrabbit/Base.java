package daniel.rabbit.springbootrabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Base {
    
    Logger log = LoggerFactory.getLogger(this.getClass());
    
    void println(Object toPrint) {
        System.out.println(toPrint);
    }

    void printf(String toPrint, Object ... args) {
        System.out.printf(toPrint, args);
    }

    void info(String toPrint, Object ... args) {
        log.info(toPrint, args);
    }

    void warn(String toPrint, Object ... args) {
        log.warn(toPrint, args);
    }

    void debug(String toPrint, Object ... args) {
        log.debug(toPrint, args);
    }
}
