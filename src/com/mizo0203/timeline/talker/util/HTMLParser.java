package com.mizo0203.timeline.talker.util;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HTMLParser {

  @NotNull
  public String parse(@NotNull String html, @NotNull Charset encoding, boolean ignoreCharSet)
      throws IOException {
    try (InputStreamReader r =
        new InputStreamReader(IOUtils.toInputStream(html, encoding), encoding)) {
      HTMLParserCallback hp = new HTMLParserCallback();
      ParserDelegator parser = new ParserDelegator();
      parser.parse(r, hp, ignoreCharSet);
      return hp.getText();
    }
  }

  /**
   * http://www.my-notebook.net/736a69e0-820c-423b-9047-a02b8a9eefb1.html
   *
   * <p>HTMLParser.java
   */
  private static class HTMLParserCallback extends HTMLEditorKit.ParserCallback {
    private final StringBuffer sb = new StringBuffer();

    private String getText() {
      return sb.toString();
    }

    @Override
    public void handleText(@NotNull char[] data, int pos) {
      sb.append(new String(data));
      sb.append(System.getProperty("line.separator"));
    }
  }
}
