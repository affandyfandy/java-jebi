package jebi.hendardi.restTemplate.client;

import jebi.hendardi.restTemplate.model.Foo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;

public class RestTemplateClient {
    private static final String fooResourceUrl = "http://localhost:8080/api/v1/foos/";
    private final RestTemplate restTemplate = new RestTemplate();

    public void getPlainJson() {
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
        System.out.println("GET JSON: " + response.getBody());
    }

    public void getPojo() {
        Foo foo = restTemplate.getForObject(fooResourceUrl + "/1", Foo.class);
        System.out.println("GET POJO: " + foo.getName());
    }

    public void getHeaders() {
        HttpHeaders httpHeaders = restTemplate.headForHeaders(fooResourceUrl);
        System.out.println("HEAD Headers: " + httpHeaders);
    }

    public void createResource() {
        Foo newFoo = new Foo(0, "New Foo");
        Foo foo = restTemplate.postForObject(fooResourceUrl, newFoo, Foo.class);
        System.out.println("Created Foo: " + foo.getId());
    }

    public void createResourceWithLocation() throws Exception {
        Foo newFoo = new Foo(0, "Another Foo");
        URI location = restTemplate.postForLocation(fooResourceUrl, newFoo);
        System.out.println("Resource created at: " + location);
    }

    public void updateResource() {
        Foo updatedFoo = new Foo(1, "Updated Foo");
        restTemplate.put(fooResourceUrl + "/1", updatedFoo);
        System.out.println("Resource updated.");
    }

    public void deleteResource() {
        restTemplate.delete(fooResourceUrl + "/1");
        System.out.println("Resource deleted.");
    }

    public void getOptions() {
        Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(fooResourceUrl);
        System.out.println("OPTIONS: " + optionsForAllow);
    }

    public void createResourceWithExchange() {
        Foo newFoo = new Foo(0, "New Foo via Exchange");
        HttpEntity<Foo> request = new HttpEntity<>(newFoo);
        ResponseEntity<Foo> response = restTemplate.exchange(fooResourceUrl, HttpMethod.POST, request, Foo.class);
        System.out.println("Exchange: " + response.getStatusCode());
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        return factory;
    }

    public static void main(String[] args) {
        RestTemplateClient client = new RestTemplateClient();

        client.getPlainJson();
        client.getPojo();
        client.getHeaders();
        client.createResource();
        try {
            client.createResourceWithLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.updateResource();
        client.deleteResource();
        client.getOptions();
        client.createResourceWithExchange();
    }
}
