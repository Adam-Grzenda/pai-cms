package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.content.api.TextContentDto;
import pl.put.cmsbackend.content.text.db.ContentTag;
import pl.put.cmsbackend.content.text.db.TextContent;
import pl.put.cmsbackend.content.text.db.TextContentRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexedTextContentService {
    private final EntityManager entityManager;
    private final TextContentRepository textContentRepository;

    public List<TextContentDto> findTextContentByKeyword(String keyword, List<ContentTag> tags, String email) {
        List<TextContent> results = keyword != null ? getSearchByKeywordResult(keyword, email) : textContentRepository.findAllByOwner_Email(email);

        if (tags != null) {
            results = results.stream().filter(content -> content.getContentTags().containsAll(tags)).toList();
        }

        return results.stream().map(TextContentMapper::mapContentToContentDto).toList();
    }

    private List<TextContent> getSearchByKeywordResult(String keyword, String email) {
        return searchContentByKeyword(keyword).stream()
                .filter(item -> item.getOwner().getEmail().equals(email))
                .toList();//TODO - filter before lucene to avoid n+1
    }

    private List<TextContent> searchContentByKeyword(String keyword) {
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
        return results;
    }


}
