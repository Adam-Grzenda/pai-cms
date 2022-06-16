package pl.put.cmsbackend.content.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.put.cmsbackend.auth.token.TokenService;
import pl.put.cmsbackend.content.text.IndexedTextContentService;
import pl.put.cmsbackend.content.text.TextContentService;
import pl.put.cmsbackend.content.text.db.ContentTag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContentController {

    private final TextContentService textContentService;
    private final IndexedTextContentService indexedTextContentService;
    private final TokenService tokenService;

    @PostMapping("/texts")
    @ResponseStatus(HttpStatus.CREATED)
    public TextContentDto addTextContent(@RequestBody TextContentDto textContent, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return textContentService.addTextContent(username, textContent);
    }

    @GetMapping("/texts")
    public List<TextContentDto> getAllUserTextContent(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return textContentService.getTextContent(username);
    }

    @GetMapping("/public/texts/{id}")
    public TextContentDto getPublicTextById(@PathVariable Long id, @RequestParam String token) {
        tokenService.verifyContentToken(token, id);
        return textContentService.getPublicTextById(id);
    }

    @DeleteMapping("/texts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTextContentById(@PathVariable Long id, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        textContentService.deleteTextContent(username, id);
    }

    @PutMapping("/texts")
    public TextContentDto updateTextContent(@RequestBody TextContentDto textContentDto, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return textContentService.updateTextContent(username, textContentDto);
    }

    @PostMapping("/texts/{id}/share")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<String>> shareContent(@PathVariable Long id, @RequestParam Boolean share, @RequestParam(defaultValue = "5") Long validForDays, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        textContentService.changeContentSharedStatus(id, username, share);
        if (share.equals(Boolean.TRUE)) {
            String contentToken = tokenService.generateContentToken(id, validForDays);
            return ResponseEntity.ok(Optional.of(contentToken));
        } else {
            return ResponseEntity.ok(Optional.empty());
        }
    }

    @GetMapping("/texts/search")
    public List<TextContentDto> searchTextContentByKeyword(@RequestParam(required = false) String keyword, @RequestParam(required = false) List<ContentTag> tags, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return indexedTextContentService.findTextContentByKeyword(keyword,tags, username);
    }

    @GetMapping("/texts/tags")
    public ContentTag[] getAvailableContentTags() {
        return ContentTag.values();
    }

    @PostMapping("/texts/title")
    public ResponseEntity<Boolean> checkTitleAvailable(@RequestParam String title, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return ResponseEntity.ok(textContentService.titleAvailable(title, username));
    }

}
