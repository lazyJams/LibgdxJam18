package net.lazyio.astral;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.World;
import net.lazyio.astral.entity.Entity;
import net.lazyio.astral.entity.EntityFactory;
import net.lazyio.astral.entity.EntityTags;
import net.lazyio.astral.entity.Player;
import net.lazyio.astral.entity.behaviors.IRender;
import net.lazyio.astral.entity.behaviors.ITick;
import net.lazyio.astral.util.PointMapObject;

public class GameManager {

    public World<Entity> world;

    public Player player;

    public GameManager(int cellSize) {
        this.world = new World<>(cellSize);
    }

    public void createEntitiesFromMapObjects(MapObjects mapObjects) {
        PointMapObject spawn = (PointMapObject) mapObjects.get("Spawn");
        this.player = EntityFactory.createPlayer(world, new Vector2(spawn.x, spawn.y));

        mapObjects.forEach(obj -> {
            if (obj instanceof RectangleMapObject) {
                EntityFactory.createTiledRectObj(world, (RectangleMapObject) obj);
            } else if (obj instanceof PointMapObject && obj.getName().equals(EntityTags.ENEMY)) {
                EntityFactory.spawnEnemy(world, (PointMapObject) obj);
            }
        });

        mapObjects.getByType(RectangleMapObject.class).forEach(rect -> EntityFactory.createTiledRectObj(world, rect));
    }

    public void tick(float dt) {
        world.getItems()
                .stream()
                .filter(item -> item.userData instanceof ITick)
                .map(item -> (ITick) item.userData)
                .forEach(render -> render.tick(dt));
    }

    public void render(SpriteBatch batch, float dt) {
        world.getItems()
                .stream()
                .filter(item -> item.userData instanceof IRender)
                .map(item -> (IRender) item.userData)
                .forEach(render -> render.render(batch, dt));
    }
}
