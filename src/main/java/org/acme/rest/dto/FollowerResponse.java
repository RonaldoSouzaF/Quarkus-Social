package org.acme.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.model.Follower;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerResponse {

    private String name;
    private Long id;

    public FollowerResponse(Follower follower) {
        this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
