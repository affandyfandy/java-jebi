package jebi.client.service.service;

import org.springframework.stereotype.Service;
import jebi.client.service.repository.ClientRepository;
import jebi.client.service.entity.Client;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client client) {
        Client existingClient = getClientById(id);
        if (existingClient != null) {
            existingClient.setName(client.getName());
            existingClient.setEmail(client.getEmail());
            return clientRepository.save(existingClient);
        }
        return null;
    }

    public String deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return "Client with ID " + id + " successfully deleted";
        }
        return "Client with ID " + id + " not found";
    }

    public String searchClientsByName(String name) {
        List<Client> clients = clientRepository.findByNameContaining(name);
        if (clients.isEmpty()) {
            return "No clients found with name containing '" + name + "'";
        }
        return clients.toString();
    }
}
