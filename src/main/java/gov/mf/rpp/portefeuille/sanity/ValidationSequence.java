package gov.mf.rpp.portefeuille.sanity;

import jakarta.validation.GroupSequence;

/**
 *
 * @author HP
 */
@GroupSequence({ValidationSequence.SanityCheck.class, ValidationSequence.BusinessCheck.class})
public interface ValidationSequence {

    interface BusinessCheck {

    }

    interface SanityCheck {

    }

}
