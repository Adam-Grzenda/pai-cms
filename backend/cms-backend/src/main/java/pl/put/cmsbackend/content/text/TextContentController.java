package pl.put.cmsbackend.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user/")
@RequiredArgsConstructor
public class TextContentController {

    private final TextContentService textContentService;

    @GetMapping("/{id}/text")
    public List<TextContentDto> getAllUserTexts(@PathVariable Long id) {
        return textContentService.findAllByOwnerId(id);
    }
}
