package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author khaled
 */
@ValidSplitRequest(groups = ValidationSequence.BusinessCheck.class)
public record SplitRequest(
        @NotBlank(message = "parent's name must not be blank", groups = ValidationSequence.SanityCheck.class)
        String name,
        @NotBlank(message = "main part's name must not be blank", groups = ValidationSequence.SanityCheck.class)
        String mainName,
        @NotBlank(message = "main part's code must not be blank", groups = ValidationSequence.SanityCheck.class)
        String mainCode,
        @NotNull(message = "split parts must not be null", groups = ValidationSequence.SanityCheck.class)
        @Size(min = 1, message = "there must be at least one part", groups = ValidationSequence.SanityCheck.class)
        List<@NotNull(message = "part must not be null", groups = ValidationSequence.SanityCheck.class)
        @Valid Part> parts) {

}
