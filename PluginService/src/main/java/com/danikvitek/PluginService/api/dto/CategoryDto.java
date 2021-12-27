package com.danikvitek.PluginService.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class CategoryDto implements Serializable {
    @Positive
    private Short id;
   
    @Length(max = 30)
    private String title;
}
