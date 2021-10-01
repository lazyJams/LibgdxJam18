package net.lazyio.astral.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;

public class AssetProvider<T> extends AssetDescriptor<T> {

    private final AssetManager manager;

    public AssetProvider(AssetManager manager, String fileName, Class<T> assetType) {
        super(fileName, assetType);
        this.manager = manager;
        this.manager.load(this);
    }

    public T get() {
        return this.manager.get(this);
    }
}
