package demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class ActorDto {
    @NotNull(message = "actorId cannot be empty")
    private Integer actorId;
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;


    public Integer getActorId() {
        return actorId;
    }


    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
