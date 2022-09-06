package app.libraryapp;

public final class UserLoggedProfile {
    private User user;

    private final static UserLoggedProfile instance = new UserLoggedProfile();

    private UserLoggedProfile() {}

    public static UserLoggedProfile getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
