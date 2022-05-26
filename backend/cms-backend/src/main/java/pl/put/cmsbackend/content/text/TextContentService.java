package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.content.exception.ContentAccessPermissionException;
import pl.put.cmsbackend.content.exception.InvalidTextContentException;
import pl.put.cmsbackend.content.exception.TextContentNotFound;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextContentService {

    private final TextContentRepository contentRepository;
    private final AppUserService appUserService;


    public TextContentDto addUserTextContent(String email, TextContentDto textContent) {
        AppUser user = appUserService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        validateTitle(email, textContent);

        TextContent content = new TextContent(user, textContent.title(), textContent.subtitle(), textContent.content());
        TextContent savedContent = contentRepository.save(content);

        return mapContentToContentDto(savedContent);
    }

    private void validateTitle(String email, TextContentDto textContent) {
        if (contentRepository.findByOwner_EmailAndTitle(email, textContent.title()).isPresent()) {
            throw new InvalidTextContentException("Duplicate title: " + textContent.content() + " is not allowed");
        }
    }

    public Page<TextContentDto> getTextContentPaginated(String requestingUserEmail, Pageable pageable) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        return contentRepository.findAllByOwner_id(user.getId(), pageable).map(this::mapContentToContentDto);
    }

    private void checkSameUser(String requestingUserEmail, Long ownerId) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        if (!user.getId().equals(ownerId)) {
            throw new ContentAccessPermissionException();
        }
    }

    public void deleteTextContent(String requestingUserEmail, Long id) {
        TextContent content = contentRepository.findById(id).orElseThrow(() -> new TextContentNotFound(id));

        checkSameUser(requestingUserEmail, content.getOwner().getId());
        contentRepository.deleteById(id);
    }

    public TextContentDto updateTextContent(String email, TextContentDto updateContent) {

        Optional<TextContent> currentContent = findExistingContent(updateContent);
        currentContent.ifPresent(content -> checkSameUser(email, content.getOwner().getId()));

        TextContent content = currentContent.orElse(new TextContent());

        if (!content.getTitle().equals(updateContent.title())) {
            validateTitle(email, updateContent);
            content.setTitle(updateContent.title());
        }
        content.setContent(updateContent.content());
        content.setSubtitle(updateContent.subtitle());

        TextContent savedContent = contentRepository.save(content);

        return mapContentToContentDto(savedContent);
    }

    private Optional<TextContent> findExistingContent(TextContentDto updateContent) {
        if (updateContent.id() != null) {
            return contentRepository.findById(updateContent.id());
        } else {
            return Optional.empty();
        }
    }

    private TextContentDto mapContentToContentDto(TextContent savedContent) {
        return new TextContentDto(savedContent.getId(), savedContent.getTitle(), savedContent.getSubtitle(),
                savedContent.getContent());
    }

}
