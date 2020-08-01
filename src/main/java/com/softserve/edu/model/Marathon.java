package com.softserve.edu.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@ToString(exclude = {"sprints", "users"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The 'title' cannot be empty")
    private String title;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "marathon", cascade = CascadeType.REMOVE)
    private Set<Sprint> sprints = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "marathon_user", joinColumns = @JoinColumn(name = "marathon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marathon)) return false;

        Marathon marathon = (Marathon) o;

        return getTitle().equals(marathon.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
