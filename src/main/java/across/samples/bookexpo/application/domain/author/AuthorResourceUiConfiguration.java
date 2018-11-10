package across.samples.bookexpo.application.domain.author;

import com.foreach.across.modules.entity.config.EntityConfigurer;
import com.foreach.across.modules.entity.config.builders.EntitiesConfigurationBuilder;
import com.foreach.across.modules.entity.query.EntityQuery;
import com.foreach.across.modules.entity.query.EntityQueryExecutor;
import com.foreach.across.modules.entity.query.SimpleEntityQueryFacade;
import com.foreach.across.modules.entity.registry.EntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.core.EntityInformation;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
class AuthorResourceUiConfiguration implements EntityConfigurer
{
	private static final EntityInformation<AuthorResource, String> ENTITY_INFORMATION = new EntityInformation<AuthorResource, String>()
	{
		@Override
		public boolean isNew( AuthorResource entity ) {
			return false;
		}

		@Override
		public String getId( AuthorResource entity ) {
			return entity.getAuthorId();
		}

		@Override
		public Class<String> getIdType() {
			return String.class;
		}

		@Override
		public Class<AuthorResource> getJavaType() {
			return AuthorResource.class;
		}
	};
	private final AuthorClient authorClient;

	@Override
	public void configure( EntitiesConfigurationBuilder entities ) {
		entities.create()
		        .entityType( AuthorResource.class, true )

		        .show()
		        .as( AuthorResource.class )
		        .entityModel(
				        model -> model
						        .labelPrinter( ( book, locale ) -> book.getName() )
						        .entityInformation( ENTITY_INFORMATION )
						        .findOneMethod( s -> authorClient.getAuthor( s.toString() ) )
						        .entityFactory( new EntityFactory<AuthorResource>()
						        {
							        @Override
							        public AuthorResource createNew( Object... args ) {
								        return new AuthorResource();
							        }

							        @Override
							        public AuthorResource createDto( AuthorResource entity ) {
								        return entity;
							        }
						        } )
						        .saveMethod( book -> authorClient.updateAuthor( book.getAuthorId(), book ) )
						        .deleteMethod( book -> authorClient.delete( book.getAuthorId() ) )
		        )
		        .listView( lvb -> lvb.pageFetcher( pageable -> authorClient.getAuthors() ) )
		        .createFormView()
		        .updateFormView()
		        .deleteFormView()
		.attribute(
				EntityQueryExecutor.class,
				new EntityQueryExecutor()
				{
					@Override
					public List findAll( EntityQuery query ) {
						return new ArrayList( authorClient.getAuthors().getContent() );
					}

					@Override
					public List findAll( EntityQuery query, Sort sort ) {
						return findAll( query );
					}

					@Override
					public Page findAll( EntityQuery query, Pageable pageable ) {
						return new PageImpl( findAll( query ) );
					}
				}
		);
	}
}
