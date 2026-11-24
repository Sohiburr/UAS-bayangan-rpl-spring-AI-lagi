package id.academy.springailabs.config;

import id.academy.springailabs.model.DemoChatModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("demo")
public class DemoChatModelConfiguration {

    @Bean
    @Primary
    ChatModel demoChatModel() {
        return new DemoChatModel();
    }
}
