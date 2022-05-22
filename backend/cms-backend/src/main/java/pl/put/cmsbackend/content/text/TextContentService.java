package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.UserNotFoundException;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.content.ContentAccessPermissionException;

@Service
@RequiredArgsConstructor
public class TextContentService {

    private final TextContentRepository contentRepository;
    private final AppUserService appUserService;


    public TextContentDto addUserTextContent(String email, TextContentDto textContent) {
        AppUser user = appUserService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        validateContent(email, textContent, user);

        TextContent content = new TextContent(user, textContent.title(), textContent.subtitle(), textContent.content());

        TextContent savedContent = contentRepository.save(content);

        return new TextContentDto(savedContent.getTitle(), savedContent.getSubtitle(), savedContent.getContent());
    }

    private void validateContent(String email, TextContentDto textContent, AppUser user) {
        if (contentRepository.findByOwner_EmailAndTitle(user.getEmail(), textContent.title()).isPresent()) {
            throw new InvalidTextContentException(
                    "Duplicate title: " + textContent.content() + " is not allowed for user: " + email + " content");
        }
    }

    public Page<TextContentDto> getTextContentPaginated(String requestingUserEmail, Long ownerId, Pageable pageable) {
        checkSameUser(requestingUserEmail, ownerId);

        return contentRepository.findAllByOwner_id(ownerId, pageable)
                .map(content -> new TextContentDto(content.getTitle(), content.getSubtitle(), content.getContent()));
    }

    private void checkSameUser(String requestingUserEmail, Long ownerId) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        if (!user.getId().equals(ownerId)) {
            throw new ContentAccessPermissionException();
        }
    }
}
