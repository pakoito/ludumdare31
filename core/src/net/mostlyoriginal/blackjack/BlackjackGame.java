package net.mostlyoriginal.blackjack;

import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.pacoworks.cardframework.framework.CardgameFramework;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import com.pacoworks.cardframework.systems.IVictoryDecider;
import com.squareup.otto.Subscribe;
import net.mostlyoriginal.blackjack.components.GameCard;
import net.mostlyoriginal.blackjack.components.PlayerHand;
import net.mostlyoriginal.blackjack.components.PlayerPosition;
import net.mostlyoriginal.blackjack.events.GameFinishedEvent;
import net.mostlyoriginal.blackjack.systems.*;
import net.mostlyoriginal.game.MainScreen;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.events.EventCommander;
import net.mostlyoriginal.game.events.KeycodeEvent;
import net.mostlyoriginal.paco.IKnownMove;
import net.mostlyoriginal.paco.ReactiveInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
public class BlackjackGame {

    public static final Logger log = LoggerFactory.getLogger(MainScreen.class);

    private final CardgameFramework cardgameFramework;

    private final ReactiveInputs reactiveInputs;

    private final HashMap<BlackJackSystems, BaseBlackjackSystem> phaseSystemsMap;

    private final EventCommander commander;

    private AtomicBoolean mGameEnd = new AtomicBoolean(false);

    private Object subscriptor;

    public BlackjackGame(){
        commander = new EventCommander();
        phaseSystemsMap = new HashMap<BlackJackSystems, BaseBlackjackSystem>();
        IGetPhaseFromId resolver = new IGetPhaseFromId() {
            @Override
            public BaseBlackjackSystem system(BlackJackSystems name) {
                return phaseSystemsMap.get(name);
            }
        };
        phaseSystemsMap.put(BlackJackSystems.CountCheck, new CountCheckSystem(resolver));
        phaseSystemsMap.put(BlackJackSystems.DealHidden, new DealHiddenSystem(resolver));
        phaseSystemsMap.put(BlackJackSystems.DealShown, new DealShownSystem(resolver));
        phaseSystemsMap.put(BlackJackSystems.PlayerChoice, new PlayerChoiceSystem(resolver));
        phaseSystemsMap.put(BlackJackSystems.SelectNextPlayer, new SelectNextPlayerSystem(resolver, 2, commander));
        cardgameFramework = CardgameFramework.builder().victoryChecker(new IVictoryDecider() {
            @Override
            public boolean isVictoryCondition() {
                return mGameEnd.get();
            }
        }).phaseSystems(new ArrayList<BasePhaseSystem>(phaseSystemsMap.values()))
                .startingSystem(phaseSystemsMap.get(BlackJackSystems.DealHidden)).eventCommander(commander).build();
        reactiveInputs = new ReactiveInputs();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                reactiveInputs.sendInputKeycode(keycode);
                commander.postAnyEvent(new KeycodeEvent(keycode));
                return super.keyDown(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        initBlackJack();
    }

    private void initBlackJack() {
        reactiveInputs.observeMove(new IKnownMove() {
            @Override
            public List<Integer> getInputSequence() {
                return Arrays.asList(Input.Keys.L, Input.Keys.O, Input.Keys.L);
            }

            @Override
            public String getName() {
                return "attack";
            }

            @Override
            public int getLeniencyFrames() {
                return 60;
            }

            @Override
            public int getMaxInputErrors() {
                return 0;
            }

            @Override
            public int getFramesInSecond() {
                return 60;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                System.out.println(integers);
            }
        });

        subscriptor = new Object() {
            @Subscribe
            public void gameEnd(GameFinishedEvent event) {
                log.trace("Game ended, winner is player " + event.getWinnerPosition() + " with " + event.getWinnerCount());
                mGameEnd.set(true);
            }
        };
        commander.subscribe(subscriptor);
        new EntityBuilder(cardgameFramework.getWorld()).tag("player1").group("1").with(new PlayerControlled(), new PlayerPosition(0), new PlayerHand()).build();
        new EntityBuilder(cardgameFramework.getWorld()).tag("player2").group("2").with(new PlayerPosition(1), new PlayerHand()).build();
        ArrayList<GameCard> shuffledDeck = new ArrayList<GameCard>(Arrays.asList(GameCard.getDeck()));
        Collections.shuffle(shuffledDeck);
        new EntityBuilder(cardgameFramework.getWorld()).tag("deck").with(new PlayerHand(shuffledDeck)).build();
    }

    public void step() {
        if (!mGameEnd.get()) {
            cardgameFramework.getWorld().process();
        } else {
            Gdx.app.exit();
        }
    }

    public void dispose() {
        commander.unsubscribe(subscriptor);
        cardgameFramework.end();
    }
}
