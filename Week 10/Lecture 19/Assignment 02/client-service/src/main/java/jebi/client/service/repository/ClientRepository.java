package jebi.client.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.client.service.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByNameContaining(String name);
}
