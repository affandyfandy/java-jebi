package jebi.hendardi.webClient.model;

import lombok.Data;

@Data
public class Reader {
    private int id;
    private String name;
    private Book favoriteBook;
}
