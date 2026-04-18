package entity;

/**
 * Interface for objects that support discussions/comments
 */
public interface Discussable {
    void addComment(User author, String message);

    String getDiscussion();
}
