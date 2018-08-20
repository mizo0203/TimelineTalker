package com.mizo0203.timeline.talker.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RuntimeUtil {

  public static void execute(String[] cmdarray) {
    try {
      Process process = Runtime.getRuntime().exec(cmdarray);
      process.waitFor();
      process.destroy();
    } catch (@NotNull IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
