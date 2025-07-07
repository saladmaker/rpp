package gov.mf.rpp.portfeuille;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import gov.mf.rpp.portfeuille.rename.RenameRequest;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PortfeuilleRepoTest {

    @Inject
    PortfeuilleRepo portfeuilleQuery;

    @Test
    @Order(1)
    void test_portfeuille_creation() {
        Portfeuille mf = Portfeuille.of("mf", "007");
        portfeuilleQuery.createPortfeuille(mf);
        assertThat("portfeuille id must not be null", mf.getId(), notNullValue());

        Portfeuille mjs = Portfeuille.of("mjs", "021");
        Portfeuille mfa = Portfeuille.of("mfa", "022");
        portfeuilleQuery.createPortfeuille(mjs);
        portfeuilleQuery.createPortfeuille(mfa);

    }

    @Test
    @Order(2)
    void test_active_portfeuille() {
        var names = portfeuilleQuery.activePortfeuille()
                .stream()
                .map(Portfeuille::getName)
                .toList();
        assertThat("it must contains mf, mjs, mfa", names, containsInAnyOrder("mf", "mjs", "mfa"));

    }

    @Test
    @Order(3)
    void test_renaming_case() {
        portfeuilleQuery.renamePortfeuille(new RenameRequest("mfa", "mfad"));
        var names = portfeuilleQuery.activePortfeuille().stream()
                .map(Portfeuille::getName)
                .toList();
        assertThat("it must contains mf, mjs, mfad", names, containsInAnyOrder("mf", "mjs", "mfad"));
        assertThat("it must not contain mfad", names, not(contains("mfa")));
    }
    
}
