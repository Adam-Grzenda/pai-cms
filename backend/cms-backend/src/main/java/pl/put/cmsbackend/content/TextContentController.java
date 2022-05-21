package pl.put.cmsbackend.content;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.put.cmsbackend.content.text.TextContentDto;
import pl.put.cmsbackend.content.text.TextContentService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TextContentController {

    private final TextContentService textContentService;

    @PostMapping("/texts")
    @ResponseStatus(HttpStatus.CREATED)
    public TextContentDto addTextContent(@RequestBody TextContentDto textContent, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return textContentService.addUserTextContent(username, textContent);
    }

    @GetMapping("/user/{userId}/texts/")
    public List<TextContentDto> getAllTextContent(@PathVariable Long userId, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return textContentService.getAllTextContent(username, userId);
    }

}
