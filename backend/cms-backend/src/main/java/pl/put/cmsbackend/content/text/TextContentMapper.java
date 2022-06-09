package pl.put.cmsbackend.content.text;

import pl.put.cmsbackend.content.text.db.TextContent;

public class TextContentMapper {
    public static TextContentDto mapContentToContentDto(TextContent savedContent) {
        return new TextContentDto(savedContent.getId(), savedContent.getTitle(), savedContent.getSubtitle(), savedContent.getContent());
    }

}
