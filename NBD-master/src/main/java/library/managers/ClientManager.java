package library.managers;

import jakarta.persistence.EntityManager;
import library.model.Adult;
import library.model.Child;
import library.model.Client;
import library.repository.ClientRepository;
import library.repository.RentRepository;

import java.util.List;

public class ClientManager {
    public ClientRepository clientRepository;
    public RentRepository rentRepository;

    public ClientManager(EntityManager entityManager) {
        this.clientRepository = new ClientRepository(entityManager);
        this.rentRepository = new RentRepository(entityManager);
    }

    public Client getClient(String personalID) {
        return clientRepository.findByPersonalID(personalID);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client registerClient(String firstname, String lastname, String personalID, int age) {
        if (age < 18) {
            return clientRepository.add(new Child(firstname, lastname, personalID, age));
        } else {
            return clientRepository.add(new Adult(firstname, lastname, personalID, age));
        }
    }

    public void unregisterClient(String personalID) throws Exception {
        Client client = clientRepository.findByPersonalID(personalID);
        if (client.getDebt() > 0) {
            throw new Exception("Client have to pay debt firstly");
        }
        if (!rentRepository.findByClient(client).isEmpty()) {
            throw new Exception("Client have to pay return books firstly");
        }
        client.setArchive(true);
        clientRepository.update(client);
    }

    public void payDebt(String personalID, Float amount) throws Exception {
        Client client = clientRepository.findByPersonalID(personalID);
      //  clientRepository.updateDebtByPersonalID(personalID, client.getDebt() - amount);
    }
    public Float getDebt(String personalID) {
        Client client = clientRepository.findByPersonalID(personalID);
        return client.getDebt();
    }
}
