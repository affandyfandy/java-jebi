package jebi.hendardi.lecture5.mapper;

import java.text.SimpleDateFormat;

import jebi.hendardi.lecture5.dto.ContactDTO;
import jebi.hendardi.lecture5.model.Contact;

public class ContactMapper {

    public static ContactDTO toDTO(Contact contact) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDob = dateFormat.format(contact.getDob());

        return new ContactDTO(
                contact.getId(),
                contact.getName(),
                formattedDob,
                contact.getAge(),
                contact.getEmail()
        );
    }
}
