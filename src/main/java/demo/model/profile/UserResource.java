package demo.model.profile;


public class UserResource {
    private Integer userId;
    private String username;
    private String passwordHash;
    private String resourceName;


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPasswordHash() {
        return passwordHash;
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

}
