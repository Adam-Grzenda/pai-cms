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
                                content.isShared() ? getSharedUrl(content.getId()) : null))
                .toList();
    }

    private String getSharedUrl(Long contentId) {
        return sharedContentRepository.findByContentIdAndActiveTrue(contentId).orElseGet(() -> createSharedContent(contentId)).getHref();
    }

    private SharedContent createSharedContent(Long contentId) {
        var sharedContent = new SharedContent();
        sharedContent.setContentId(contentId);
        sharedContent.setHref("shared/" + contentId);
        sharedContent.setActive(true);
        return sharedContentRepository.save(sharedContent);
    }
}
