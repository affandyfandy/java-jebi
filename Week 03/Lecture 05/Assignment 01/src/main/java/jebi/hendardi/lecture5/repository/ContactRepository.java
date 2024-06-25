package jebi.hendardi.lecture5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jebi.hendardi.lecture5.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
