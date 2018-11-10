package across.samples.bookexpo.application.domain.book;

import across.samples.bookexpo.application.domain.author.AuthorResource;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "books", url = "${book.api.url}")
@Exposed
public interface BookClient
{
	@GetMapping("/books")
	Resources<BookResource> getBooks();

	@GetMapping("/books/{id}")
	BookResource getBook( @PathVariable("id") String bookId );

	@PutMapping("/books/{id}")
	BookResource updateBook( @PathVariable("id") String bookId, @RequestBody BookResource book );

	@GetMapping("/books/{id}/author")
	AuthorResource getBookAuthor( @PathVariable("id") String bookId );

	@PutMapping(path = "/books/{id}/author", consumes = "text/uri-list")
	void updateBookAuthor( @PathVariable("id") String bookId, @RequestBody String authorUri );

	@DeleteMapping("/books/{id}")
	void delete( @PathVariable("id") String bookId );
}
