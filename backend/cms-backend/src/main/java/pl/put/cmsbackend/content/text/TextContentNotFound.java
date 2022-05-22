package pl.put.cmsbackend.content.text;

public class TextContentNotFound extends RuntimeException{

    public TextContentNotFound(Long contentId) {
        super("Content with id: " + contentId + " not found");
    }
}
