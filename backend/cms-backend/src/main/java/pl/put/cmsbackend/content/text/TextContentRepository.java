package pl.put.cmsbackend.content.text;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TextContentRepository extends CrudRepository<TextContent, Long> {
    List<TextContent> findAllByOwnerId(Long ownerId);
}
