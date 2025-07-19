package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;
import gov.mf.rpp.portefeuille.movement.split.Part;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.EnumSet;
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

    private static final String MF_NAME = "mf",
            MF_CODE = "007",
            MJS_NAME = "mjs",
            MJS_CODE = "021",
            MFA_NAME = "mfa",
            MFA_CODE = "022",
            MFAD_NAME = "mfad",
            MJ_NAME = "mj",
            MJ_CODE = "021",
            MS_NAME = "ms",
            MS_CODE = "024";

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
        var mf = new CreateRequest(MF_NAME, MF_CODE, PortefeuilleStatus.ACTIVE);
        portefeuilleRules.createPortefeuille(mf);
        assertThat("portfeuille id must not be null",
                portefeuilleRules.portefeuilleByName("mf")
                        .map(Portefeuille::getId)
                        .orElseThrow(),
                notNullValue());

        var mjs = new CreateRequest(MJS_NAME, MJS_CODE, PortefeuilleStatus.ACTIVE);
        var mfa = new CreateRequest(MFA_NAME, MFA_CODE, PortefeuilleStatus.ACTIVE);
        portefeuilleRules.createPortefeuille(mjs);
        portefeuilleRules.createPortefeuille(mfa);

    }

    @Test
    @Order(2)
    void test_portefeuille_create_validation() {
        //null param
        var nullParamEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(null)
        );
        var nullParamMessage = nullParamEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                nullParamMessage,
                containsString("create request must not be null")
        );

        //null name
        var nullNameEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(
                        new CreateRequest(null, "212", PortefeuilleStatus.ACTIVE)
                )
        );
        var nullNameMessage = nullNameEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                nullNameMessage,
                containsString("create request's old name must not be null or blank")
        );

        //null code
        var nullCodeEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(
                        new CreateRequest("df", null, PortefeuilleStatus.ACTIVE)
                )
        );
        var nullCodeMessage = nullCodeEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                nullCodeMessage,
                containsString("create request's new name must not be null or blank")
        );

        //existing name
        var exsitingNameEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.createPortefeuille(
                        new CreateRequest(MFA_NAME, "323", PortefeuilleStatus.ACTIVE)
                )
        );
        var existingNameMessage = exsitingNameEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                existingNameMessage,
                containsString("create request's name and code must not be already taken")
        );

    }

    @Test
    @Order(3)
    void test_active_portfeuille() {
        var names = portefeuilleRules.relevantPortefeuilleNames(EnumSet.of(PortefeuilleStatus.ACTIVE));

        assertThat(
                "it must contains mf, mjs, mfa",
                names,
                containsInAnyOrder(MF_NAME, MJS_NAME, MFA_NAME)
        );

    }

    @Test
    @Order(4)
    void test_portefeuille_renaming() {
        var mfad = portefeuilleRules.renamePortefeuille(new RenameRequest(MFA_NAME, MFAD_NAME));
        assertThat("mfad must be of renaming legal source type", mfad.getOriginatingEvent(),
                is(LegalSourceType.RENAMING));
        var names = portefeuilleRules.relevantPortefeuilleNames(EnumSet.of(PortefeuilleStatus.ACTIVE));

        assertThat(
                "it must contains mf, mjs, mfad",
                names,
                containsInAnyOrder(MF_NAME, MJS_NAME, MFAD_NAME)
        );

        assertThat("it must not contain mfad", names, not(contains(MFA_NAME)));

        assertThat(
                "mfa must be inactive",
                portefeuilleRules.portefeuilleByName(MFA_NAME)
                        .map(Portefeuille::getStatus).orElseThrow(),
                is(PortefeuilleStatus.INACTIVE)
        );

        var originatingsNames = portefeuilleRules.portefeuilleByNameWithOrigins(MFAD_NAME)
                .orElseThrow()
                .getOriginatingPortefeuilles()
                .stream()
                .map(Portefeuille::getName)
                .toList();
        assertThat("it must contain mfa", originatingsNames, contains(MFA_NAME));
    }

    @Test
    @Order(6)
    void test_portefeuille_renaming_validation() {
        //null
        var nullEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(null)
        );
        var nullMessage = nullEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                nullMessage,
                containsString("rename request must not be null")
        );

        //null record components
        var nullNameEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest(null, "fds"))
        );
        var nullNameMessage = nullNameEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                nullNameMessage,
                containsString("rename request's old name must not be null or blank")
        );

        //no existing old name
        var notValidTargetEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest("mfr", "fds"))
        );
        var notValidTargetMessage = notValidTargetEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                notValidTargetMessage,
                containsString("old name must be valid target and new name must not be already taken")
        );

        //taken name
        var takenNameEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest(MJS_NAME, MF_NAME))
        );
        var takenNameMessage = takenNameEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must message exact message...",
                takenNameMessage,
                containsString("old name must be valid target and new name must not be already taken")
        );

        //not active name
        var inactiveNameEx = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.renamePortefeuille(new RenameRequest(MJS_NAME, MF_NAME))
        );
        var inactiveNameMessage = inactiveNameEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        assertThat(
                "it must contain message",
                inactiveNameMessage,
                containsString("old name must be valid target and new name must not be already taken")
        );

    }

    @Test
    @Order(7)
    void test_portefeuille_split() {
        var splitRequest = new SplitRequest(
                MJS_NAME, MJ_NAME, MJ_CODE,
                List.of(new Part(MS_NAME, MS_CODE))
        );
        portefeuilleRules.split(splitRequest);

        assertThat(
                "mjs must be evolving",
                portefeuilleRules.portefeuilleByName("mjs")
                        .map(Portefeuille::getStatus)
                        .orElseThrow(),
                is(PortefeuilleStatus.EVOLVING)
        );

        var mj = portefeuilleRules.portefeuilleByNameWithOrigins(MJ_NAME)
                .orElseThrow();

        assertThat("mj must be incubating",
                mj.getStatus(),
                is(PortefeuilleStatus.INCUBATING));

        assertThat("mjs must be parent of mj",
                mj.getOriginatingPortefeuilles()
                        .stream()
                        .map(Portefeuille::getName)
                        .toList(),
                containsInAnyOrder(MJS_NAME)
        );
        var ms = portefeuilleRules.portefeuilleByNameWithOrigins(MS_NAME)
                .orElseThrow();
        assertThat("ms must be incubating", ms.getStatus(), is(PortefeuilleStatus.INCUBATING));

        assertThat("mjs must be parent of ms",
                ms.getOriginatingPortefeuilles()
                        .stream()
                        .map(Portefeuille::getName)
                        .toList(),
                containsInAnyOrder(MJS_NAME)
        );
        
        var evolvings = portefeuilleRules.relevantPortefeuilleNames(EnumSet.of(PortefeuilleStatus.EVOLVING));

        assertThat("it must contain MJS", evolvings, containsInAnyOrder(MJS_NAME));
        
        var incubatings = portefeuilleRules.relevantPortefeuilleNames(EnumSet.of(PortefeuilleStatus.INCUBATING));
        assertThat("it must contain MJS", incubatings, containsInAnyOrder(MJ_NAME, MS_NAME));

    }

    @Test
    @Order(8)
    void test_split_sanity_validation() {
        //null param
        var nullCase = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(null)
        );
        var nullCaseMessage = nullCase.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullCaseMessage,
                containsString("split request must not be null")
        );

        //null parent name
        var nullParentName = Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> portefeuilleRules.split(
                        new SplitRequest(null, "md", "021",
                                List.of(new Part("mff", "22")))
                )
        );
        var nullParentMessage = nullParentName.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullParentMessage,
                containsString("parent's name must not be blank")
        );

        //null main name
        var nullMainName = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", null, "021", List.of(new Part("mff", "22"))))
        );
        var nullMainMessage = nullMainName.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullMainMessage,
                containsString("main part's name must not be blank")
        );

        //null main code
        var nullMainCode = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mff", null, List.of(new Part("mff", "22"))))
        );
        var nullMainCodeMessage = nullMainCode.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullMainCodeMessage,
                containsString("main part's code must not be blank")
        );
        //null parts
        var nullParts = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mff", "22", null))
        );
        var nullPartsMessage = nullParts.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullPartsMessage,
                containsString("split parts must not be nul")
        );

        //empty parts
        var emptyParts = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, "mff", "22", List.of()))
        );
        var emptyPartsMessage = emptyParts.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                emptyPartsMessage,
                containsString("there must be at least one part")
        );

        //null part
        var nullPart = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, "mff", "22", Arrays.asList(new Part[]{null})))
        );
        var nullPartMessage = nullPart.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullPartMessage,
                containsString("part must not be null")
        );

        //null part components
        //null main code
        var nullPartCode = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mff", "323", List.of(new Part("mff", null))))
        );
        var nullpartCodeMessage = nullPartCode.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                nullpartCodeMessage,
                containsString("part's code must not be null or blank")
        );

    }

    @Test
    @Order(9)
    void test_portefeuille_split_validation() {

        //inactive portfeuille
        var inactivePortfeuilleName = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("fd", "mff", "22", List.of(new Part("ff", "3"))))
        );
        var inactivePortfeuilleNameMessage = inactivePortfeuilleName.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                inactivePortfeuilleNameMessage,
                containsString("split request must be valid")
        );
        //main name existes
        var mainNamesExistes = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", MFAD_NAME, "22", List.of(new Part("ff", "3"))))
        );
        var mainNamesExistesMessage = mainNamesExistes.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                mainNamesExistesMessage,
                containsString("split request must be valid")
        );

        //main code existes
        var mainCodeExistes = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest("mf", "mfs", MJ_CODE, List.of(new Part("ff", "3"))))
        );
        var mainCodeExistesMessage = mainCodeExistes.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                mainCodeExistesMessage,
                containsString("split request must be valid")
        );

        //    parts name duplicates
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, MF_NAME, MF_CODE, List.of(new Part("ff", "3"), new Part("ff", "4"))))
        );
        var partCodeDuplicates = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, "mfs", "d", List.of(new Part("ff", "3"), new Part("fsds", "3"))))
        );
        var partCodeDuplicatesMessage = partCodeDuplicates.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                partCodeDuplicatesMessage,
                containsString("split request must be valid")
        );

        //    parts code/name existes
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, "mfs", "d", List.of(new Part("fsdf", MJ_CODE))))
        );
        var partexist = Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> portefeuilleRules.split(new SplitRequest(MF_NAME, "mfs", "d", List.of(new Part(MFAD_NAME, "3"))))
        );
        var partexistMessage = partexist.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        assertThat(
                "it must message exact message...",
                partexistMessage,
                containsString("split request must be valid")
        );
    }
}
