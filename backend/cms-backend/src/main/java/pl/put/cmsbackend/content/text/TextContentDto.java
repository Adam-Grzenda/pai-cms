package pl.put.cmsbackend.content.text;

import javax.validation.constraints.NotBlank;

public record TextContentDto(Long id, @NotBlank(message = "Title is mandatory") String title, String subtitle,
                             @NotBlank(message = "Content is mandatory") String content) {

}
