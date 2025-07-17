package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author khaled
 */
public record Part(
        @NotBlank (message = "part's name must not be null", groups = ValidationSequence.SanityCheck.class)
        String name,
        @NotBlank (message = "part's code must not be null or blank", groups = ValidationSequence.SanityCheck.class)
        String code){}
