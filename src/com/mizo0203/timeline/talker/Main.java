package com.mizo0203.timeline.talker;

import org.jetbrains.annotations.NotNull;

/**
 * Java アプリケーション起動時に実行されるクラス
 *
 * @author みぞ@CrazyBeatCoder
 */
public class Main {

  public static void main(@NotNull String[] args) {
    TimelineTalker twitterTimelineTalker;
    TimelineTalker mastodonTimelineTalker;
    Talker talker;

    try {
      Arguments arguments = new Arguments(args);
      talker = new Talker();

      if (arguments.twitterConfiguration != null) {
        twitterTimelineTalker = new TwitterTimelineTalker(arguments.twitterConfiguration, talker);
        twitterTimelineTalker.start();
      }

      if (arguments.mastodonClient != null) {
        mastodonTimelineTalker = new MastodonTimelineTalker(arguments.mastodonClient, talker);
        mastodonTimelineTalker.start();
      }

    } catch (@NotNull IllegalArgumentException | IllegalStateException e) {
      System.err.println(e.getMessage());
      return;
    }

    talker.talkAlternatelyAsync("アプリケーションを起動しました");
  }
}
