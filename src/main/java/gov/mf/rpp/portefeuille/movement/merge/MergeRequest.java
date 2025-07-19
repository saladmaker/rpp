package gov.mf.rpp.portefeuille.movement.merge;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author khaled
 */
public record MergeRequest(
        @NotBlank(message = "merge name must not be blank",
                groups = ValidationSequence.SanityCheck.class) String name,
        @NotBlank(message = "merge code must not be blank",
                groups = ValidationSequence.SanityCheck.class) String code,
        
        @NotNull(message = "target list must not be null",
                groups = ValidationSequence.SanityCheck.class)
        @NotEmpty(message = "target list must not be null",
                groups = ValidationSequence.SanityCheck.class)
        List<@NotBlank(message = "target must not be blank",
                groups = ValidationSequence.SanityCheck.class)String> targetNames) {}
