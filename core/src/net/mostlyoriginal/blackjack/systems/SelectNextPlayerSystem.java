
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.blackjack.components.GameCard;
import net.mostlyoriginal.blackjack.components.PlayerHand;
import net.mostlyoriginal.blackjack.components.PlayerPosition;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.events.EventCommander;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class SelectNextPlayerSystem extends BaseBlackjackSystem {
    private final int mPlayerTotal;

    private final EventCommander eventCommander;

    private ComponentMapper<PlayerPosition> playerPositionComponentMapper;

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private ComponentMapper<PlayerControlled> playerControlledComponentMapper;

    private boolean isNext;

    private int mWinner = -1;

    private int mWinnerCount = -1;

    private boolean isEnded;

    private TagManager tagManager;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public SelectNextPlayerSystem(IGetPhaseFromId resolver, int playerTotal,
            EventCommander eventCommander) {
        super(Aspect.getAspectForAll(PlayerHand.class, PlayerPosition.class), resolver);
        this.mPlayerTotal = playerTotal;
        this.eventCommander = eventCommander;
        isNext = true;
        isEnded = false;
    }

    @Override
    protected void process(Entity e) {
        int position = playerPositionComponentMapper.get(e).position;
        int count = playerHandComponentMapper.get(e).getCount();
        if (count > 0 && count < 22 && count > mWinnerCount) {
            mWinner = position;
            mWinnerCount = count;
        }
        if (isNext) {
            e.edit().add(new PlayerControlled());
            isNext = false;
            log.trace("Turn for player " + (position + 1) + "");
        } else {
            if (playerControlledComponentMapper.getSafe(e) != null) {
                if (position + 1 >= mPlayerTotal) {
                    log.trace("Game ended, winner is player " + (mWinner + 1) + " with "
                            + mWinnerCount);
                    mWinner = -1;
                    mWinnerCount = -1;
                    isEnded = true;
                }
                isNext = true;
                e.edit().remove(PlayerControlled.class);
            }
        }
    }

    @Override
    protected void end() {
        super.end();
        if (isEnded){
            Entity deck = tagManager.getEntity("deck");
            deck.edit().add(new PlayerHand(GameCard.getShuffledDeck())).getEntity();
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        BasePhaseSystem[] basePhaseSystems;
        if (isEnded) {
            isEnded = false;
            basePhaseSystems = new BasePhaseSystem[1];
            basePhaseSystems[0] = getPhaseFromName(BlackJackSystems.CleanupSystem);
        } else {
            basePhaseSystems = new BasePhaseSystem[1];
            basePhaseSystems[0] = getPhaseFromName(BlackJackSystems.DealHidden);
        }
        return basePhaseSystems;
    }
}
