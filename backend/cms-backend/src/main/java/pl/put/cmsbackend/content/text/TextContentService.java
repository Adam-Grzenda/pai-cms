package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.content.exception.ContentAccessPermissionException;
import pl.put.cmsbackend.content.exception.InvalidTextContentException;
import pl.put.cmsbackend.content.exception.TextContentNotFound;
import pl.put.cmsbackend.content.api.TextContentDto;
import pl.put.cmsbackend.content.text.db.TextContent;
import pl.put.cmsbackend.content.text.db.TextContentRepository;

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
        validateTitle(email, textContent);

        TextContent content = new TextContent(user, textContent.title(), textContent.subtitle(), textContent.content());
        TextContent savedContent = contentRepository.save(content);

        return mapContentToContentDto(savedContent);
    }

    @Transactional
    public void deleteTextContent(String email, Long id) {
        TextContent content = contentRepository.findById(id).orElseThrow(() -> new TextContentNotFound(id));

        checkSameUser(email, content.getOwner().getId());
        contentRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
    public void changeContentSharedStatus(Long contentId, String email, Boolean requestedShareStatus) {
        TextContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new TextContentNotFound(contentId));
        checkSameUser(email, content.getOwner().getId());

        content.setShared(requestedShareStatus);
    }

    public TextContentDto getPublicTextById(Long id) {
        TextContent content = contentRepository.findById(id).orElseThrow(() -> new TextContentNotFound(id));
        return mapContentToContentDto(content);
    }

    public Page<TextContentDto> getTextContentPaginated(String requestingUserEmail, Pageable pageable) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        return contentRepository.findAllByOwner_id(user.getId(), pageable)
                .map(TextContentMapper::mapContentToContentDto);
    }

    private Optional<TextContent> findExistingContent(TextContentDto updateContent) {
        if (updateContent.id() != null) {
            return contentRepository.findById(updateContent.id());
        } else {
            return Optional.empty();
        }
    }


    private void validateTitle(String email, TextContentDto textContent) {
        if (contentRepository.findByOwner_EmailAndTitle(email, textContent.title()).isPresent()) {
            throw new InvalidTextContentException("Duplicate title: " + textContent.content() + " is not allowed");
        }
    }

    private void checkSameUser(String requestingUserEmail, Long ownerId) {
        AppUser user = appUserService.findUserByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException(requestingUserEmail));

        if (!user.getId().equals(ownerId)) {
            throw new ContentAccessPermissionException();
        }
    }


}
