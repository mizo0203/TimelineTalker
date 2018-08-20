package com.mizo0203.timeline.talker;

import com.mizo0203.timeline.talker.util.DisplayNameUtil;
import com.mizo0203.timeline.talker.util.HTMLParser;
import com.mizo0203.timeline.talker.util.UrlUtil;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.entity.Account;
import com.sys1yagi.mastodon4j.api.entity.Notification;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.method.Streaming;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MastodonTimelineTalker implements TimelineTalker {

  @NotNull private final Streaming mStreaming;
  @NotNull private final OnStatusEvent mOnStatusEvent;

  /* package */ MastodonTimelineTalker(MastodonClient client, Talker talker) {
    mStreaming = new Streaming(client);
    mOnStatusEvent = new OnStatusEvent(talker);
  }

  @Override
  public void start() {

    try {
      mStreaming.user(mOnStatusEvent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static class OnStatusEvent implements Handler {

    private final Talker mTalker;

    private OnStatusEvent(Talker talker) {
      mTalker = talker;
    }

    @Override
    public void onStatus(@NotNull Status status) {

      try {
        final StringBuffer buffer = new StringBuffer();
        final Status reblogStatus = status.getReblog();

        String displayName = "誰か";
        if (status.getAccount() != null) {
          displayName = status.getAccount().getDisplayName();
        }
        if (reblogStatus != null) {
          String reblogDisplayName = "誰か";
          if (status.getAccount() != null) {
            displayName = status.getAccount().getDisplayName();
          }
          buffer.append(DisplayNameUtil.removeContext(displayName)).append("さんがブースト。");
          buffer.append(DisplayNameUtil.removeContext(reblogDisplayName)).append("さんから、");
          buffer.append(
              new HTMLParser().parse(reblogStatus.getContent(), StandardCharsets.UTF_8, true));
        } else {
          buffer.append(DisplayNameUtil.removeContext(displayName)).append("さんから、");
          buffer.append(new HTMLParser().parse(status.getContent(), StandardCharsets.UTF_8, true));
        }

        final String talkText = UrlUtil.convURLEmpty(buffer).replaceAll("\n", "。");
        mTalker.talkAlternatelyAsync(talkText);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onNotification(@NotNull Notification notification) {
      final StringBuilder buffer = new StringBuilder();
      final Account account = notification.getAccount();

      if (account == null) {
        return;
      }

      switch (notification.getType()) {
        case "mention":
          buffer
              .append(DisplayNameUtil.removeContext(account.getDisplayName()))
              .append("さんがあなたをメンションしました。");
          break;
        case "reblog":
          buffer
              .append(DisplayNameUtil.removeContext(account.getDisplayName()))
              .append("さんがあなたのトゥートをブーストしました。");
          break;
        case "favourite":
          buffer
              .append(DisplayNameUtil.removeContext(account.getDisplayName()))
              .append("さんがあなたのトゥートをお気に入りに登録しました。");
          break;
        case "follow":
          buffer
              .append(DisplayNameUtil.removeContext(account.getDisplayName()))
              .append("さんにフォローされました。");
          break;
        default:
          return;
      }

      final String talkText = buffer.toString();
      mTalker.talkAlternatelyAsync(talkText);
    }

    @Override
    public void onDelete(long id) {
      /* no op */
    }
  }
}
