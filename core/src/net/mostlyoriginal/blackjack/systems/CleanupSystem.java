
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.blackjack.BlackJackTags;
import net.mostlyoriginal.blackjack.components.GameCard;
import net.mostlyoriginal.blackjack.components.PlayerHand;
import net.mostlyoriginal.blackjack.components.PlayerPosition;
import net.mostlyoriginal.game.component.agent.PlayerControlled;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class CleanupSystem extends BaseBlackjackSystem {
    private TagManager tagManager;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public CleanupSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerHand.class, PlayerPosition.class), resolver);
    }

    @Override
    protected void begin() {
        super.begin();
        Entity deck = tagManager.getEntity("deck");
        deck.edit().add(new PlayerHand(GameCard.getShuffledDeck())).getEntity();
        tagManager.unregister(BlackJackTags.WINNER);
    }

    @Override
    protected void process(Entity e) {
        e.edit().remove(PlayerControlled.class).add(new PlayerHand()).getEntity();
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        BasePhaseSystem[] basePhaseSystems = new BasePhaseSystem[1];
        basePhaseSystems[0] = getPhaseFromName(BlackJackSystems.SelectNextPlayer);
        return basePhaseSystems;
    }
}
