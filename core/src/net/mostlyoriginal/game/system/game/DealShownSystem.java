package net.mostlyoriginal.game.system.game;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.game.BlackJackSystems;
import net.mostlyoriginal.game.IGetPhaseFromId;
import net.mostlyoriginal.game.component.agent.PlayerControlled;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.component.game.GameCard;
import net.mostlyoriginal.game.component.game.PlayerHand;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
@Wire
public class DealShownSystem extends BaseBlackjackSystem {

    private ComponentMapper<PlayerHand> playerHandComponentMapper;

    private BasePhaseSystem[] pushSystems;

    public DealShownSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        if (Gdx.input.isButtonPressed(Input.Keys.D)){
            PlayerHand hand = playerHandComponentMapper.get(e);
            hand.hand.add(new GameCard(GameCard.Suit.HEARTS, GameCard.Number.Q));
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = getPhaseFromName(BlackJackSystems.PlayerChoice);
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
