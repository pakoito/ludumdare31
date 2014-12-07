package net.mostlyoriginal.game.system.game;

import com.artemis.Aspect;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.BlackJackSystems;
import net.mostlyoriginal.game.IGetPhaseFromId;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
public abstract class BaseBlackjackSystem extends BasePhaseSystem {

    private final IGetPhaseFromId mPhaseResolver;

    public BaseBlackjackSystem(Aspect aspect, IGetPhaseFromId phaseResolver) {
        super(aspect);
        this.mPhaseResolver = phaseResolver;
    }

    protected BaseBlackjackSystem getPhaseFromName(BlackJackSystems name) {
        return mPhaseResolver.system(name);
    }
}
