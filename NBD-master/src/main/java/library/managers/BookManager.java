package library.managers;

import jakarta.persistence.EntityManager;
import library.model.Book;
import library.repository.BookRepository;
import library.repository.RentRepository;

import java.util.List;

public class BookManager {
    private BookRepository bookRepository;
    private RentRepository rentRepository;

    public BookManager(EntityManager entityManager) {
        this.bookRepository = new BookRepository(entityManager);
        this.rentRepository = new RentRepository(entityManager);
    }

    public Book getBook(String serialNumber) {
        return bookRepository.findBySerialNumber(serialNumber);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book registerBook(String title, String author, String serialNumber, String genre) {
        return bookRepository.add(new Book(title,author,serialNumber,genre));
    }

    public void unregisterBook(String serialNumber) throws Exception {
        Book book = bookRepository.findBySerialNumber(serialNumber);
        if(rentRepository.existsByBook(book)){
            throw new Exception("This book is already rented");
        }
        book.setArchive(true);
        bookRepository.update(book);
    }

}
