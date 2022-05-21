package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.content.shared.SharedContent;
import pl.put.cmsbackend.content.shared.SharedContentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TextContentService {

    private final TextContentRepository contentRepository;
    private final SharedContentRepository sharedContentRepository;

    List<TextContentDto> findAllByOwnerId(Long ownerId) {
        return contentRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(content ->
                        new TextContentDto(content.getTitle(), content.getSubtitle(), content.getContent(),
                                content.isShared() ? content.getSharedContent().getHref() : null))
                .toList();
    }
}
