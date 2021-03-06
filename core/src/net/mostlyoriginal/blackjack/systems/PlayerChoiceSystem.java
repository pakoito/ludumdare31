
package net.mostlyoriginal.blackjack.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.blackjack.BlackJackSystems;
import net.mostlyoriginal.blackjack.components.PlayerHand;
import net.mostlyoriginal.game.component.agent.PlayerControlled;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
@Wire
public class PlayerChoiceSystem extends BaseBlackjackSystem {
    private BasePhaseSystem[] pushSystems = new BasePhaseSystem[0];

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private boolean firstEnter = true;

    /**
     * Creates an entity system that uses the specified aspect as a matcher against entities.
     */
    public PlayerChoiceSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        PlayerHand hand = playerHandComponentMapper.get(e);
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            log.trace("Showing card, drawing secretly, with: " + hand.hand);
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.DealHidden);
            firstEnter = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            log.trace("Hiding card, drawing openly, with: " + hand.hand);
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.DealShown);
            firstEnter = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            log.trace("Player checks with: " + hand.hand);
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.UpdateWinnerPlayerSystem);
            firstEnter = true;
        } else {
            if(firstEnter) {
                log.trace("REVEAL CARD DRAW HIDDEN: S\tHIDE CARD DRAW OPENLY: D\tCHECK: R");
                firstEnter = false;
            }
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = this;
        }
    }

    @Override
    public BasePhaseSystem[] pushSystems() {
        return pushSystems;
    }
}
