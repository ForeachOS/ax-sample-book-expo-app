package across.samples.bookexpo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.adminweb.AdminWebModule;
import com.foreach.across.modules.entity.EntityModule;
import com.foreach.across.modules.hibernate.jpa.AcrossHibernateJpaModule;
import com.foreach.across.modules.web.AcrossWebModule;
import feign.codec.Decoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@AcrossApplication(
		modules = {
				AcrossWebModule.NAME,
				AdminWebModule.NAME,
				AcrossHibernateJpaModule.NAME,
				EntityModule.NAME
		}
)
public class ExpoApplication
{
	@Bean
	public Decoder feignDecoder() {
		ObjectMapper mapper = new ObjectMapper()
				.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false )
				.registerModule( new Jackson2HalModule() );

		HttpMessageConverters converters = new HttpMessageConverters(
				new MappingJackson2HttpMessageConverter( mapper )
		);

		return new ResponseEntityDecoder( new SpringDecoder( () -> converters ) );
	}

	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( ExpoApplication.class );
		//springApplication.setDefaultProperties( Collections.singletonMap( "spring.config.location", "${user.home}/dev-configs/expo-application.yml" ) );
		springApplication.run( args );
	}
}