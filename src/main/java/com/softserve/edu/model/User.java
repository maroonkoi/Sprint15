package com.softserve.edu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    public enum Role {
        MENTOR, TRAINEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank
    private String password;

    @ToString.Exclude
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Marathon> marathons;

    @ToString.Exclude
    @OneToMany(mappedBy = "trainee")
    private List<Progress> progresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!getFirstName().equals(user.getFirstName())) return false;
        if (!getLastName().equals(user.getLastName())) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        if (getRole() != user.getRole()) return false;
        return getPassword().equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getRole().hashCode();
        result = 31 * result + getPassword().hashCode();
        return result;
    }
}

