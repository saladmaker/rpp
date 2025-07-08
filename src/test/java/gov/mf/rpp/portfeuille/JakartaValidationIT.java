package gov.mf.rpp.portfeuille;


import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

//TODO ideal setup and clean for IT for hibernate
//TODO test sechema generation, validation, ...etc.
@QuarkusTest
public class JakartaValidationIT {

    @Inject
    PortfeuilleRepo portfeuilleRepo;

    @Inject 
    SessionFactory sessionFactory ;


    @BeforeAll
    static void setup(){

    }

    @BeforeEach
    void setup_data(){
    }
    @Test
    void test_not_null_behavior(){
        portfeuilleRepo.renamePortfeuille(null);
    }
}
