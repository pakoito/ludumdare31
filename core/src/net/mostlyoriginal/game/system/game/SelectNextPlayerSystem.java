
package net.mostlyoriginal.game.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.BlackJackSystems;
import net.mostlyoriginal.game.IGetPhaseFromId;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.component.game.PlayerHand;
import net.mostlyoriginal.game.component.game.PlayerPosition;
import net.mostlyoriginal.game.events.EventCommander;
import net.mostlyoriginal.game.events.GameFinishedEvent;

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

    private int mWinner;

    private int mWinnerCount;

    private boolean isEnded;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public SelectNextPlayerSystem(IGetPhaseFromId resolver, int playerTotal,
            EventCommander eventCommander) {
        super(Aspect.getAspectForAll(PlayerHand.class, PlayerPosition.class), resolver);
        this.mPlayerTotal = playerTotal;
        this.eventCommander = eventCommander;
        isNext = false;
        isEnded = false;
    }

    @Override
    protected void process(Entity e) {
        int position = playerPositionComponentMapper.get(e).position;
        int count = playerHandComponentMapper.get(e).getCount();
        if (count < 22 && count > mWinnerCount) {
            mWinner = position;
            mWinnerCount = count;
        }
        if (isNext) {
            e.edit().add(new PlayerControlled());
            isNext = false;
        } else {
            if (playerControlledComponentMapper.getSafe(e) != null) {
                if (position + 1 >= mPlayerTotal) {
                    eventCommander.postAnyEvent(new GameFinishedEvent(mWinner, mWinnerCount));
                    isEnded = true;
                } else {
                    isNext = true;
                }
                e.edit().remove(PlayerControlled.class);
            }
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        BasePhaseSystem[] basePhaseSystems;
        if (isEnded) {
            basePhaseSystems = new BasePhaseSystem[0];
        } else {
            basePhaseSystems = new BasePhaseSystem[1];
            basePhaseSystems[0] = getPhaseFromName(BlackJackSystems.DealHidden);
        }
        return basePhaseSystems;
    }
}
