package ru.skillbox.diplom.group25.microservice.post.configuration;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "dkfu894ti");
        config.put("api_key", "694624762442931");
        config.put("api_secret", "jWcMAYikg-xLpaHjE1Gyg8l-5pA");
        return new Cloudinary(config);
    }

}
