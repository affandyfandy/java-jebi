package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.client.JSONPlaceHolderClient;
import jebi.hendardi.spring.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private JSONPlaceHolderClient jsonPlaceHolderClient;

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return jsonPlaceHolderClient.getPosts();
    }

    @GetMapping("/posts/{postId}")
    public Post getPostById(@PathVariable Long postId) {
        return jsonPlaceHolderClient.getPostById(postId);
    }
}
