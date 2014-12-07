package net.mostlyoriginal.game.events;

import com.pacoworks.cardframework.eventbus.events.BaseEvent;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
public class KeycodeEvent extends BaseEvent {
    public final int keycode;

    public KeycodeEvent(int keycode) {
        this.keycode = keycode;
    }
}
