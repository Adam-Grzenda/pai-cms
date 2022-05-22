package pl.put.cmsbackend.content.text;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TextContentRepository extends CrudRepository<TextContent, Long> {

    Optional<TextContent> findByOwner_EmailAndTitle(String ownerEmail, String title);

    Page<TextContent> findAllByOwner_id(Long owner_id, Pageable pageable);

}
