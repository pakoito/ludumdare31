
package net.mostlyoriginal.game.system.game;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.BlackJackSystems;
import net.mostlyoriginal.game.IGetPhaseFromId;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.component.game.PlayerHand;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
public class PlayerChoiceSystem extends BaseBlackjackSystem {
    private BasePhaseSystem[] pushSystems;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public PlayerChoiceSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        if (Gdx.input.isButtonPressed(Input.Keys.S)) {
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.DealHidden);
        } else if (Gdx.input.isButtonPressed(Input.Keys.D)) {
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.DealShown);
        } else if (Gdx.input.isButtonPressed(Input.Keys.R)) {
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.SelectNextPlayer);
        } else {
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = this;
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}
