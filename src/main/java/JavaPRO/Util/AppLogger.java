package JavaPRO.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppLogger {

    @Bean(name = "registerLogger")
    public Logger getRegisterLogger(){
        return LogManager.getLogger("registerLogger");
    }

    @Bean(name = "passRecoveryLogger")
    public Logger getPassRecoveryLogger(){
        return LogManager.getLogger("passRecoveryLogger");
    }

    @Bean(name = "authorizationLogger")
    public Logger getAuthorizationLogger(){
        return LogManager.getLogger("authorizationLogger");
    }

    @Bean(name = "mailSenderLogger")
    public Logger getMailSenderLogger(){
        return LogManager.getLogger("mailSenderLogger");
    }
}
