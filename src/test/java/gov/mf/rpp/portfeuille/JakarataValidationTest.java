/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.mf.rpp.portfeuille;

import gov.mf.rpp.portfeuille.rename.RenameRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 *
 * @author khaled
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JakarataValidationTest {

    @Inject
    PortfeuilleRepo portfeuilleRepo;
    
    @Inject
    PortfeuilleQueries portfeuilleQueries;
    
    @Inject
    SessionFactory session;

    @BeforeAll
    void truncate(){
        session.getSchemaManager().truncateMappedObjects();
        portfeuilleQueries.createPortfeuille(Portfeuille.of("a", "2", PortfeuilleStatus.ACTIVE));
        
    } 
    
    

    @Test
    void test_non_valid_renaming() {
//        Assertions.assertThrows(ConstraintViolationException.class, ()-> portfeuilleRepo.renamePortfeuille(null));
        portfeuilleQueries.renamePortfeuille(null);
    }
}
