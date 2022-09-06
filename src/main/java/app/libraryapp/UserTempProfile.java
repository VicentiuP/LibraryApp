package app.libraryapp;

public final class UserTempProfile {
    private User user;

    private final static UserTempProfile instance = new UserTempProfile();

    private UserTempProfile() {}

    public static UserTempProfile getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
