package library.model;


import jakarta.persistence.*;
import library.repository.RentRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "client")
@Access(AccessType.FIELD)
@DiscriminatorColumn(name = "clientType")
@NamedQueries({
        @NamedQuery(name = "Client.findAll", query = "select c from Client c"),
        @NamedQuery(name = "Client.findByPersonalID", query = "select c from Client c where c.personalID = :personalID"),
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@NoArgsConstructor
public abstract class Client extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "client_id")
    private long id;
    @Column(unique = true)
    private String personalID;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Integer age;
    @Column
    @Setter
    private boolean isArchive;

    @Column
    @Setter
    private Float debt;


    public Client(String firstName, String lastName, String personalID, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        isArchive = false;
        this.debt = (float) 0.0;
    }

    public abstract Float getPenalty();

    public abstract Integer getMaxDays();

    public abstract Integer getMaxBooks();

}
