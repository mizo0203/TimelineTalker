package com.mizo0203.timeline.talker;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Java アプリケーション起動時に指定する引数のデータクラス
 *
 * @author みぞ@CrazyBeatCoder
 */
/* package */ class Arguments {

  @Nullable /* package */ final Configuration twitterConfiguration;
  @Nullable /* package */ final MastodonClient mastodonClient;

  /* package */ Arguments(String[] args) throws IllegalArgumentException {
    if (args.length == Argument.values().length) {
      twitterConfiguration =
          createTwitterConfiguration(
              args[Argument.TWITTER_CONSUMER_KEY.ordinal()],
              args[Argument.TWITTER_CONSUMER_SECRET.ordinal()],
              args[Argument.TWITTER_ACCESS_TOKEN.ordinal()],
              args[Argument.TWITTER_ACCESS_TOKEN_SECRET.ordinal()]);
      mastodonClient =
          createMastodonClient(
              args[Argument.MASTODON_INSTANCE_NAME.ordinal()],
              args[Argument.MASTODON_ACCESS_TOKEN.ordinal()]);
    } else if (args.length == Argument.Twitter.values().length) {
      twitterConfiguration =
          createTwitterConfiguration(
              args[Argument.Twitter.CONSUMER_KEY.ordinal()],
              args[Argument.Twitter.CONSUMER_SECRET.ordinal()],
              args[Argument.Twitter.ACCESS_TOKEN.ordinal()],
              args[Argument.Twitter.ACCESS_TOKEN_SECRET.ordinal()]);
      mastodonClient = null;
    } else if (args.length == Argument.Mastodon.values().length) {
      twitterConfiguration = null;
      mastodonClient =
          createMastodonClient(
              args[Argument.Mastodon.INSTANCE_NAME.ordinal()],
              args[Argument.Mastodon.ACCESS_TOKEN.ordinal()]);
    } else {
      throw createIllegalArgumentException();
    }
  }

  private static Configuration createTwitterConfiguration(
      String twitterConsumerKey,
      String twitterConsumerSecret,
      String twitterAccessToken,
      String twitterAccessTokenSecret) {
    return new ConfigurationBuilder()
        .setOAuthConsumerKey(twitterConsumerKey)
        .setOAuthConsumerSecret(twitterConsumerSecret)
        .setOAuthAccessToken(twitterAccessToken)
        .setOAuthAccessTokenSecret(twitterAccessTokenSecret)
        .build();
  }

  private static MastodonClient createMastodonClient(
      String mastodonInstanceName, String mastodonAccessToken) {
    return new MastodonClient.Builder(mastodonInstanceName, new OkHttpClient.Builder(), new Gson())
        .accessToken(mastodonAccessToken)
        .useStreamingApi()
        .build();
  }

  private static IllegalArgumentException createIllegalArgumentException() {
    return new IllegalArgumentException(
        Argument.createExceptionMessage()
            + "\n"
            + Argument.Twitter.createExceptionMessage()
            + "\n"
            + Argument.Mastodon.createExceptionMessage());
  }

  /**
   * Java アプリケーション起動時に指定する引数の定義
   *
   * @author みぞ@CrazyBeatCoder
   */
  private enum Argument {
    TWITTER_CONSUMER_KEY, //
    TWITTER_CONSUMER_SECRET, //
    TWITTER_ACCESS_TOKEN, //
    TWITTER_ACCESS_TOKEN_SECRET, //
    MASTODON_INSTANCE_NAME, //
    MASTODON_ACCESS_TOKEN, //
    ;

    @NotNull
    private static String createExceptionMessage() {
      StringBuilder exceptionMessage =
          new StringBuilder("Twitter と Mastodon の両方を読み上げる場合、 ")
              .append(values().length)
              .append(" つの引数を指定してください。\n");
      for (Argument arg : values()) {
        exceptionMessage
            .append(arg.ordinal() + 1)
            .append(" つ目: ")
            .append(arg.getDetail())
            .append("\n");
      }

      return exceptionMessage.toString();
    }

    @NotNull
    private String getDetail() throws IllegalStateException {
      switch (this) {
        case TWITTER_CONSUMER_KEY:
          return "Twitter Application's Consumer Key (API Key)";
        case TWITTER_CONSUMER_SECRET:
          return "Twitter Application's Consumer Secret (API Secret)";
        case TWITTER_ACCESS_TOKEN:
          return "Twitter Account's Access Token";
        case TWITTER_ACCESS_TOKEN_SECRET:
          return "Twitter Account's Access Token Secret";
        case MASTODON_INSTANCE_NAME:
          return "Mastodon Instance Name";
        case MASTODON_ACCESS_TOKEN:
          return "Mastodon Account's Access Token";
        default:
          throw new IllegalStateException("getDetail this: " + this);
      }
    }

    private enum Twitter {
      CONSUMER_KEY, //
      CONSUMER_SECRET, //
      ACCESS_TOKEN, //
      ACCESS_TOKEN_SECRET, //
      ;

      @NotNull
      private static String createExceptionMessage() {
        StringBuilder exceptionMessage =
            new StringBuilder("Twitter のみを読み上げる場合、 ")
                .append(values().length)
                .append(" つの引数を指定してください。\n");
        for (Argument.Twitter arg : values()) {
          exceptionMessage
              .append(arg.ordinal() + 1)
              .append(" つ目: ")
              .append(arg.getDetail())
              .append("\n");
        }
        return exceptionMessage.toString();
      }

      @NotNull
      private String getDetail() throws IllegalStateException {
        switch (this) {
          case CONSUMER_KEY:
            return TWITTER_CONSUMER_KEY.getDetail();
          case CONSUMER_SECRET:
            return TWITTER_CONSUMER_SECRET.getDetail();
          case ACCESS_TOKEN:
            return TWITTER_ACCESS_TOKEN.getDetail();
          case ACCESS_TOKEN_SECRET:
            return TWITTER_ACCESS_TOKEN_SECRET.getDetail();
          default:
            throw new IllegalStateException("getDetail this: " + this);
        }
      }
    }

    private enum Mastodon {
      INSTANCE_NAME, //
      ACCESS_TOKEN, //
      ;

      @NotNull
      private static String createExceptionMessage() {
        StringBuilder exceptionMessage =
            new StringBuilder("Mastodon のみを読み上げる場合、 ")
                .append(values().length)
                .append(" つの引数を指定してください。\n");
        for (Argument.Mastodon arg : values()) {
          exceptionMessage
              .append(arg.ordinal() + 1)
              .append(" つ目: ")
              .append(arg.getDetail())
              .append("\n");
        }
        return exceptionMessage.toString();
      }

      @NotNull
      private String getDetail() throws IllegalStateException {
        switch (this) {
          case INSTANCE_NAME:
            return MASTODON_INSTANCE_NAME.getDetail();
          case ACCESS_TOKEN:
            return MASTODON_ACCESS_TOKEN.getDetail();
          default:
            throw new IllegalStateException("getDetail this: " + this);
        }
      }
    }
  }
}
