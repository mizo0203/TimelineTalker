package com.mizo0203.timeline.talker;

import com.mizo0203.timeline.talker.util.DisplayNameUtil;
import com.mizo0203.timeline.talker.util.UrlUtil;
import org.jetbrains.annotations.NotNull;
import twitter4j.*;
import twitter4j.conf.Configuration;

import java.util.Collections;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TwitterTimelineTalker implements TimelineTalker {

  /** ISO 639 言語コード - 日本語 (ja) */
  private static final String LANG_JA = Locale.JAPAN.getLanguage();

  @NotNull private final RequestHomeTimelineTimerTask mRequestHomeTimelineTimerTask;

  public TwitterTimelineTalker(@NotNull Configuration configuration, Talker talker) {
    Twitter twitter = new TwitterFactory(configuration).getInstance();
    mRequestHomeTimelineTimerTask = new RequestHomeTimelineTimerTask(twitter, talker);
  }

  @Override
  public void start() {
    new Timer().schedule(mRequestHomeTimelineTimerTask, 0L, TimeUnit.MINUTES.toMillis(1));
  }

  private static class RequestHomeTimelineTimerTask extends TimerTask {

    private static final int HOME_TIMELINE_COUNT_MAX = 200;
    private static final int HOME_TIMELINE_COUNT_MIN = 1;

    private final Twitter mTwitter;
    private final Talker mTalker;

    /** mStatusSinceId より大きい（つまり、より新しい） ID を持つ HomeTimeline をリクエストする */
    private long mStatusSinceId = 1L;

    private boolean mIsUpdatedStatusSinceId = false;

    private RequestHomeTimelineTimerTask(Twitter twitter, Talker talker) {
      mTwitter = twitter;
      mTalker = talker;
    }

    /** The action to be performed by this timer task. */
    @Override
    public void run() {
      try {
        // mStatusSinceId が未更新ならば、 Status を 1 つだけ取得する
        int count = mIsUpdatedStatusSinceId ? HOME_TIMELINE_COUNT_MAX : HOME_TIMELINE_COUNT_MIN;
        Paging paging = new Paging(1, count, mStatusSinceId);
        ResponseList<Status> statusResponseList = mTwitter.getHomeTimeline(paging);

        if (statusResponseList.isEmpty()) {
          return;
        }

        // mStatusSinceId を、取得した最新の ID に更新する
        mStatusSinceId = statusResponseList.get(0).getId();
        mIsUpdatedStatusSinceId = true;

        // Status が古い順になるよう、 statusResponseList を逆順に並び替える
        Collections.reverse(statusResponseList);

        for (Status status : statusResponseList) {
          onStatus(status);
        }

      } catch (TwitterException e) {
        e.printStackTrace();
      }
    }

    private void onStatus(final Status status) {
      if (!LANG_JA.equalsIgnoreCase(status.getLang())) {
        return;
      }

      final StringBuffer buffer = new StringBuffer();

      if (status.isRetweet()) {
        Status retweetedStatus = status.getRetweetedStatus();
        buffer
            .append(DisplayNameUtil.removeContext(status.getUser().getName()))
            .append("さんがリツイート。");
        buffer
            .append(DisplayNameUtil.removeContext(retweetedStatus.getUser().getName()))
            .append("さんから、");
        buffer.append(retweetedStatus.getText());
      } else {
        buffer.append(DisplayNameUtil.removeContext(status.getUser().getName())).append("さんから、");
        buffer.append(status.getText());
      }

      mTalker.talkAlternatelyAsync(UrlUtil.convURLEmpty(buffer).replaceAll("\n", "。"));
    }
  }
}
