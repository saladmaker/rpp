package gov.mf.rpp.portfeuille;

import java.util.List;
import java.util.Optional;

import org.hibernate.StatelessSession;

import gov.mf.rpp.portfeuille.split.PortfeuilleSplitRequest;
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

    @Query("""
            SELECT pf FROM Portfeuille pf 
            WHERE pf.active IS TRUE
                    """)
    List<Portfeuille> activePortfeuille();

    @Save
    void createPortfeuille(Portfeuille namedPortfeuille);

    @Query("SELECT pf FROM Portfeuille as pf WHERE pf.name = :name")
    Optional<Portfeuille> portfeuilleByName(String name);

    //TODO 
    //create a query for checking:
    //validate that portfeuille name exist 
    //validate that all portfeuille names and codes aren't already taken

    
    
    public default Portfeuille rename(String name, String newName){
        var portfeuille = portfeuilleByName(name).orElseThrow();
        portfeuille.setActive(false);
        var newPortfeuille = Portfeuille.of(newName, portfeuille.getCode());
        newPortfeuille.addOriginatingSource(portfeuille);
        session().insert(newPortfeuille);
        session().update(portfeuille);
        return newPortfeuille;
    }

    //TODO
    //validate that name exist Jakrata Custom Validation
    //validate that all names and code aren't already taken Jakarta Custom Validation
    //set active to false for old portfeuille 
    //for each portfeuilleRequest create new portfeuille
    public default void split(@Valid @NotNull PortfeuilleSplitRequest portfeuilleSplitRequest){


    }

}
