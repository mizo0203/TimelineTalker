package com.mizo0203.timeline.talker;

import com.mizo0203.timeline.talker.util.RuntimeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Talker {

  private static final String AQUESTALK_PI_PATH = "./aquestalkpi/AquesTalkPi";

  private final ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

  @NotNull private YukkuriVoice mNextVoice = Talker.YukkuriVoice.REIMU;

  public Talker() throws IllegalStateException, SecurityException {
    File file = new File(AQUESTALK_PI_PATH);
    if (!file.isFile()) {
      throw new IllegalStateException(
          file.getPath()
              + " に AquesTalk Pi がありません。\n"
              + "https://www.a-quest.com/products/aquestalkpi.html\n"
              + "からダウンロードしてください。");
    }
    if (!file.canExecute()) {
      throw new IllegalStateException(file.getPath() + " に実行権限がありません。");
    }
  }

  public void talkAlternatelyAsync(final String text) {
    mSingleThreadExecutor.submit(
        new Runnable() {

          @Override
          public void run() {
            try {
              File file = new File("text.txt");
              PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
              pw.println(text);
              pw.flush();
              pw.close();
              RuntimeUtil.execute(
                  new String[] {
                    AQUESTALK_PI_PATH, "-v", mNextVoice.value, "-f", "text.txt", "-o", "out.wav"
                  });
              RuntimeUtil.execute(new String[] {"sh", "-c", "aplay < out.wav"}); // 起動コマンドを指定する

              // 読み上げは、霊夢と魔理沙が交互に行なう
              if (mNextVoice == Talker.YukkuriVoice.REIMU) {
                mNextVoice = Talker.YukkuriVoice.MARISA;
              } else {
                mNextVoice = Talker.YukkuriVoice.REIMU;
              }

              Thread.sleep(2000);
            } catch (@NotNull IOException | InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
  }

  public enum YukkuriVoice {

    /** ゆっくりボイス - 霊夢 */
    REIMU("f1"), //

    /** ゆっくりボイス - 魔理沙 */
    MARISA("f2"), //
    ;

    private final String value;

    YukkuriVoice(String value) {
      this.value = value;
    }
  }
}
