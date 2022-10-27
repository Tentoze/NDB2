package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@Access(AccessType.FIELD)
@Embeddable
public abstract class AbstractEntity implements Serializable {

    @Column(name = "entity_id")
    @NotNull
    protected UUID entityId = UUID.randomUUID();

    @Version
    @NotNull
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    protected long version;
}
