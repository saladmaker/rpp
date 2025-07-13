package gov.mf.rpp.portefeuille;


import java.util.List;
import java.util.Optional;

import org.hibernate.StatelessSession;

import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;
import jakarta.data.repository.Update;
import jakarta.validation.constraints.NotNull;

/**
 * Repository interface for managing {@link Portefeuille} entities.
 * Provides persistence operations and queries related to portefeuille lifecycle,
 * name uniqueness checks, and renaming logic.
 */
@Repository
public interface PortefeuilleRepo {

    /**
     * Provides access to a stateless Hibernate session for performing
     * low-level insert/update operations outside of standard JPA behavior.
     *
     * @return the current {@link StatelessSession} instance
     */
    StatelessSession session();

    /**
     * Persists a new portefeuille in the database.
     *
     * @param portfeuille the portefeuille to create
     * @return the persisted portefeuille
     */
    @Save
    Portefeuille createPortfeuille(@NotNull Portefeuille portfeuille);
    
    @Update
    Portefeuille update(@NotNull Portefeuille portefeuille);

    /**
     * Verifies whether a new portefeuille with the given name or code
     * can be safely created, ensuring no conflicts with active portefeuilles.
     *
     * @param name the desired portefeuille name
     * @param code the desired portefeuille code
     * @return {@code true} if no active portefeuille with the same name or code exists; {@code false} otherwise
     */
    @Query("""
           select case when (count(f) = 0) then true else false end
           from Portefeuille f
           where f.status = ACTIVE
           and (f.name = :name or f.code = :code)
           """)
    boolean validNewPortefeuille(String name, String code);
    
    /**
     * Verifies whether a new portefeuille with the given name or code
     * can be safely created, ensuring no conflicts with active portefeuilles.
     *
     * @param parentName the target portefeuille name
     * @param mainName the desired portefeuille name
     * @param mainCode the desired portefeuille code
     * @return {@code true} if no active portefeuille with the same name or code exists; {@code false} otherwise
     */
    @Query("""
           select case when (count(f) = 1) then true else false end
           from Portefeuille f
           where f.status = ACTIVE
           and f.name <> :parentName
           and (f.name = :mainName or f.code = :mainCode)
           """)
    boolean validSplitMainPortefeuille(String parentName, String mainName, String mainCode);

    /**
     * Retrieves a list of portefeuilles considered relevant in the current legal context.
     * Typically includes active and evolving portefeuilles.
     *
     * @return a list of relevant portefeuilles
     */
    @Query("""
           select pf from Portefeuille as pf
           where pf.status in (ACTIVE, EVOLVING)
           """)
    List<Portefeuille> relevantPortefeuilles();

    /**
     * Finds a portefeuille by its exact name.
     *
     * @param name the portefeuille name
     * @return an {@link Optional} containing the portefeuille if found; otherwise empty
     */
    @Query("select pf from Portefeuille as pf where pf.name = :name")
    Optional<Portefeuille> portefeuilleByName(String name);

    /**
     * Finds a portefeuille by its name and fetches its originating portefeuilles.
     *
     * @param name the portefeuille name
     * @return an {@link Optional} containing the portefeuille with its origins; otherwise empty
     */
    @Query("""
           select pf from Portefeuille as pf join fetch pf.originatingPortefeuilles opf
           where pf.name = :name
           """)
    Optional<Portefeuille> portefeuilleByNameWithOrigins(String name);

    /**
     * Validates whether a portefeuille with the given old name exists
     * and is currently active. Used as a precondition for renaming.
     *
     * @param oldName the current name of the portefeuille to be renamed
     * @return {@code true} if an active portefeuille with the old name exists; {@code false} otherwise
     */
    @Query("""
           select case when (count(f) = 1) then true else false end
           from Portefeuille f
           where f.name = :oldName and f.status = ACTIVE
           """)
    boolean validRenameTarget(String oldName);

    /**
     * Checks if a proposed new name is not already used by another active portefeuille.
     *
     * @param newName the proposed new name
     * @return {@code true} if the name is available; {@code false} otherwise
     */
    @Query("""
           select case when (count(f) = 0) then true else false end
           from Portefeuille f
           where f.name = :newName and f.status = ACTIVE
           """)
    boolean validNewName(String newName);

}
