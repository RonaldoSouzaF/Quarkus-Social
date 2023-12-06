package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Post;
import org.acme.model.User;
import org.acme.repository.FollowerRepository;
import org.acme.repository.PostRepository;
import org.acme.repository.UserRepository;
import org.acme.rest.dto.CreatePostRequest;
import org.acme.rest.dto.PostResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;
    private PostRepository repository;
    private Long followerId;

    @Inject
    public PostResource(
            UserRepository userRepository,
            PostRepository repository,
            FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response savePost(
            @PathParam("userId") Long userId, CreatePostRequest request){

        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);
        post.setDateTime(LocalDateTime.now());

        repository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts( @PathParam("userId") Long userId ){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerId);

        boolean follows = followerRepository.follows(follower, user);

        if (!follows){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("you can't see these post")
                    .build();

        }



        PanacheQuery<Post> query = repository.find(
                "user", Sort.by("dateTime", Sort.Direction.Descending)
                ,user);
        var list = query.list();


        var postResponselist = list.stream()
                .map(post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());

        return Response.ok(postResponselist).build();
    }

}