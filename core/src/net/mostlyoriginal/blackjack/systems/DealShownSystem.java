
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.blackjack.components.GameCard;
import net.mostlyoriginal.blackjack.components.PlayerHand;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class DealShownSystem extends BaseBlackjackSystem {
    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private BasePhaseSystem[] pushSystems = new BasePhaseSystem[0];

    public DealShownSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        PlayerHand hand = playerHandComponentMapper.get(e);
        GameCard draw = GameCard.getRandomCard();
        hand.hand.add(draw);
        log.trace(e + " publicly draw: " + draw);
        pushSystems = new BasePhaseSystem[1];
        pushSystems[0] = getPhaseFromName(BlackJackSystems.CountCheck);
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}