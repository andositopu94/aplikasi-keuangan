package brajaka.demo.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        //security
//        return Optional.ofNullable(SecurityContexHolder.getContext().getAuthentication())
//                .map(auth -> auth.getName());
        return Optional.of("System");
    }
}
