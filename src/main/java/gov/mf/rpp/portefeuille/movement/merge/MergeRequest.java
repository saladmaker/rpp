package gov.mf.rpp.portefeuille.movement.merge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author khaled
 */
public record MergeRequest(
        @NotBlank(message = "merge name must not be blank") String name,
        @NotBlank(message = "merge code must not be blank") String code,
        
        @NotNull(message = "target list must not be null")
        @NotEmpty(message = "target list must not be null")
        List<@NotBlank(message = "target must not be blank")String> targetNames) {}
