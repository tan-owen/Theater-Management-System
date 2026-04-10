public interface Discussable {
    void addComment(User author, String message);
    String getDiscussion();
}