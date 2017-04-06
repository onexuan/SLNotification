# SLNotification
- [x] [Android权限大全](./app/markdown/Android权限大全.md)


深入学习Notification
>* 原文链接 : [Notifications in Android N](https://android-developers.blogspot.hk/2016/06/notifications-in-android-n.html)
* 原文作者 : Ian Lake
* 译文出自 : [掘金翻译计划](https://github.com/xitu/gold-miner)
* 译者 :[DeadLion](https://github.com/DeadLion)
* 校对者:[danke77](https://github.com/danke77), [xcc3641](https://github.com/xcc3641)

# 来瞧瞧 Android N 中的通知长成啥样了

Android 通知往往是应用和用户之间至关重要的交互形式。为了提供更好的用户体验，Android N 在通知上做出了诸多改进：收到消息后的视觉刷新，改进对自定义视图的支持，扩展了更加实用的直接回复消息的形式，新的 `MessagingStyle`，捆绑的通知。

### 同样的通知，不一样的“面貌”

首先，最明显的变化是通知的默认外观已经显著改变。除了应用程序的图标和名称会固定在通知内，很多分散在通知周围的字段也被折叠进新的标题行内。这一改变是为了确保尽可能腾出更多空间给标题、文本和大图标，这样一来通知就比现在的稍大些，更加易读。

![](http://ww3.sinaimg.cn/large/a490147fgw1f4w3pakcdrj20hs0853zv.jpg)

给出单标题行，这就比以往的信息更加重要且更有用。**当指定 Android N 时，默认情况下，时间会被隐藏** - 对时间敏感的通知（比如消息类应用），可以 `setShowWhen(true)` 设置重新启用显示时间。此外，现在 subtext 会取代内容消息和数量的作用：数量是绝不会在 Android N 设备上出现的，除非指定之前的 Android 版本，而且不包含任何 subtext，内容消息将会显示。在所有情况下，都要确保 subtext 是相关且有意义的。例如，如果用户只有一个账号，就不要再添加邮箱账户作为 subtext 了。

通知收到后的操作也重新设计了，现在视觉上是在通知下方单独的一栏中。

![](http://ww4.sinaimg.cn/large/a490147fgw1f4w3pwyytkj20b203vdfw.jpg)

你会注意到，图标都没有出现在新的通知中；取而代之的是，将通知内有限的空间提供给了标签本身。然而，在旧版本的 Android 和设备上，通知操作图标仍然需要且被继续使用，如 Android Wear 。

如果你使用 [NotificationCompat.Builder](https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html?utm_campaign=android_series_notificationsandroidnblog_060816&utm_source=anddev&utm_medium=blog) 创建了自己的通知，那么可以使用标准样式，无需修改任何代码就能变成默认的新样子。

### 更好的支持自定义视图


如果要从自定义 `RemoteViews` 创建自己的通知，以适应任何新的样式一直以来都很具有挑战性。随着新的 header，扩展行为，操作，和大图标位置都作为元素从通知的主要内容标题中分离出来，我们已经介绍一种新的 `DecoratedCustomViewStyle` 和 `DecoratedMediaCustomViewStyle` 提供所有这些元素使用， 这样就能使用新的 `setCustomContentView()` 方法，专注于内容部分。

![](http://ww4.sinaimg.cn/large/a490147fjw1f4w3qquphlj209p03hglr.jpg)


这也确保未来外观改变了，也能轻易的随着平台更新，适配这些样式，还无需修改 app 端的代码。

### 直接回复


虽然通知是可以用来启动一个 `Activity`，或以一个 `Service` 、`BroadcastReceiver` 的方式在后台工作，**直接回复** 允许你使用通知操作直接在内嵌输入框中回复。

![](http://ww2.sinaimg.cn/large/a490147fjw1f4w3r9gdt2j207l02pt8n.jpg)

直接回复使用相同的 [RemoteInput](https://developer.android.com/reference/android/support/v4/app/RemoteInput.html?utm_campaign=android_series_notificationsandroidnblog_060816&utm_source=anddev&utm_medium=blog) API，最初是为 Android Wear 某个 [Action](https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Action.html?utm_campaign=android_series_notificationsandroidnblog_060816&utm_source=anddev&utm_medium=blog)用的，为了能直接接收用户的输入。

`RemoteInput` 本身包含信息，如将用于以后恢复输入的秘钥，在用户开始输入之前的提示信息。

<pre>// Where should direct replies be put in the intent bundle (can be any string)
private static final String KEY_TEXT_REPLY = "key_text_reply";

// Create the RemoteInput specifying this key
String replyLabel = getString(R.string.reply_label);
RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
        .setLabel(replyLabel)
        .build();
