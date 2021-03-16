package com.pg2dev.mworld.utils;

import android.util.Log;

public final class Debug {
  private static final String TAG_INFO = "info";
  private static final String TAG_ERROR = "error";
  private static final String TAG_WARN = "warn";

  public static void info(Object msj) {
    Log.i(TAG_INFO, msj.toString());
  }

  public static void error(Object msj) {
    Log.i(TAG_ERROR, msj.toString());
  }

  public static void warn(Object msj) {
    Log.i(TAG_WARN, msj.toString());
  }

}
