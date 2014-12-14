
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
public class UpdateWinnerSystem extends BaseBlackjackSystem {
    private ComponentMapper<PlayerPosition> playerPositionComponentMapper;

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private TagManager tagManager;

    private Entity mWinner;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public UpdateWinnerSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerHand.class, PlayerControlled.class), resolver);
    }

    @Override
    protected void begin() {
        super.begin();
        mWinner = tagManager.getEntity(BlackJackTags.WINNER);
    }

    @Override
    protected void process(Entity e) {
        int count = playerHandComponentMapper.get(e).getCount();
        int winnerCount = (mWinner == null) ? -1 : playerHandComponentMapper.get(mWinner)
                .getCount();
        if (count < 22 && count > winnerCount && count > 0) {
            tagManager.register(BlackJackTags.WINNER, e);
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        BasePhaseSystem[] basePhaseSystemss = new BasePhaseSystem[1];
        basePhaseSystemss[0] = getPhaseFromName(BlackJackSystems.SelectNextPlayer);
        return basePhaseSystemss;
    }
}
