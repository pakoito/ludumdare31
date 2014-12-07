package net.mostlyoriginal.game.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.IGetPhaseFromId;
import net.mostlyoriginal.game.component.game.PlayerPosition;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
@Wire
public class SelectNextPlayerSystem extends BaseBlackjackSystem {

    private final int mPlayerTotal;

    ComponentMapper<PlayerPosition> playerHandComponentMapper;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     *
     */
    public SelectNextPlayerSystem(IGetPhaseFromId resolver, int playerTotal) {
        super(Aspect.getAspectForAll(PlayerPosition.class), resolver);
        this.mPlayerTotal = playerTotal;
    }

    @Override
    protected void process(Entity e) {
        if ()

    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return new BasePhaseSystem[0];
    }
}
