package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.blackjack.components.PlayerHand;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
@Wire
public class CountCheckSystem extends BaseBlackjackSystem {

    private BasePhaseSystem[] pushSystems = new BasePhaseSystem[0];

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     *
     * @param resolver
     */
    public CountCheckSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        PlayerHand hand = playerHandComponentMapper.get(e);
        log.trace("Checking " + e + " card count");
        int count = hand.getCount();
        if (count > 21){
            log.trace(e + " cards are over 21: " + count);
            log.trace("Moving to next player");
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.SelectNextPlayer);
        } else {
            log.trace("Count correct: " + count);
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.PlayerChoice);
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}
