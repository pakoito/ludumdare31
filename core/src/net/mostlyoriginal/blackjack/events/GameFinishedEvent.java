package net.mostlyoriginal.blackjack.events;

import com.pacoworks.cardframework.eventbus.events.BaseEvent;

/**
 * Created by Paco on 07/12/2014.
 * See LICENSE.md
 */
public class GameFinishedEvent extends BaseEvent {

    private final int winnerPosition;

    private final int winnerCount;

    public GameFinishedEvent(int winnerPosition, int winnerCount) {
        this.winnerPosition = winnerPosition;
        this.winnerCount = winnerCount;
    }

    public int getWinnerPosition() {
        return winnerPosition;
    }

    public int getWinnerCount() {
        return winnerCount;
    }
}
