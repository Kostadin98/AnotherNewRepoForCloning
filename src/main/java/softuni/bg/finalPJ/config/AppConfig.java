package softuni.bg.finalPJ.config;


import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class AppConfig {


//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelmapper = new ModelMapper();

//        Provider<LocalDateTime> localDateTimeProvider = new AbstractProvider<LocalDateTime>() {
//            @Override
//            public LocalDateTime get() {
//                return LocalDateTime.now();
//            }
//        };

//        Converter<String, LocalDateTime> toStringDate = new AbstractConverter<String, LocalDateTime>() {
//            @Override
//            protected LocalDateTime convert(String source) {
//                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
//                LocalDateTime localDateTime = LocalDateTime.parse(source, format);
//                return localDateTime;
//            }
//        };

//        modelmapper.createTypeMap(String.class, LocalDateTime.class);
//        modelmapper.addConverter(toStringDate);
//        modelmapper.getTypeMap(String.class, LocalDateTime.class).setProvider(localDateTimeProvider);

//        return modelmapper;
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
