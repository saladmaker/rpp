package gov.mf.rpp.portefeuille.movement.split;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author khaled
 */
public record SplitRequest(
        @NotBlank(message = "parent's name must not be blank")
        String name,
        @NotBlank(message = "main part's name must not be blank")
        String mainName,
        @NotBlank(message = "main part's code must not be blank")
        String mainCode,
        @NotNull(message = "split parts must not be null")
        @Size(min = 1, message = "there must be at least one part")
        List<@Valid @NotNull(message = "part must not be null") Part> parts) {

}
