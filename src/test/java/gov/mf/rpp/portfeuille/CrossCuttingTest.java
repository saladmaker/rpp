package gov.mf.rpp.portfeuille;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;


//TODO test validation, transactions, ...etc.
@QuarkusTest
public class CrossCuttingTest {

    @Inject
    PortfeuilleRepo portfeuilleRepo;

    @BeforeAll
    static void test_setup(){
        Persistence
    }

    @Test
    void test_not_null_behavior(){
        portfeuilleRepo.renamePortfeuille(null);
    }
}
