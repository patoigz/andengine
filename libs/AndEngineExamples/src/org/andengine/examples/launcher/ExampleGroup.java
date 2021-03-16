package org.andengine.examples.launcher;

import org.andengine.examples.R;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:13:34 - 27.06.2010
 */
public enum ExampleGroup {
  // ===========================================================
  // Elements
  // ===========================================================

  SIMPLE(R.string.examplegroup_simple,
      Example.LINE,
      Example.RECTANGLE,
      Example.SPRITE,
      Example.SPRITEREMOVE,
      Example.SPRITEBATCH,
      Example.NINESLICESPRITE,
      Example.BUTTONSPRITE,
      Example.GRADIENT,
      Example.CLIPENTIY
  ),
  MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation,
      Example.MOVINGBALL,
      Example.ENTITYMODIFIER,
      Example.ENTITYMODIFIERIRREGULAR,
      Example.CARDINALSPLINEMOVEMODIFIER,
      Example.PATHMODIFIER,
      Example.ANIMATEDSPRITES,
      Example.ANIMATIONPACK,
      Example.EASEFUNCTION,
      Example.ROTATION3D
  ),
  TOUCH(R.string.examplegroup_touch,
      Example.TOUCHDRAG,
      Example.MULTITOUCH,
      Example.ANALOGONSCREENCONTROL,
      Example.DIGITALONSCREENCONTROL,
      Example.ANALOGONSCREENCONTROLS,
      Example.COORDINATECONVERSION,
      Example.PINCHZOOM
  ),
  PARTICLESYSTEM(R.string.examplegroup_particlesystems,
      Example.PARTICLESYSTEMSIMPLE,
      Example.PARTICLESYSTEMCOOL,
      Example.PARTICLESYSTEMNEXUS
  ),
  PHYSICS(R.string.examplegroup_physics,
      Example.COLLISIONDETECTION,
      Example.PHYSICS,
      Example.PHYSICSFIXEDSTEP,
      Example.PHYSICSCOLLISIONFILTERING,
      Example.PHYSICSJUMP,
      Example.PHYSICSREVOLUTEJOINT,
      Example.PHYSICSMOUSEJOINT,
      Example.PHYSICSREMOVE
  ),
  TEXT(R.string.examplegroup_text,
      Example.TEXT,
      Example.TICKERTEXT,
      Example.CHANGEABLETEXT,
      Example.TEXTBREAK,
      Example.CUSTOMFONT,
      Example.STROKEFONT,
      Example.BITMAPFONT
  ),
  AUDIO(R.string.examplegroup_audio,
      Example.SOUND,
      Example.MUSIC
  ),
  ADVANCED(R.string.examplegroup_advanced,
      Example.SPLITSCREEN,
      Example.BOUNDCAMERA,
      Example.HULLALGORITHM
  ),
  POSTPROCESSING(R.string.examplegroup_postprocessing,
      Example.MOTIONSTREAK,
      Example.RADIALBLUR
  ),
  BACKGROUND(R.string.examplegroup_background,
      Example.REPEATINGSPRITEBACKGROUND,
      Example.AUTOPARALLAXBACKGROUND,
      Example.TMXTILEDMAP
  ),
  OTHER(R.string.examplegroup_other,
      Example.SCREENCAPTURE,
      Example.PAUSE,
      Example.MENU,
      Example.SUBMENU,
      Example.TEXTMENU,
      Example.ZOOM,
      Example.IMAGEFORMATS,
      Example.PVRTEXTURE,
      Example.PVRCCZTEXTURE,
      Example.PVRGZTEXTURE,
      Example.TEXTUREOPTIONS,
      Example.CANVASTEXTURECOMPOSITING,
      Example.TEXTUREPACK,
      Example.COLORKEYTEXTURESOURCEDECORATOR,
      Example.LOADTEXTURE,
      Example.UPDATETEXTURE,
      Example.UNLOADRESOURCES,
      Example.RUNNABLEPOOLUPDATEHANDLER,
      Example.XMLLAYOUT
  ),
  GAME(R.string.examplegroup_game,
      Example.GAME_SNAKE,
      Example.GAME_RACER
  );

  // ===========================================================
  // Constants
  // ===========================================================

  // ===========================================================
  // Fields
  // ===========================================================

  public final Example[] mExamples;
  public final int mNameResourceID;

  // ===========================================================
  // Constructors
  // ===========================================================

  private ExampleGroup(final int pNameResourceID, final Example... pExamples) {
    this.mNameResourceID = pNameResourceID;
    this.mExamples = pExamples;
  }

  // ===========================================================
  // Getter & Setter
  // ===========================================================

  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================

  // ===========================================================
  // Methods
  // ===========================================================

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
}
