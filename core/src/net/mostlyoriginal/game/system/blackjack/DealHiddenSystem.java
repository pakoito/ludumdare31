
package net.mostlyoriginal.game.system.blackjack;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.BlackJackSystems;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.component.blackjack.GameCard;
import net.mostlyoriginal.game.component.blackjack.PlayerHand;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class DealHiddenSystem extends BaseBlackjackSystem {
    private BasePhaseSystem[] pushSystems = new BasePhaseSystem[0];

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    public DealHiddenSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        PlayerHand hand = playerHandComponentMapper.get(e);
        GameCard draw = new GameCard(GameCard.Suit.CLUBS, GameCard.Number.A);
        hand.hand.add(draw);
        log.trace(e + " secretly draw: " + draw);
        pushSystems = new BasePhaseSystem[1];
        pushSystems[0] = getPhaseFromName(BlackJackSystems.CountCheck);
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}
