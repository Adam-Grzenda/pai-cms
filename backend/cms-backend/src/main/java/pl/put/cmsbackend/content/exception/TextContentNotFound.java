package pl.put.cmsbackend.content.exception;

public class TextContentNotFound extends RuntimeException {

    public TextContentNotFound(Long contentId) {
        super("Content with id: " + contentId + " not found");
    }
}
