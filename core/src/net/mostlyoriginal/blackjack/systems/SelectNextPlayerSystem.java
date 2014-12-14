
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.blackjack.BlackJackTags;
import net.mostlyoriginal.blackjack.components.PlayerHand;
import net.mostlyoriginal.blackjack.components.PlayerPosition;
import net.mostlyoriginal.game.component.agent.PlayerControlled;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class SelectNextPlayerSystem extends BaseBlackjackSystem {
    private final int mPlayerTotal;

    private ComponentMapper<PlayerPosition> playerPositionComponentMapper;

    private ComponentMapper<PlayerControlled> playerControlledComponentMapper;

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private TagManager tagManager;

    private boolean isNext;

    private boolean isEnded;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public SelectNextPlayerSystem(IGetPhaseFromId resolver, int playerTotal) {
        super(Aspect.getAspectForAll(PlayerPosition.class), resolver);
        this.mPlayerTotal = playerTotal;
        isNext = true;
        isEnded = false;
    }

    @Override
    protected void process(Entity e) {
        int position = playerPositionComponentMapper.get(e).position;
        if (isNext) {
            e.edit().add(new PlayerControlled());
            isNext = false;
            log.trace("Turn for player " + (position + 1) + "");
        } else {
            if (playerControlledComponentMapper.getSafe(e) != null) {
                if (position + 1 >= mPlayerTotal) {
                    Entity mWinner = tagManager.getEntity(BlackJackTags.WINNER);
                    log.trace("===========================================");
                    if (mWinner != null) {
                        String msg = "== Game end, winner is player: "
                                + (playerPositionComponentMapper.get(mWinner).position + 1)
                                + " with " + playerHandComponentMapper.get(mWinner).getCount();
                        log.trace(msg.toUpperCase());
                    } else {
                        log.trace("== Game ended with no winner".toUpperCase());
                    }
                    log.trace("===========================================\n\n");
                    isEnded = true;
                }
                isNext = true;
                e.edit().remove(PlayerControlled.class);
            }
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
