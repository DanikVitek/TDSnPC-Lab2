package com.danikvitek.CommentService.data.model.entity;

import com.danikvitek.CommentService.data.model.pk.CommentResponsePK;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_responses", schema = "course_project")
@IdClass(CommentResponsePK.class)
public class CommentResponse {
    @Id
    @Column(name = "parent_id", nullable = false, updatable = false)
    private Long parentId;
    
    @Id
    @Column(name = "response_id", nullable = false, updatable = false)
    private Long responseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentResponse that = (CommentResponse) o;

        if (!Objects.equals(parentId, that.parentId)) return false;
        if (!Objects.equals(responseId, that.responseId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parentId != null ? parentId.hashCode() : 0;
        result = 31 * result + (responseId != null ? responseId.hashCode() : 0);
        return result;
    }
}
