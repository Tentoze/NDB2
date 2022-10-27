package library.managers;

import jakarta.persistence.EntityManager;
import library.model.Book;
import library.model.Client;
import library.model.Rent;
import library.repository.BookRepository;
import library.repository.ClientRepository;
import library.repository.RentRepository;

import java.util.Date;
import java.util.List;

public class RentManager {
    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private BookRepository bookRepository;

    public RentManager(EntityManager entityManager) {
        this.rentRepository = new RentRepository(entityManager);
        this.clientRepository = new ClientRepository(entityManager);
        this.bookRepository = new BookRepository(entityManager);
    }

    public Rent rentBook(String personalID, String serialNumber) throws Exception {
        try {
            Client client = clientRepository.findByPersonalID(personalID);
            Book book = bookRepository.findBySerialNumber(serialNumber);
            checkIfBookCanBeRented(client, book);
            book.setRented(true);
            bookRepository.update(book);
            return rentRepository.add(new Rent(client, book));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIfBookCanBeRented(Client client, Book book) throws Exception {
        if (client.isArchive()) {
            throw new Exception("Client is archived");
        }
        if (book.isArchive()) {
            throw new Exception("Book is archived");
        }
        if (rentRepository.existsByBook(book)) {
            throw new Exception("Book is already rented");
        }
        if (client.getMaxBooks() < rentRepository.countByClient(client) + 1) {
            throw new Exception("Client have already rented max number of books");
        }
        if (client.getDebt() > 0) {
            throw new Exception("Client have to pay the debt");
        }
    }

    public void returnBook(String serialNumber) {
        try {
            Book book = bookRepository.findBySerialNumber(serialNumber);
            if (book == null) {
                throw new Exception("There is no book with that serial number");
            }
            Rent rent = rentRepository.findByBook(book);
            if (rent == null) {
                throw new Exception("There is no rent with this book");
            }
            if(isAfterEndTime(rent)){
                Client client = rent.getClient();
                client.setDebt(client.getDebt() + client.getPenalty());
                clientRepository.update(client);
            }
            book.setRented(false);
            bookRepository.update(book);
            rentRepository.remove(rent);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAfterEndTime(Rent rent){
        Date date = new Date();
        if(date.after(rent.getEndTime())){
            return true;
        }
        return false;
    }

    public Rent getRentByBook(String serialnumber) {
        return rentRepository.findByBook(bookRepository.findBySerialNumber(serialnumber));
    }

    public List<Rent> getRentByClient(String personalID){
        return rentRepository.findByClient(clientRepository.findByPersonalID(personalID));
    }



}
