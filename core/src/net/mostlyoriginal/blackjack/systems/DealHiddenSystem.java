
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.blackjack.components.GameCard;
import net.mostlyoriginal.blackjack.components.PlayerHand;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class DealHiddenSystem extends BaseBlackjackSystem {
    private BasePhaseSystem[] pushSystems = new BasePhaseSystem[0];

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private TagManager tagManager;

    private PlayerHand deck;

    public DealHiddenSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void begin() {
        super.begin();
        deck = playerHandComponentMapper.get(tagManager.getEntity("deck"));
    }

    @Override
    protected void process(Entity e) {
        PlayerHand hand = playerHandComponentMapper.get(e);
        GameCard draw = deck.hand.remove(0);
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
