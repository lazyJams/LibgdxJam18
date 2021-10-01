package net.lazyio.astral.util;

import com.badlogic.gdx.maps.MapObject;

public class PointMapObject extends MapObject {

    public float x;
    public float y;

    public PointMapObject() {
        this(0f, 0f);
    }

    public PointMapObject(float x, float y) {
        super();
        this.x = x;
        this.y = y;
    }
}
