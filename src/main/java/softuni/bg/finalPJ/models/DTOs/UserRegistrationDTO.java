package softuni.bg.finalPJ.models.DTOs;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserRegistrationDTO {
    @NotNull
    @Length(min = 5, max = 20, message = "First name should be between 5 and 20 characters")
    private String firstName;

    @NotNull
    @Length(min = 5, max = 20, message = "Last name should be between 5 and 20 characters")
    private String lastName;

    @NotNull
    @Length(min = 5, max = 30, message = "Company name should be between 5 and 30 characters")
    private String companyName;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Passwords do not match")
    private String confirmPassword;

    @NotNull
    @Email
    private String email;
    public String getFirstName() {
        return firstName;
    }

    public UserRegistrationDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegistrationDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }


    public String getCompanyName() {
        return companyName;
    }

    public UserRegistrationDTO setCompanyName( String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegistrationDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public  String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword( String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public UserRegistrationDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + (password == null ? "N/A" : "[PROVIDED]") + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
