package pl.put.cmsbackend.content.shared;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SharedContentRepository extends CrudRepository<SharedContent, Long> {
    Optional<SharedContent> findByContentIdAndActiveTrue(Long contentId);
}
