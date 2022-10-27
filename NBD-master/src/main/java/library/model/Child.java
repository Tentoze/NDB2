package library.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("child")
@NoArgsConstructor
public class Child extends Client {
    public Child(String firstName, String lastName, String personalID, Integer age) {
        super(firstName, lastName, personalID, age);
    }

    @Override
    public Float getPenalty() {
        return (float)(5*1.2);
    }

    @Override
    public Integer getMaxDays() {
        return 90;
    }

    @Override
    public Integer getMaxBooks() {
        return 3;
    }
}
