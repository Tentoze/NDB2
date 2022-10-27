package library.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Persistence;
import library.managers.BookManager;
import library.managers.ClientManager;
import library.managers.RentManager;
import library.model.Book;
import library.model.Child;
import library.model.Client;
import library.model.Rent;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class mainTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    private ClientManager clientManager;

    private BookManager bookManager;

    private RentManager rentManager;

    @BeforeEach
    void beforeEach() {
        if (emf != null) {
            emf.close();
        }
        emf = Persistence.createEntityManagerFactory("POSTGRES");
        em = emf.createEntityManager();
        this.clientManager = new ClientManager(em);
        this.bookManager = new BookManager(em);
        this.rentManager = new RentManager(em);
    }

    @Test
    void addSameObjects() throws Exception {

        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);

        bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        rentManager.rentBook("2313123341", "2131341");
        assertThrows(Exception.class, () -> {
            rentManager.rentBook("2313123341", "2131341");
        });
        assertThrows(Exception.class, () -> {
            bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        });
        assertThrows(Exception.class, () -> {
            clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        });
    }

    @Test
    void addCheckRegister() throws Exception {
        ;
        var client1 = clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var client2 = clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        var book1 = bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        var rent1 = rentManager.rentBook("2313123341", "2131341");
        assertEquals(clientManager.getClient("231312341"), client1);
        assertEquals(clientManager.getClient("2313123341"), client2);
        assertEquals(bookManager.getBook("2131341"), book1);
        assertEquals(rentManager.getRentByBook("2131341"), rent1);
    }

    @Test
    void checkTooMuchRents() throws Exception {
        var client1 = clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var client2 = clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        var book1 = bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        var book2 = bookManager.registerBook("najlepssza ksiega2", "najlepszy autor2", "21313413", "genre");
        var book3 = bookManager.registerBook("najlepssza ksiega3", "najlepszy autor3", "21313414", "genre");
        var book4 = bookManager.registerBook("najlepssza ksiega4", "najlepszy autor4", "21313415", "genre");
        var book5 = bookManager.registerBook("najlepssza ksiega5", "najlepszy autor5", "21313411", "genre");
        var book6 = bookManager.registerBook("najlepssza ksiega5", "najlepszy autor5", "21313416", "genre");
        rentManager.rentBook("2313123341", "21313412");
        rentManager.rentBook("2313123341", "21313413");
        rentManager.rentBook("2313123341", "21313414");
        assertThrows(Exception.class, () -> {
            rentManager.rentBook("2313123341", "21313416");
        });
    }

    @Test
    void checkUnregisterClientAndBook() throws Exception {
        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        bookManager.registerBook("najlepssza ksiega2", "najlepszy autor2", "21313413", "genre");
        clientManager.unregisterClient("231312341");
        bookManager.unregisterBook("21313412");
        assertTrue(clientManager.getClient("231312341").isArchive());
        assertTrue(bookManager.getBook("21313412").isArchive());
        rentManager.rentBook("2313123341", "21313413");
        assertThrows(Exception.class, () -> {
            clientManager.unregisterClient("2313123341");
        });
        assertThrows(Exception.class, () -> {
            bookManager.unregisterBook("21313413");
        });
    }

    @Test
    void optymisticLockTest() throws Exception {
        ClientRepository clientRepository = new ClientRepository(em);
        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        Client client1 = clientManager.getClient("231312341");
        Client client2 = emf.createEntityManager().find(Client.class, 1L);

        client1.setDebt((float)3.25);
        clientRepository.update(client1);
        assertThrows(OptimisticLockException.class, () -> {
            client2.setDebt((float)4.0);
           clientRepository.update(client2);
        });
    }
    @Test
    void optymisticLockTest2() throws Exception {
        RentRepository rentRepository = new RentRepository(em);
        BookRepository bookRepository = new BookRepository(em);
        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Client client = clientManager.getClient("231312341");
        Client client2 = clientManager.getClient("2313123341");
        Book book = bookManager.getBook("21313412");
        Book book2 = emf.createEntityManager().find(Book.class, 3L);
        Rent rent = new Rent(client,book);
        rentRepository.add(rent);
        book.setRented(true);
        bookRepository.update(book);
        assertThrows(OptimisticLockException.class, () -> {
            book2.setRented(true);
            bookRepository.update(book2);
            rentRepository.add(new Rent(client2,book2));
        });
    }
}
