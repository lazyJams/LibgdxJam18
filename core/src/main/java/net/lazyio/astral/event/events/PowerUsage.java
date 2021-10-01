package net.lazyio.astral.event.events;

import net.lazyio.astral.entity.Player;
import net.lazyio.astral.event.Event;

public class PowerUsage {

    public static class Start extends Event {

        public final Player player;

        public Start(Player player) {
            this.player = player;
        }
    }

    public static class Stop extends Event {
        public final Player player;

        public Stop(Player player) {
            this.player = player;
        }
    }
}
