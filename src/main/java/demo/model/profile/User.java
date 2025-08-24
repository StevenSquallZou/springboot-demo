package demo.model.profile;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


public class User {
    @Schema(name = "user id", description = "the unique user id", example = "1")
    @NotNull
    private Integer userId;

    @Schema(name = "username", description = "the username")
    @NotBlank
    private String username;

    @Schema(name = "passwordHash", description = "the hash of password")
    @NotBlank
    private String passwordHash;

    @Schema(name = "resourceSet", description = "the set of resources that the user has access to")
    private Set<String> resourceSet = new HashSet<>();


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


    public Set<String> getResourceSet() {
        return resourceSet;
    }


    public void setResourceSet(Set<String> resourceSet) {
        this.resourceSet = resourceSet;
    }


    public void addResource(String resource) {
        this.resourceSet.add(resource);
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
//                ", passwordHash='" + passwordHash + '\'' +
                ", resourceSet=" + resourceSet +
                '}';
    }

}
