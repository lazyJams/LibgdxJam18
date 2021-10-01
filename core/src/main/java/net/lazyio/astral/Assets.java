package net.lazyio.astral;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lazyio.astral.util.AssetProvider;

public class Assets {

    private final static AssetManager manager = new AssetManager();

    public static final AssetProvider<Skin> defaultSkin = new AssetProvider<>(manager, "skin/uiskin.json", Skin.class);

    public static final AssetProvider<Texture> logo = new AssetProvider<>(manager, "logo.png", Texture.class);
    public static final AssetProvider<Texture> shapeDrawer = new AssetProvider<>(manager, "shapeDrawer.png", Texture.class);
    public static final AssetProvider<Texture> player = new AssetProvider<>(manager, "player.png", Texture.class);
    public static final AssetProvider<Texture> enemy = new AssetProvider<>(manager, "enemy.png", Texture.class);
    public static final AssetProvider<Texture> tutWall = new AssetProvider<>(manager, "tutWall.png", Texture.class);
    public static final AssetProvider<Texture> spiralParticle = new AssetProvider<>(manager, "spiralParticle.png", Texture.class);

    public static final AssetProvider<Sound> jumpSound = new AssetProvider<>(manager, "sounds/jump.wav", Sound.class);
    public static final AssetProvider<Sound> hurtSound = new AssetProvider<>(manager, "sounds/hurt.wav", Sound.class);

    public static void init() {
        manager.finishLoading();
    }

    public static void dispose() {
        manager.dispose();
    }
}