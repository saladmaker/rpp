package gov.mf.rpp.portfeuille.split;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PortfeuilleSplitRequest(@NotNull @NotBlank String portfeuilleName,
 @NotEmpty List<PortfeuilleSplitRequestPart> childPortfeuilles) {
    
}
