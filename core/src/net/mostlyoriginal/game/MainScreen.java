
package net.mostlyoriginal.game;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.pacoworks.cardframework.framework.CardgameFramework;
import com.pacoworks.cardframework.systems.BasePhaseSystem;
import com.pacoworks.cardframework.systems.IVictoryDecider;
import com.squareup.otto.Subscribe;
import net.mostlyoriginal.api.system.anim.ColorAnimationSystem;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.camera.EntityCameraSystem;
import net.mostlyoriginal.api.system.interact.AimSystem;
import net.mostlyoriginal.api.system.map.MapCollisionSystem;
import net.mostlyoriginal.api.system.map.MapWallSensorSystem;
import net.mostlyoriginal.api.system.map.TiledMapSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.api.system.physics.*;
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.render.MapRenderSystem;
import net.mostlyoriginal.api.system.script.EntitySpawnerSystem;
import net.mostlyoriginal.api.system.script.SchedulerSystem;
import net.mostlyoriginal.game.component.agent.PlayerControlled;
import net.mostlyoriginal.game.component.game.PlayerHand;
import net.mostlyoriginal.game.component.game.PlayerPosition;
import net.mostlyoriginal.game.events.EventCommander;
import net.mostlyoriginal.game.events.GameFinishedEvent;
import net.mostlyoriginal.game.events.KeycodeEvent;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.system.agent.PlayerControlSystem;
import net.mostlyoriginal.game.system.agent.SlumbererSystem;
import net.mostlyoriginal.game.system.game.*;
import net.mostlyoriginal.game.system.interact.PluckableSystem;
import net.mostlyoriginal.paco.IKnownMove;
import net.mostlyoriginal.paco.ReactiveInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {
    public static final int CAMERA_ZOOM_FACTOR = 3;

    public static final Logger log = LoggerFactory.getLogger(MainScreen.class);

    private final World world;

    private final CardgameFramework cardgameFramework;

    private final InputMultiplexer inputMultiplexer;

    private final ReactiveInputs reactiveInputs;

    private final HashMap<BlackJackSystems, BaseBlackjackSystem> phaseSystemsMap;

    private final EventCommander commander;

    private AtomicBoolean mGameEnd = new AtomicBoolean(false);

    private Object subscriptor;

    public MainScreen() {
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
        inputMultiplexer = new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                reactiveInputs.sendInputKeycode(keycode);
                commander.postAnyEvent(new KeycodeEvent(keycode));
                return super.keyDown(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
        world = new World();
        // @todo comment out systems you do not need for your game.
        // NS2D:
        // @todo port: buildable
        // @todo port: critical (game over when destroyed?)
        // @todo port: Health (Is this sufficiently generic? Probably not.)
        // @todo port: HealthIndicator (Is this sufficiently generic? Probably not.)
        // @todo port: Harvester (Is this sufficiently generic? Probably not.)
        // @todo port: Inventory
        // @todo port: Wallet
        // @todo port: Payload (generalize)
        // @todo port: Weapon (generalize)
        // @todo port: PlayerControlled (Is this sufficiently generic? Probably not.)
        // @todo port: RespawnOnDeath (Is this sufficiently generic? Probably not.)
        // @todo port: SkulkControlled (generalize?)
        // @todo port: Terminal (do we need this? Script suffices).
        // @todo port: example UI system
        // Tox:
        // @todo port: dissolvesontouch.
        // @todo port: EquipBonus
        // @todo port: ExitSystem
        // @todo port: HighscoreSystem
        // Other
        // @todo steve logo!
        /*
         * @todo port: // Active - Cleanup world.setSystem(new TerminalSystem());
         * world.setSystem(new CollisionSystem()); // Active - Input/Logic world.setSystem(new
         * PlayerControlSystem()); world.setSystem(new SkulkControlSystem()); world.setSystem(new
         * WeaponSystem()); // Active - Interactions world.setSystem(new BuildableSystem());
         * world.setSystem(new CombatSystem()); world.setSystem(new HarvesterSystem());
         * world.setSystem(new BulletCollisionSystem()); // Active - Render world.setSystem(new
         * CostRenderSystem()); world.setSystem(new HealthRenderSystem()); world.setSystem(new
         * MapRenderSystemInFront()); world.setSystem(new DialogRenderSystem()); world.setSystem(new
         * UIRenderSystem()); world.setSystem(new UIAlertActiveSpawnerSystem()); world.setSystem(new
         * UIAlertBuildableUnderAttack()); world.setSystem(new UIAlertTechpointUnderAttack());
         * world.setSystem(new UIStageRenderSystem()); world.setSystem(new
         * UIStopwatchRenderSytem()); world.setSystem(new DirectorSystem());
         */
        /** UTILITY - MANAGERS */
        world.setManager(new GroupManager());
        world.setManager(new TagManager());
        world.setManager(new UuidEntityManager());
        /** UTILITY - PASSIVE */
        world.setSystem(new CollisionSystem());
        world.setSystem(new EntityFactorySystem());
        world.setSystem(new TiledMapSystem("level1.tmx"));
        world.setSystem(new AssetSystem());
        world.setSystem(new CameraSystem(CAMERA_ZOOM_FACTOR));
        /** CONTROL */
        // control systems.
        /** Agency Systems (Control and Interact) */
        world.setSystem(new PlayerControlSystem());
        world.setSystem(new SlumbererSystem());
        /** Acting Systems (Control and Interact) */
        world.setSystem(new PluckableSystem());
        world.setSystem(new SchedulerSystem());
        world.setSystem(new EntitySpawnerSystem());
        /** SIMULATE */
        /** Physics systems that apply a vector on an entity */
        world.setSystem(new HomingSystem());
        world.setSystem(new GravitySystem());
        /** Physics systems that constrain the movement */
        world.setSystem(new MapCollisionSystem());
        world.setSystem(new ClampedSystem());
        /** Physics systems that move the entity to an absolute location. */
        world.setSystem(new AttachmentSystem());
        world.setSystem(new InbetweenSystem());
        world.setSystem(new MouseCursorSystem());
        /** apply velocity */
        world.setSystem(new PhysicsSystem());
        /** Post Physics Simulations */
        world.setSystem(new AimSystem());
        world.setSystem(new MapWallSensorSystem());
        /** PRE-RENDER */
        world.setSystem(new ColorAnimationSystem());
        /** RENDER */
        /** Camera */
        world.setSystem(new EntityCameraSystem());
        world.setSystem(new CameraShakeSystem());
        /** Rendering */
        world.setSystem(new MapRenderSystem());
        world.setSystem(new AnimRenderSystem());
        world.initialize();

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // limit world delta to prevent clipping through walls.
        world.setDelta(MathUtils.clamp(delta, 0, 1 / 15f));
//        world.process();
        if (!mGameEnd.get()) {
            cardgameFramework.getWorld().process();
        } else {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        commander.unsubscribe(subscriptor);
        cardgameFramework.end();
    }
}
