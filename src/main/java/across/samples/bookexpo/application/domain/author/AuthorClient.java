package across.samples.bookexpo.application.domain.author;

import com.foreach.across.core.annotations.Exposed;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authors", url = "${author.api.url}")
@Exposed
public interface AuthorClient
{
	@GetMapping("/authors")
	Resources<AuthorResource> getAuthors();

	default AuthorResource getAuthor( Link link ) {
		String[] segments = link.getHref().split( "/" );
		return getAuthor( segments[segments.length - 1] );
	}

	@GetMapping("/authors/{id}")
	AuthorResource getAuthor( @PathVariable("id") String authorId );

	@PutMapping("/authors/{id}")
	AuthorResource updateAuthor( @PathVariable("id") String authorId, @RequestBody AuthorResource author );

	@DeleteMapping("/authors/{id}")
	void delete( @PathVariable("id") String authorId );
}
