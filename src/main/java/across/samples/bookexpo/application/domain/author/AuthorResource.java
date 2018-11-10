package across.samples.bookexpo.application.domain.author;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.ResourceSupport;

@Data
public class AuthorResource extends ResourceSupport
{
	@Length(max = 255)
	private String name;

	public String getAuthorId() {
		String[] segments = StringUtils.split( getId().getHref(), "/" );
		return segments[segments.length - 1];
	}
}
