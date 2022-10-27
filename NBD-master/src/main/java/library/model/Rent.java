package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import library.model.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "rent")
@NamedQueries({
        @NamedQuery(name = "Rent.findByClient", query = "select r from Rent r where r.client = :client"),
        @NamedQuery(name = "Rent.findAll", query = "select r from Rent r"),
        @NamedQuery(name = "Rent.findByBook", query = "select r from Rent r where r.book = :book"),
        @NamedQuery(name = "Rent.countByClient", query = "select count(r) from Rent r where r.client = :client"),
        @NamedQuery(name = "Rent.existsByBook", query = "select (count(r) > 0) from Rent r where r.book = :book")
})
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Rent extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rent_id")
    private UUID id;
    @Column
    private Date beginTime;
    @Setter
    @Column
    private Date endTime;
    @ManyToOne
    @JoinColumn
    @NotNull
    private Client client;

    @OneToOne
    @JoinColumn
    @NotNull
    private Book book;

    public Rent(Client client, Book book) {
        this.client = client;
        this.beginTime = new Date();
        this.book = book;
        this.endTime = calculateEndTime(beginTime);

    }
    private Date calculateEndTime(Date beginTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        calendar.add(Calendar.DATE,client.getMaxDays());
        return calendar.getTime();
    }
}
