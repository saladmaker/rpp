package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.create.ValidCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author khaled
 */
@ValidSplitRequest
public record SplitRequest(
        @NotBlank(message = "parent must not be blank") String name,
        @NotBlank(message = "main part's name must not be blank") String mainName,
        @NotBlank(message = "main part's code must not be blank") String mainCode,
        @NotNull(message = "split parts must not be null")
        @Size(min = 1, message = "there must be at least 2 or more parts")
        List<@ValidCreateRequest CreateRequest> parts) {   
}
