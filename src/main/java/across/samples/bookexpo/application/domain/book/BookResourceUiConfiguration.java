package across.samples.bookexpo.application.domain.book;

import across.samples.bookexpo.application.domain.author.AuthorResource;
import com.foreach.across.modules.entity.EntityAttributes;
import com.foreach.across.modules.entity.config.EntityConfigurer;
import com.foreach.across.modules.entity.config.builders.EntitiesConfigurationBuilder;
import com.foreach.across.modules.entity.registry.EntityFactory;
import com.foreach.across.modules.entity.registry.properties.EntityPropertyController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.core.EntityInformation;

@Configuration
@RequiredArgsConstructor
class BookResourceUiConfiguration implements EntityConfigurer
{
	private static final EntityInformation<BookResource, String> ENTITY_INFORMATION = new EntityInformation<BookResource, String>()
	{
		@Override
		public boolean isNew( BookResource entity ) {
			return false;
		}

		@Override
		public String getId( BookResource entity ) {
			return entity.getBookId();
		}

		@Override
		public Class<String> getIdType() {
			return String.class;
		}

		@Override
		public Class<BookResource> getJavaType() {
			return BookResource.class;
		}
	};
	private final BookClient bookClient;

	@Override
	public void configure( EntitiesConfigurationBuilder entities ) {
		entities.create()
		        .entityType( BookResource.class, true )

		        .show()
		        .as( BookResource.class )
		        .properties(
				        props -> props.property( "author" )
				                      .propertyType( AuthorResource.class )
				                      .writable( true )
				                      .readable( true )
				                      .hidden( false )
				                      .controller(
						                      controller -> controller.withTarget( BookResource.class, AuthorResource.class )
						                                              .order( EntityPropertyController.AFTER_ENTITY )
						                                              .valueFetcher( book -> bookClient.getBookAuthor( book.getBookId() ) )
						                                              .saveConsumer( ( book, author ) -> {
							                                              if ( author.isModified() ) {
								                                              bookClient.updateBookAuthor( book.getBookId(),
								                                                                           author.getNewValue().getId().getHref() );
							                                              }
						                                              } )
				                      )
				                      .attribute( EntityAttributes.PROPERTY_REQUIRED, true )
		        )
		        .entityModel(
				        model -> model
						        .labelPrinter( ( book, locale ) -> book.getTitle() )
						        .entityInformation( ENTITY_INFORMATION )

						        .findOneMethod( s -> bookClient.getBook( s.toString() ) )
						        .entityFactory( new EntityFactory<BookResource>()
						        {
							        @Override
							        public BookResource createNew( Object... args ) {
								        return new BookResource();
							        }

							        @Override
							        public BookResource createDto( BookResource entity ) {
								        return entity;
							        }
						        } )
						        .saveMethod( book -> bookClient.updateBook( book.getBookId(), book ) )
						        .deleteMethod( book -> bookClient.delete( book.getBookId() ) )
		        )
		        .listView( lvb -> lvb.pageFetcher( pageable -> bookClient.getBooks() ) )
		        .createFormView()
		        .updateFormView()
		        .deleteFormView();
	}
}
