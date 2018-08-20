package com.mizo0203.timeline.talker.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayNameUtil {

  public static String removeContext(@NotNull String name) {
    Pattern p = Pattern.compile("([^@＠]+).+");
    Matcher m = p.matcher(name);
    return m.replaceFirst("$1");
  }
}
