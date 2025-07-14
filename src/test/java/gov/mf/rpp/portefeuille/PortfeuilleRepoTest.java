package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

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
        var nex = Assertions.assertThrowsExactly(ConstraintViolationException.class,
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
    @Order(3)
    void test_active_portfeuille() {
        var names = portefeuilleRules.relevantPortefeuilles()
                .stream()
                .map(Portefeuille::getName)
                .toList();
        assertThat("it must contains mf, mjs, mfa", names, containsInAnyOrder("mf", "mjs", "mfa"));

    }

    @Test
    @Order(4)
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
        //inactive name
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfa", "md"))
        );
        //already taken new name
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfad", "mf"))
        );

    }

    @Test
    @Order(7)
    void test_portefeuille_split() {
        var splitRequest = new SplitRequest("mjs", "mj", "021", List.of(new CreateRequest("ms", "024", PortefeuilleStatus.ACTIVE)));
        portefeuilleRules.split(splitRequest);

        assertThat("mjs must be evolving",
                portefeuilleRules.portefeuilleByName("mjs").map(Portefeuille::getStatus).orElseThrow(),
                is(PortefeuilleStatus.EVOLVING));
        var mj = portefeuilleRules.portefeuilleByNameWithOrigins("mj").orElseThrow();
        assertThat("mj must be incubating",
                mj.getStatus(),
                is(PortefeuilleStatus.INCUBATING));

        assertThat("mjs must be parent of mj",
                mj.getOriginatingPortefeuilles().stream()
                        .map(Portefeuille::getName)
                        .toList(),
                containsInAnyOrder("mjs")
        );
        var ms = portefeuilleRules.portefeuilleByNameWithOrigins("ms").orElseThrow();
        assertThat("ms must be incubating",
                ms.getStatus(),
                is(PortefeuilleStatus.INCUBATING));

        assertThat("mjs must be parent of ms",
                ms.getOriginatingPortefeuilles().stream()
                        .map(Portefeuille::getName)
                        .toList(),
                containsInAnyOrder("mjs")
        );

    }

    @Test
    @Order(8)
    void test_portefeuille_split_validation() {
        //null
        var nullCase = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(null)
        );
        var nullCaseMessage = nullCase.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        
        assertThat(
                "message must say split request ...",
                nullCaseMessage,
                containsString("split request must not be null!")
        );
        //null parent name
        var nullParentName = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(null, "md", "021", List.of(new CreateRequest("mff", "22", PortefeuilleStatus.ACTIVE))))
        );
        var nullParentMessage = nullParentName.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
        assertThat(
                "it must contain parent's name must not be blank",
                nullParentMessage,
                containsString("parent's name must not be blank")
        );
         
        //null main name
        var nullMainName = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", null, "021", List.of(new CreateRequest("mff", "22", PortefeuilleStatus.ACTIVE))))
        );
        var nullMainMessage = nullMainName.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
        assertThat(
                "it must contain main part's name must not be blank",
                nullMainMessage,
                containsString("main part's name must not be blank")
        );
        
        //null main code
        var nullMainCode = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mff", null, List.of(new CreateRequest("mff", "22", PortefeuilleStatus.ACTIVE))))
        );
        var nullMainCodeMessage = nullMainCode.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
        assertThat(
                "it must contain main part's code must not be blank",
                nullMainCodeMessage,
                containsString("main part's code must not be blank")
        );
        //null parts
        var nullParts = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mff", "22", null))
        );
        var nullPartsMessage = nullParts.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
        assertThat(
                "it must contain split parts must not be nul",
                nullPartsMessage,
                containsString("split parts must not be nul")
        );
        //inactive name


    }

}
