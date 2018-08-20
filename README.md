# TimelineTalker
Raspberry Pi でゆっくり達に Twitter &amp; Mastodon タイムラインを読み上げてもらう

## 使い方

### 動作確認環境

|              | Version                          |
|:-------------|:---------------------------------|
| ボード        | Raspberry Pi 3 Model B           |
| OS           | Raspbian GNU/Linux 9.4 (stretch) |
| Java         | OpenJDK 9-Raspbian               |
| AquesTalk Pi | Ver.1.00                         |

### Twitter API Key を取得する (任意)

[Twitter Application Management](https://apps.twitter.com) から、下記 4 つの Key を取得します。

* Consumer Key (API Key)
* Consumer Secret (API Secret)
* Access Token
* Access Token Secret

[phi 様の記事](http://phiary.me/twitter-api-key-get-how-to/)がわかりやすいので、詳細手順は割愛します。

Access level は Read and Write がデフォルトのようですが、本アプリに投稿機能は無いため Read only に変更しても問題ありません。

もし、Access Token の発行後に Access level を変更したのであれば、Access Token を Regenerate してください。

### Mastodon の Access Token を取得する (任意)

例) mstdn.jp の場合

1. https://mstdn.jp/settings/applications/new にアクセス
2. 下記を入力・設定して送信
    * アプリの名前: 任意の値
    * アプリのウェブサイト: 任意の値
    * アクセス権: read のみをチェック
3. Access Token が発行される

### TimelineTalker および音声合成アプリ「AquesTalk Pi」のダウンロード

1. [AquesTalk Pi](https://www.a-quest.com/products/aquestalkpi.html) をダウンロード、および tgz ファイルを展開
2. [TimelineTalker.jar](http://redmine.mizo0203.com/projects/timeline-talker/files) から最新版をダウンロード
3. ダウンロードおよび展開したファイルを下記のように配置
	* TimelineTalker.jar
	* aquestalkpi/AquesTalkPi
	* aquestalkpi/aq_dic

### TimelineTalker.jar を起動

Raspberry Pi にスピーカーを接続して、下記コマンドを実行してください。

引数 \<Mastodon Instance Name\> は Mastodon のインスタンス名に置き換えてください。 例) mstdn.jp  
その他の引数は、上記で取得した値に置き換えてください  

ゆっくり霊夢が『アプリケーションを起動しました』と発声します。その後、タイムラインに更新があれば、ゆっくり霊夢・ゆっくり魔理沙が交互にツイートを読み上げます。

#### Twitter と Mastodon の両方を読み上げる場合は 6 つの引数を指定

```bash
$ nohup java -jar TimelineTalker.jar <Twitter Consumer Key> <Twitter Consumer Secret> <Twitter Access Token> <Twitter Access Token Secret> <Mastodon Instance Name> <Mastodon Account's Access Token> &
```

#### Twitter のみを読み上げる場合は 4 つの引数を指定

```bash
$ nohup java -jar TimelineTalker.jar <Twitter Consumer Key> <Twitter Consumer Secret> <Twitter Access Token> <Twitter Access Token Secret> &
```

#### Mastodon のみを両方を読み上げる場合は 2 つの引数を指定

```bash
$ nohup java -jar TimelineTalker.jar <Mastodon Instance Name> <Mastodon Account's Access Token> &
```

## 使用しているもの

### 音声合成アプリ「AquesTalk Pi」

YouTube やニコニコ動画でおなじみの「ゆっくりボイス」とは、[株式会社アクエスト](https://www.a-quest.com/index.html)社製の音声合成エンジン「[AquesTalk](https://www.a-quest.com/products/aquestalk_1.html)」によって生成された音声です。有名な読み上げフリーソフトに「[SofTalk](https://www35.atwiki.jp/softalk/)」や「[棒読みちゃん](http://chi.usamimi.info/Program/Application/BouyomiChan/)」がありますが、いずれにも「AquesTalk」が使われています。

音声合成アプリ「[AquesTalk Pi](https://www.a-quest.com/products/aquestalkpi.html)」には、ARM 用にビルドされた「AquesTalk」が使われています。また、言語処理エンジン「[AqKanji2Koe](https://www.a-quest.com/products/aqkanji2koe.html)」もアプリに含まれるため、漢字も読み上げてくれます。

個人かつ非営利に限り、無償で使用することができます。

### Java ライブラリ「Twitter4J」

Twitter の statuses/home_timeline API を使用するため、[Twitter4J](http://twitter4j.org/)を使用しています。

Apache License 2.0 で使用できる Twitter 非公式のライブラリです。

![powered-by-twitter4j-138x30.png](https://qiita-image-store.s3.amazonaws.com/0/40619/6e7bb573-0590-e434-42f3-759355d3971c.png "powered-by-twitter4j-138x30.png")

### Java ライブラリ「sys1yagi/mastodon4j」

Mastodon の Streaming API を使用するため、[sys1yagi/mastodon4j](https://github.com/sys1yagi/mastodon4j)を使用しています。

MIT License で使用できるライブラリです。

[公式ドキュメント](https://github.com/tootsuite/documentation/blob/master/Using-the-API/Libraries.md)で Mastodon API が使用できるライブラリ一覧が紹介されています。
