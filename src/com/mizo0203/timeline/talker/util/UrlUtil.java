package com.mizo0203.timeline.talker.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** http://chat-messenger.net/blog-entry-40.html */
public class UrlUtil {
  /** URLを抽出するための正規表現パターン */
  @SuppressWarnings("Annotator")
  private static final Pattern convURLLinkPtn =
      Pattern.compile(
          "(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);

  /**
   * 指定された文字列内のURLを、正規表現を使用し、 空文字列に変換する。
   *
   * @param str 指定の文字列。
   * @return リンクに変換された文字列。
   */
  public static String convURLEmpty(@NotNull CharSequence str) {
    Matcher matcher = convURLLinkPtn.matcher(str);
    return matcher.replaceAll("");
  }
}
