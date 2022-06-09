package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.content.text.db.TextContent;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexedTextContentService {
    private final EntityManager entityManager;

    public List<TextContentDto> findTextContentByKeyword(String keyword, Long ownerId) {
        var fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(TextContent.class)
                .get();

        Query query = queryBuilder.keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .onFields("title", "subtitle", "content")
                .matching(keyword)
                .createQuery();

        List<TextContent> results = fullTextEntityManager.createFullTextQuery(query, TextContent.class).getResultList();


        return results.stream()
                .filter(item -> item.getOwner().getId().equals(ownerId)) //TODO - filter before lucene to avoid n+1
                .map(TextContentMapper::mapContentToContentDto)
                .toList();
    }


}
