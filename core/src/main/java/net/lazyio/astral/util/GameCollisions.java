package net.lazyio.astral.util;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import net.lazyio.astral.entity.Entity;
import net.lazyio.astral.entity.EntityTags;

public class GameCollisions {

    public static final CollisionFilter playerFilter = GameCollisions::resolvePlayerFilter;

    private static Response resolvePlayerFilter(Item<Entity> item, Item<Entity> other) {
        switch (other.userData.tag) {
            case EntityTags.ENEMY:
            case EntityTags.GOAL:
                return Response.cross;
        }
        return Response.slide;
    }

    public static final CollisionFilter enemyFilter = GameCollisions::resolveEnemyFilter;

    private static Response resolveEnemyFilter(Item<Entity> item, Item<Entity> other) {
        switch (other.userData.tag) {
            case EntityTags.GOAL:
            case EntityTags.PLAYER:
                return Response.cross;
        }
        return Response.slide;
    }
}
