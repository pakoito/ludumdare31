package net.mostlyoriginal.game.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import net.mostlyoriginal.game.IGetPhaseFromId;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.component.game.GameCard;
import net.mostlyoriginal.game.component.game.PlayerHand;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
@Wire
public class DealHiddenSystem extends BaseBlackjackSystem {

    private BasePhaseSystem[] pushSystems;

    ComponentMapper<PlayerHand> playerHandComponentMapper;

    public DealHiddenSystem(IGetPhaseFromId resolver) {
        super(Aspect.getAspectForAll(PlayerControlled.class, PlayerHand.class), resolver);
    }

    @Override
    protected void process(Entity e) {
        if (Gdx.input.isButtonPressed(Input.Keys.D)){
            PlayerHand hand = playerHandComponentMapper.get(e);
            hand.hand.add(new GameCard(GameCard.Suit.CLUBS, GameCard.Number.A));
            pushSystems = new BasePhaseSystem[1];
            pushSystems[0] = new CountCheckSystem(null);
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
