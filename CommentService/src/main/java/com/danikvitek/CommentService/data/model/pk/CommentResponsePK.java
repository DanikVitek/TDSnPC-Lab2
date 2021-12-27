package com.danikvitek.CommentService.data.model.pk;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponsePK implements Serializable {
    @Column(name = "parent_id", nullable = false, updatable = false)
    @Id
    private Long parentId;
    
    @Column(name = "response_id", nullable = false, updatable = false)
    @Id
    private Long responseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentResponsePK that = (CommentResponsePK) o;

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
