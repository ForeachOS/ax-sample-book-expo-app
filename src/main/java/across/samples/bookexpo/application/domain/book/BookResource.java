package across.samples.bookexpo.application.domain.book;

import across.samples.bookexpo.application.domain.author.AuthorResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;

@Data
public class BookResource extends ResourceSupport
{
	//private long id;
	@Length(max = 255)
	private String title;

	/*
	@JsonProperty("_embedded")
	@JsonDeserialize(using = Jackson2HalModule.HalResourcesDeserializer.class)
	private Embedded embedded;*/


//	private String author;
//	private double price;
//	private LocalDate publishedOn;

	public String getBookId() {
		String[] segments = StringUtils.split( getId().getHref(), "/" );
		return segments[segments.length - 1];
	}

	@Data
	public static class Embedded
	{
		private AuthorResource author;
	}
}
