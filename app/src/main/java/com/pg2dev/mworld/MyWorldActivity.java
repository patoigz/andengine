package com.pg2dev.mworld;

import android.graphics.Typeface;
import android.widget.Toast;

import com.pg2dev.mworld.utils.Constant;
import com.pg2dev.mworld.utils.Debug;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;

import java.io.IOException;

public class MyWorldActivity extends SimpleBaseGameActivity {
  // ===========================================================
  // Constants
  // ===========================================================

  private int CAMERA_WIDTH;
  private int CAMERA_HEIGHT;

  // ===========================================================
  // Fields
  // ===========================================================

  private BoundCamera mBoundChaseCamera;
  private TiledTextureRegion mPlayerTextureRegion;

  private TMXTiledMap mTMXTiledMap;
  public static int mCactusCount;

  private Font mFont;
  private Scene mScene;
  private HUD mHud;

  private ITexture mOnScreenControlBaseTexture;
  private ITextureRegion mOnScreenControlBaseTextureRegion;
  private ITexture mOnScreenControlKnobTexture;
  private ITextureRegion mOnScreenControlKnobTextureRegion;

  private boolean setControlsAtDifferentLocation = false;
  private int centerX;
  private int centerY;
  private AnimatedSprite mPlayer;

  private AnalogOnScreenControl mAnalogOnScreenControl;
  private DigitalOnScreenControl mDigitalOnScreenControl;

  // ===========================================================
  // Constructors
  // ===========================================================

  // ===========================================================
  // Getter & Setter
  // ===========================================================

  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================

  @Override
  public EngineOptions onCreateEngineOptions() {
    initCamera();
    Debug.info("Load Camera!");

    final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mBoundChaseCamera);

    engineOptions.getTouchOptions().setNeedsMultiTouch(true);
    Debug.info("Setting Engine Options!");

    initMultiTouch();
    Debug.info("Setting Multi Touch!");

