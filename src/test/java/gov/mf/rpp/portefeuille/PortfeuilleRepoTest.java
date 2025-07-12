package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfeuilleRepoTest {

    @Inject
    PortefeuilleMovement portefeuilleRules;

    @Inject
    SessionFactory session;

    @BeforeAll
    void setup() {
        session.getSchemaManager().truncateMappedObjects();
    }

    @Test
    @Order(1)
    void test_portfeuille_creation() {
        var mf = new CreateRequest("mf", "007", PortefeuilleStatus.ACTIVE);
        portefeuilleRules.createPortefeuille(mf);
        assertThat("portfeuille id must not be null",
                portefeuilleRules.portefeuilleByName("mf")
                        .map(Portefeuille::getId)
                        .orElseThrow(),
                notNullValue());

        var mjs = new CreateRequest("mjs", "021", PortefeuilleStatus.ACTIVE);
        var mfa = new CreateRequest("mfa", "022", PortefeuilleStatus.ACTIVE);
        portefeuilleRules.createPortefeuille(mjs);
        portefeuilleRules.createPortefeuille(mfa);

    }

    @Test
    @Order(2)
    void test_portefeuille_create_validation() {
        var nex = Assertions.assertThrowsExactly(ValidationException.class,
                () -> portefeuilleRules.createPortefeuille(null)
        );
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(new CreateRequest(null, "212", PortefeuilleStatus.ACTIVE))
        );
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(new CreateRequest("df", null, PortefeuilleStatus.ACTIVE))
        );

    }

    @Test
    @Order(4)
    void test_active_portfeuille() {
        var names = portefeuilleRules.relevantPortefeuilles()
                .stream()
                .map(Portefeuille::getName)
                .toList();
        assertThat("it must contains mf, mjs, mfa", names, containsInAnyOrder("mf", "mjs", "mfa"));

    }

    @Test
    @Order(5)
    void test_portefeuille_renaming() {
        var mfad = portefeuilleRules.renamePortefeuille(new RenameRequest("mfa", "mfad"));
        assertThat("mfad must be of renaming legal source type", mfad.getOriginatingEvent(),
                is(LegalSourceType.RENAMING));
        var names = portefeuilleRules.relevantPortefeuilles().stream()
                .map(Portefeuille::getName)
                .toList();
        assertThat("it must contains mf, mjs, mfad", names,
                containsInAnyOrder("mf", "mjs", "mfad"));
        assertThat("it must not contain mfad", names, not(contains("mfa")));
        assertThat("mfa must be inactive",
                portefeuilleRules.portefeuilleByName("mfa").map(Portefeuille::getStatus).orElseThrow(),
                is(PortefeuilleStatus.INACTIVE));

        var originatingsNames = portefeuilleRules.portefeuilleByNameWithOrigins("mfad")
                .orElseThrow()
                .getOriginatingPortefeuilles()
                .stream()
                .map(Portefeuille::getName)
                .toList();
        assertThat("it must contain mfa", originatingsNames, contains("mfa"));
    }

    @Test
    @Order(6)
    void test_portefeuille_renaming_validation() {
        //null
        var nex = Assertions.assertThrowsExactly(ValidationException.class,
                () -> portefeuilleRules.renamePortefeuille(null)
        );
        //not existing
        var noName = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfr", "fds"))
        );
        noName.getConstraintViolations().stream()
                .forEach(System.out::println);
        //inactive name
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfa", "md"))
        );
        //already taken new name
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfad", "mf"))
        );

    }

}
