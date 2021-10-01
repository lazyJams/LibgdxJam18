package net.lazyio.astral.event.events;

import com.dongbat.jbump.Item;
import net.lazyio.astral.entity.Entity;
import net.lazyio.astral.event.Event;

public class CollisionEvent extends Event {

    public Item<Entity> self;
    public Item<Entity> other;

    public CollisionEvent(Item<Entity> self, Item<Entity> other) {
        this.self = self;
        this.other = other;
    }
}
