package pl.put.cmsbackend.content.api;

import pl.put.cmsbackend.content.text.db.ContentTag;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public record TextContentDto(Long id, @NotBlank(message = "Title is mandatory") String title, String subtitle,
                             @NotBlank(message = "Content is mandatory") String content, Set<ContentTag> tags,
                             Boolean isShared) {

}
