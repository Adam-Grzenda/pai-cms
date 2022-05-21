package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.UserNotFoundException;
import pl.put.cmsbackend.content.ContentAccessPermissionException;
import pl.put.cmsbackend.content.shared.SharedContentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TextContentService {

    private final TextContentRepository contentRepository;
    private final SharedContentRepository sharedContentRepository;
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

    public List<TextContentDto> getAllTextContent(String requestingUserEmail, Long ownerId) {
        checkSameUser(requestingUserEmail, ownerId);

        return contentRepository.findAllByOwner_id(ownerId)
                .stream()
                .map(content -> new TextContentDto(content.getTitle(), content.getSubtitle(), content.getContent()))
                .toList();
    }

    private void checkSameUser(String requestingUserEmail, Long ownerId) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        if (!user.getId().equals(ownerId)) {
            throw new ContentAccessPermissionException();
        }
    }
}
