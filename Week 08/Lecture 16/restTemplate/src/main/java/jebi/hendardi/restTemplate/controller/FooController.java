package jebi.hendardi.restTemplate.controller;

import jebi.hendardi.restTemplate.model.Foo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/foos")
public class FooController {

    private Map<Long, Foo> fooRepository = new HashMap<>();
    private long currentId = 1;

    public FooController() {
        fooRepository.put(currentId, new Foo(currentId, "foo"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Foo> getFooByID(@PathVariable("id") long id){
        Foo foo = fooRepository.get(id);
        if (foo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foo);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Foo> createFoo(@RequestBody Foo foo) throws URISyntaxException {
        foo.setId(++currentId);
        fooRepository.put(foo.getId(), foo);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/v1/foos/" + foo.getId()));
        return ResponseEntity.created(headers.getLocation()).body(foo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Foo> updateFoo(@PathVariable("id") long id, @RequestBody Foo foo) {
        Foo fooToUpdate = fooRepository.get(id);
        if (fooToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        fooToUpdate.setName(foo.getName());
        fooRepository.put(id, fooToUpdate);
        return ResponseEntity.ok(fooToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Foo> deleteFoo(@PathVariable("id") long id) {
        Foo foo = fooRepository.remove(id);
        if (foo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foo);
    }
}
