// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.tq;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class ControlBox {

  private static MemcacheService mc = MemcacheServiceFactory.getMemcacheService();

  static void stop() {
    mc.delete("GO");
  }

  static void start() {
    mc.put("GO", true);
  }

  static boolean isRunning() {
    return mc.get("GO") != null;
  }
}
