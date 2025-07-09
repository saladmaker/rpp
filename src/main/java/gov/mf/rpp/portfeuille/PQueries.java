package gov.mf.rpp.portfeuille;

import jakarta.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.annotations.processing.HQL;

/**
 *
 * @author khaled
 */
public interface PQueries {
    
    StatelessSession session();
    
    default void createPortfeuille(@NotNull Portfeuille portfeuille){
        session().insert(portfeuille);
    }
    
}
