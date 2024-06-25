package jebi.hendardi.lecture5.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jebi.hendardi.lecture5.dto.ContactDTO;
import jebi.hendardi.lecture5.model.Contact;
import jebi.hendardi.lecture5.repository.ContactRepository;
import jebi.hendardi.lecture5.mapper.ContactMapper;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/contacts")
@AllArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;

    // Get all contacts
    @GetMapping
    public ResponseEntity<List<ContactDTO>> listAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ContactDTO> contactDTOs = contacts.stream()
                                               .map(ContactMapper::toDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(contactDTOs);
    }

    // Get contact detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> findContact(@PathVariable("id") String id) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ContactDTO contactDTO = ContactMapper.toDTO(contactOpt.get());
        return ResponseEntity.ok(contactDTO);
    }

    // Create new contact
    @PostMapping
    public ResponseEntity<ContactDTO> saveContact(@RequestBody Contact contact) {
        if (contact.getId() == null || contact.getId().isEmpty()) {
            contact.setId(UUID.randomUUID().toString());
        }

        Contact savedContact = contactRepository.save(contact);
        ContactDTO contactDTO = ContactMapper.toDTO(savedContact);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactDTO);
    }

    // Update contact by ID
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable("id") String id,
                                                 @RequestBody Contact contactForm) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Contact contact = contactOpt.get();
        contact.setName(contactForm.getName());
        contact.setAge(contactForm.getAge());
        contact.setEmail(contactForm.getEmail());
        contact.setDob(contactForm.getDob());

        Contact updatedContact = contactRepository.save(contact);
        ContactDTO contactDTO = ContactMapper.toDTO(updatedContact);
        return ResponseEntity.ok(contactDTO);
    }

    // Delete contact by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable("id") String id) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        contactRepository.delete(contactOpt.get());
        return ResponseEntity.noContent().build();
    }
}
