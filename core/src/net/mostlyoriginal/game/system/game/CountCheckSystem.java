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
        if (hand.getCount() > 21){
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.SelectNextPlayer);
        } else {
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.PlayerChoice);
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}
