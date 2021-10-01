package net.lazyio.astral.entity;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.World;
import net.lazyio.astral.util.PointMapObject;

public class EntityFactory {

    public static Player createPlayer(World<Entity> world, Vector2 pos) {
        Player player = (Player) new Player(pos, new Vector2(8f, 11f)).build(world);
        player.setRespawnPoint(pos.x, pos.y);
        return player;
    }

    public static void createTiledRectObj(World<Entity> world, RectangleMapObject rect) {
        String tag = rect.getName() != null ? rect.getName() : EntityTags.NONE;
        if (EntityTags.AFFECT.equals(tag)) {
            new AffectableEntity(
                    new Vector2(rect.getRectangle().x, rect.getRectangle().y),
                    new Vector2(rect.getRectangle().width, rect.getRectangle().height)
            ).build(world);
        } else {
            new Entity(
                    new Vector2(rect.getRectangle().x, rect.getRectangle().y),
                    new Vector2(rect.getRectangle().width, rect.getRectangle().height),
                    tag
            ).build(world);
        }
    }

    public static void spawnEnemy(World<Entity> world, PointMapObject point) {
        Enemy enemy = (Enemy) new Enemy(new Vector2(point.x, point.y)).build(world);
        enemy.setSpawnPoint(point.x, point.y);
    }
}
