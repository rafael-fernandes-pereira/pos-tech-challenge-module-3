package com.github.rafaelfernandes.review.adapter.in.web.response;

import com.github.rafaelfernandes.common.enums.Like;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ReviewResponse(

        @Schema(description = "Review Id", implementation = UUID.class)
        String reviewId,

        @Schema(description = "Name of Client")
        String name,

        @Schema(description = "Like / Dislike to Service", implementation = Like.class)
        String like_type,

        @Schema(description = "Comment to service")
        String commment

) {
}
