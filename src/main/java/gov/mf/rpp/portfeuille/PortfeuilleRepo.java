package gov.mf.rpp.portfeuille;

import java.util.List;
import java.util.Optional;

import org.hibernate.StatelessSession;

import gov.mf.rpp.portfeuille.rename.RenameRequest;
import gov.mf.rpp.portfeuille.rename.ValidRenameRequest;
import io.smallrye.common.constraint.NotNull;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Transactional
@Repository
public interface PortfeuilleRepo {

    StatelessSession session();

    @Save
    void createPortfeuille(Portfeuille namedPortfeuille);

    @Query("""
            select pf from Portfeuille as pf
            where pf.status in (ACTIVE, EVOLVING)
                    """)
    List<Portfeuille> relevantPortfeuilles();

    @Query("select pf from Portfeuille as pf where pf.name = :name")
    Optional<Portfeuille> portfeuilleByName(String name);

    @Query("""
            select case when (count(f) = 1) then true else false end
            from Portfeuille f where f.name = :oldName and f.status = ACTIVE
                """)
    boolean validRenameTarget(String oldName);

    @Query("""
        select case when (count(f) = 0) then true else false end
        from Portfeuille f where f.name = :newName and f.status = ACTIVE
            """)
    boolean validNewName(String newName);

    //TODO!
    // create a query for checking:
    // validate that portfeuille name exist
    // validate that all portfeuille names and codes aren't already taken

    public default Portfeuille renamePortfeuille(@Valid @NotNull @ValidRenameRequest RenameRequest renameRequest) {
        var portfeuille = portfeuilleByName(renameRequest.oldName()).orElseThrow();
        portfeuille.setStatus(PortfeuilleStatus.INACTIVE);
        var newPortfeuille = Portfeuille.of(renameRequest.newName(), portfeuille.getCode());
        newPortfeuille.addOriginatingSource(portfeuille);
        session().insert(newPortfeuille);
        session().update(portfeuille);
        return newPortfeuille;
    }

    // TODO
    // validate that name exist Jakrata Custom Validation
    // validate that all names and code aren't already taken Jakarta Custom
    // Validation
    // set active to false for old portfeuille
    // for each portfeuilleRequest create new portfeuille
    // public default void split(@Valid @NotNull PortfeuilleSplitRequest portfeuilleSplitRequest) {

    // }

}