    return engineOptions;
  }

  private void initMultiTouch() {
    if (MultiTouch.isSupported(this)) {
      if (MultiTouch.isSupportedDistinct(this)) {
        Debug.info("MultiTouch detected!");
        Toast.makeText(this, "MultiTouch detected!", Toast.LENGTH_SHORT).show();
      } else {
        setControlsAtDifferentLocation = true;
        Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG)
            .show();
        Debug.warn("MultiTouch detected, but your device has problems distinguishing between fingers!");
      }
    } else {
      Toast.makeText(this, "Sorry your device does NOT support MultiTouch!", Toast.LENGTH_LONG).show();
      Debug.error("Your device does NOT support MultiTouch!");
    }
  }

  private void initCamera() {
//    try {
//      DisplayMetrics metrics = new DisplayMetrics();
//      getWindowManager().getDefaultDisplay().getMetrics(metrics);
//      CAMERA_WIDTH = metrics.widthPixels;
//      CAMERA_HEIGHT = metrics.heightPixels;
//    } catch (Exception ex) {
    CAMERA_WIDTH = Constant.CAMERA_WIDTH;
    CAMERA_HEIGHT = Constant.CAMERA_HEIGHT;
//    }
    mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    mBoundChaseCamera.setBoundsEnabled(false);

    centerX = CAMERA_WIDTH / 2;
    centerY = CAMERA_HEIGHT / 2;
  }

  @Override
  public void onCreateResources() throws IOException {
    loadfonts();
    Debug.info("Load Fonts!");

    loadControlsTexture();
    loadPlayerTexture();
    Debug.info("Load Textures!");
  }

  private void loadfonts() {
    mFont = FontFactory.create(getFontManager(), getTextureManager(), 256, 256,
        TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Constant.COLOR_BLUE);
    mFont.load();
  }

  private void loadControlsTexture() throws IOException {
    mOnScreenControlBaseTexture = new AssetBitmapTexture(getTextureManager(), getAssets(), "gfx/onscreen_control_base.png", TextureOptions.BILINEAR);
    mOnScreenControlBaseTextureRegion = TextureRegionFactory.extractFromTexture(mOnScreenControlBaseTexture);
    mOnScreenControlBaseTexture.load();

    mOnScreenControlKnobTexture = new AssetBitmapTexture(getTextureManager(), getAssets(), "gfx/onscreen_control_knob.png", TextureOptions.BILINEAR);
    mOnScreenControlKnobTextureRegion = TextureRegionFactory.extractFromTexture(mOnScreenControlKnobTexture);
    mOnScreenControlKnobTexture.load();
  }

  private void loadPlayerTexture() throws IOException {
    ITexture mPlayerTexture = new AssetBitmapTexture(getTextureManager(), getAssets(), "gfx/player.png", TextureOptions.DEFAULT);
    mPlayerTextureRegion = TextureRegionFactory.extractTiledFromTexture(mPlayerTexture, 3, 4);
    mPlayerTexture.load();
  }
  /*






   */


  private void initScene() {
    final FPSCounter fpsCounter = new FPSCounter();
    mEngine.registerUpdateHandler(fpsCounter);
    mScene = new Scene();
    mScene.getBackground().setColor(0, 0, 0);
    mHud = new HUD();
//    final Text elapsedText = new Text(5, 320, mFont, "Seconds elapsed:", "Seconds elapsed: XXXXXX".length(), getVertexBufferObjectManager());
    final Text fpsText = new Text(centerX, 160, mFont, "FPS:", "FPS: XXXXX".length(), getVertexBufferObjectManager());
//    mHud.attachChild(elapsedText);
    mHud.attachChild(fpsText);
    mScene.registerUpdateHandler(new TimerHandler(1 / 10.0f, true, pTimerHandler -> {
//      elapsedText.setText(String.format("Seconds elapsed: %.2f", MyWorldActivity.mEngine.getSecondsElapsedTotal()));
      fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFPS()));
    }));

    mBoundChaseCamera.setHUD(mHud);
  }

  private void loadTMXMap() {
    try {
      final TMXLoader tmxLoader = new TMXLoader(getAssets(), mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, getVertexBufferObjectManager(), (pTMXTiledMap, pTMXLayer, pTMXTile, pTMXTileProperties) -> {
        /* We are going to count the tiles that have the property "cactus=true" set. */
        if (pTMXTileProperties.containsTMXProperty("cactus", "true")) {
          MyWorldActivity.mCactusCount++;
        }
      });
      mTMXTiledMap = tmxLoader.loadFromAsset("tmx/desert.tmx");
      mTMXTiledMap.setOffsetCenter(0, 0);

      toastOnUiThread("Cactus count in this TMXTiledMap: " + MyWorldActivity.mCactusCount, Toast.LENGTH_LONG);

      mScene.attachChild(mTMXTiledMap);

      /* Make the camera not exceed the bounds of the TMXEntity. */
      mBoundChaseCamera.setBoundsEnabled(false);
      mBoundChaseCamera.setBounds(0, 0, mTMXTiledMap.getWidth(), mTMXTiledMap.getHeight());
      mBoundChaseCamera.setBoundsEnabled(true);
    } catch (final TMXLoadException e) {
      Debug.error(e);
    }
  }

  private void initPlayer() {
    /* Create the sprite and add it to the mScene. */
    mPlayer = new AnimatedSprite(centerX, centerY, mPlayerTextureRegion, getVertexBufferObjectManager());
    mPlayer.setOffsetCenterY(0);
    mBoundChaseCamera.setChaseEntity(mPlayer);

    final PathModifier.Path path = new PathModifier.Path(5).to(50, 740).to(50, 1000).to(820, 1000).to(820, 740).to(0);

    mPlayer.registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null, new IPathModifierListener() {

      @Override
      public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
      }

      @Override
      public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
        switch (pWaypointIndex) {
          case 0:
            mPlayer.animate(new long[]{200, 200, 200}, 0, 2, true);
            break;
          case 1:
            mPlayer.animate(new long[]{200, 200, 200}, 3, 5, true);
            break;
          case 2:
            mPlayer.animate(new long[]{200, 200, 200}, 6, 8, true);
            break;
          case 3:
            mPlayer.animate(new long[]{200, 200, 200}, 9, 11, true);
            break;
        }
      }

      @Override
      public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {

      }

      @Override
      public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {

      }
    })));

    /* Now we are going to create a rectangle that will  always highlight the tile below the feet of the pEntity. */
