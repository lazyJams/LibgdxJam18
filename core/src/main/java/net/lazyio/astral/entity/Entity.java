package net.lazyio.astral.entity;

import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;

public class Entity {

    public String tag;

    public Vector2 pos;
    public Vector2 vel = new Vector2();
    public Vector2 size;
    public Vector2 bbOff = new Vector2();
    public Vector2 itemPos = new Vector2();

    public World<Entity> world;
    public Item<Entity> item;


    // FIXME: 01/10/2021 
    
    public Entity(Vector2 pos, Vector2 size, String tag) {
        this.pos = pos;
        this.itemPos.set(pos.x + bbOff.x, pos.y + this.bbOff.y);
        this.size = size;
        this.tag = tag;
    }

    public Entity build(World<Entity> world) {
        this.world = world;
        this.item = new Item<>(this);
        this.world.add(this.item, this.pos.x + this.bbOff.x, this.pos.y + this.bbOff.y, this.size.x, this.size.y);
        return this;
    }
}
