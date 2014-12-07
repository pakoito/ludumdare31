package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
public abstract class BaseBlackjackSystem extends BasePhaseSystem {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final IGetPhaseFromId mPhaseResolver;

    public BaseBlackjackSystem(Aspect aspect, IGetPhaseFromId phaseResolver) {
        super(aspect);
        this.mPhaseResolver = phaseResolver;
    }

    protected BaseBlackjackSystem getPhaseFromName(BlackJackSystems name) {
        return mPhaseResolver.system(name);
    }
}
