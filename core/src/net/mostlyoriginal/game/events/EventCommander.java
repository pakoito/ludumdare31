
package net.mostlyoriginal.game.events;

import com.pacoworks.cardframework.eventbus.IEventCommander;
import com.pacoworks.cardframework.eventbus.events.BaseEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Paco on 20/09/2014. See LICENSE.md
 */
public class EventCommander implements IEventCommander {
    private final Bus mEventBus = new Bus(ThreadEnforcer.ANY);

    public Bus getEventBus() {
        return mEventBus;
    }

    public EventCommander() {
    }
    public void subscribe(Object subscriptor) {
        getEventBus().register(subscriptor);
    }

    public void unsubscribe(Object subscriptor) {
        getEventBus().unregister(subscriptor);
    }

    public void postAnyEvent(BaseEvent event) {
        getEventBus().post(event);
    }
}
