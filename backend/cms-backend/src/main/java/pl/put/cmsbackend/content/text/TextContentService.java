package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.content.api.TextContentDto;
import pl.put.cmsbackend.content.exception.ContentAccessPermissionException;
import pl.put.cmsbackend.content.exception.InvalidTextContentException;
import pl.put.cmsbackend.content.exception.TextContentNotFound;
import pl.put.cmsbackend.content.text.db.TextContent;
import pl.put.cmsbackend.content.text.db.TextContentRepository;

import java.util.List;
import java.util.Optional;

import static pl.put.cmsbackend.content.text.TextContentMapper.mapContentToContentDto;

@Service
@RequiredArgsConstructor
public class TextContentService {

    private final TextContentRepository contentRepository;
    private final AppUserService appUserService;

    @Transactional
    public TextContentDto addTextContent(String email, TextContentDto textContent) {
        AppUser user = appUserService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        validateTitle(email, textContent.title());

        TextContent content = new TextContent(user, textContent.title(), textContent.subtitle(), textContent.content(), textContent.tags(), textContent.imageHref());
        TextContent savedContent = contentRepository.save(content);

        return mapContentToContentDto(savedContent);
    }

    @Transactional
    public void deleteTextContent(String email, Long id) {
        TextContent content = contentRepository.findById(id).orElseThrow(() -> new TextContentNotFound(id));

        checkSameUser(email, content);
        contentRepository.deleteById(id);
    }

    private void checkSameUser(String email, TextContent content) {
        if (!content.getOwner().getEmail().equals(email)) {
            throw new ContentAccessPermissionException();
        }
    }

    @Transactional
    public TextContentDto updateTextContent(String email, TextContentDto updateContent) {
        Optional<TextContent> currentContent = findExistingContent(updateContent);
        currentContent.ifPresent(content -> checkSameUser(email, content));
        TextContent content = currentContent.orElse(new TextContent());

        if (!content.getTitle().equals(updateContent.title())) {
            validateTitle(email, updateContent.title());
            content.setTitle(updateContent.title());
        }
        content.setContent(updateContent.content());
        content.setSubtitle(updateContent.subtitle());
        content.setImageHref(updateContent.imageHref());

        TextContent savedContent = contentRepository.save(content);
        return mapContentToContentDto(savedContent);
    }

    private void validateTitle(String email, String title) {
        if (!titleAvailable(title, email)) {
            throw new InvalidTextContentException("Duplicate title not allowed for user:" + email);
        }
    }

    @Transactional
    public void changeContentSharedStatus(Long contentId, String email, Boolean requestedShareStatus) {
        TextContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new TextContentNotFound(contentId));
        checkSameUser(email, content);

        content.setShared(requestedShareStatus);
    }

    public TextContentDto getPublicTextById(Long id) {
        TextContent content = contentRepository.findById(id).orElseThrow(() -> new TextContentNotFound(id));

        if (!content.isShared()) {
            throw new ContentAccessPermissionException("Content is not shared publicly");
        }
        return mapContentToContentDto(content);
    }

    public List<TextContentDto> getTextContent(String requestingUserEmail) {
        return contentRepository.findAllByOwner_Email(requestingUserEmail)
                .stream()
                .map(TextContentMapper::mapContentToContentDto)
                .toList();
    }


    private Optional<TextContent> findExistingContent(TextContentDto updateContent) {
        if (updateContent.id() != null) {
            return contentRepository.findById(updateContent.id());
        } else {
            return Optional.empty();
        }
    }


    public boolean titleAvailable(String title, String email) {
        return !contentRepository.existsByTitleAndOwner_Email(title, email);
    }
}