//    final Rectangle currentTileRectangle = new Rectangle(0, 0, mTMXTiledMap.getTileWidth(), mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
    /* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
//    currentTileRectangle.setOffsetCenter(0, 0);
//    currentTileRectangle.setColor(1, 0, 0, 0.25f);
//    mScene.attachChild(currentTileRectangle);
  }

  private void animatePlayer() {
    /* The layer for the mPlayer to walk on. */
    final TMXLayer tmxLayer = mTMXTiledMap.getTMXLayers().get(0);

    mScene.registerUpdateHandler(new IUpdateHandler() {

      @Override
      public void reset() {
      }

      @Override
      public void onUpdate(final float pSecondsElapsed) {
        /* Get the mScene-coordinates of the players feet. */
        final float[] playerFootCordinates = mPlayer.convertLocalCoordinatesToSceneCoordinates(16, 1);

        /* Get the tile the feet of the mPlayer are currently waking on. */
        final TMXTile tmxTile = tmxLayer.getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]);
        if (tmxTile != null) {
          // tmxTile.setTextureRegion(null); <-- Eraser-style removing of tiles =D
//          currentTileRectangle.setPosition(tmxLayer.getTileX(tmxTile.getTileColumn()), tmxLayer.getTileY(tmxTile.getTileRow()));
        }
      }
    });
    mScene.attachChild(mPlayer);
  }

  private void initScreenControls() {
    loadFirstAnalogControl();
//    loadSecondAnalogControl();

    loadDigitalControl();
  }

  private void loadFirstAnalogControl() {
    mAnalogOnScreenControl = new AnalogOnScreenControl(0, 0,
        mBoundChaseCamera, mOnScreenControlBaseTextureRegion, mOnScreenControlKnobTextureRegion, 0.1f,
        getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
      @Override
      public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {

      }

      @Override
      public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
        /* Nothing. */
      }
    });

    final Sprite controlBase = mAnalogOnScreenControl.getControlBase();
    controlBase.setAlpha(0.5f);
    controlBase.setOffsetCenter(0, 0);

    mScene.setChildScene(mAnalogOnScreenControl);
  }

  private void loadSecondAnalogControl() {
    final float y = (setControlsAtDifferentLocation) ? CAMERA_HEIGHT : 0;
    final AnalogOnScreenControl mRotationOnScreenControl = new AnalogOnScreenControl(CAMERA_WIDTH, y,
        mBoundChaseCamera, mOnScreenControlBaseTextureRegion, mOnScreenControlKnobTextureRegion, 0.1f,
        getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
      @Override
      public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
      }

      @Override
      public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
        /* Nothing. */
      }
    });

    final Sprite controlBase = mRotationOnScreenControl.getControlBase();
    if (setControlsAtDifferentLocation) {
      controlBase.setOffsetCenter(1, 1);
    } else {
      controlBase.setOffsetCenter(1, 0);
    }
    controlBase.setAlpha(0.5f);

    mAnalogOnScreenControl.setChildScene(mRotationOnScreenControl);
  }

  private void loadDigitalControl() {
    final float y = (setControlsAtDifferentLocation) ? CAMERA_HEIGHT : 0;
    mDigitalOnScreenControl = new DigitalOnScreenControl(CAMERA_WIDTH, y,
        mBoundChaseCamera, mOnScreenControlBaseTextureRegion, mOnScreenControlKnobTextureRegion, 0.1f,
        getVertexBufferObjectManager(), (pBaseOnScreenControl, pValueX, pValueY) -> {
    });

    final Sprite controlBase = mDigitalOnScreenControl.getControlBase();
    if (setControlsAtDifferentLocation) {
      controlBase.setOffsetCenter(1, 1);
    } else {
      controlBase.setOffsetCenter(1, 0);
    }
    controlBase.setAlpha(0.5f);

    mAnalogOnScreenControl.setChildScene(mDigitalOnScreenControl);
  }

  @Override
  public Scene onCreateScene() {
    initScene();
    Debug.info("Scene Created!");

    loadTMXMap();
    Debug.info("Load Tiled Map!");

    initScreenControls();
    Debug.info("Init Controls!");

    initPlayer();
    Debug.info("Load Player!");

    animatePlayer();
    Debug.info("Animate Player!");

    return mScene;
  }

  // ===========================================================
  // Methods
  // ===========================================================

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
}
