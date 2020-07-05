### ChannelList

The ChannelListView shows a list of channel previews.
Typically it will show an unread/read state, the last message and who is participating in the conversation.

The easiest way to render a ChannelList is to add it to your layout:

```xml
<com.getstream.sdk.chat.view.ChannelListView
    android:id="@+id/channelList"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_marginBottom="10dp"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent" />
```

And in activity do something like this:

```java
package io.getstream.chat.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import io.getstream.chat.example.databinding.ActivityMainBinding;
import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.enums.FilterObject;
import com.getstream.sdk.chat.rest.User;
import com.getstream.sdk.chat.rest.core.Client;
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel;

import java.util.HashMap;

import static com.getstream.sdk.chat.enums.Filters.in;

/**
 * This activity shows a list of channels
 */
public class MainActivity extends AppCompatActivity {

    final String USER_ID = "broken-waterfall-5";
    // User token is typically provided by your server when the user authenticates
    final String USER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiYnJva2VuLXdhdGVyZmFsbC01In0.d1xKTlD_D0G-VsBoDBNbaLjO-2XWNA8rlTm4ru4sMHg";
    private ChannelListViewModel viewModel;

    // establish a websocket connection to stream
    protected Client configureStreamClient() {
        Client client = StreamChat.getInstance(this.getApplication());
        HashMap<String, Object> extraData = new HashMap<>();
        extraData.put("name", "Broken waterfall");
        extraData.put("image", "https://bit.ly/2u9Vc0r");
        User user = new User(USER_ID, extraData);
        client.setUser(user, USER_TOKEN);

        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the client using the example API key
        StreamChat.init("qk4nn7rpcn75", this.getApplicationContext());

        // setup the client
        Client client = configureStreamClient();

        // we're using data binding in this example
        ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // most the business logic for chat is handled in the ChannelListViewModel view model
        viewModel = ViewModelProviders.of(this).get(ChannelListViewModel.class);
        // set the viewModel data for the activity_main.xml layout
        binding.setViewModel(viewModel);
        binding.channelList.setViewModel(viewModel, this);

        // query all channels where the current user is a member
        // FilterObject filter = in("members", USER_ID);
        FilterObject filter = in("type", "messaging");
        viewModel.setChannelFilter(filter);

        // setup an onclick listener to capture clicks to the user profile or channel
        binding.channelList.setOnChannelClickListener(channel -> {
            // open the channel activity
        });
        binding.channelList.setOnUserClickListener(user -> {
            // TODO: open your user profile
        });
    }
}
```

#### View Model Event handling

The Channel List view model keeps channels in sync using event delegation. 

Events that updates channels:

- `message.new`
- `message.updated`
- `message.deleted`
- `channel.updated`
- `message.mark_read`
- `member.added`
- `member.updated`
- `member.removed`

Events that add new channels:

- `notification.message_new`
- `notification.added_to_channel`

Events that remove channels:

- `notification.removed_from_channel`
- `channel.deleted`

##### Customizing event handling

In some cases you might want to disable the built-in behavior such as automatically add a new channel to the list.

Here's an example on how to ignore new channels when `NOTIFICATION_MESSAGE_NEW` has a message from a muted user

```java
viewModel.setEventInterceptor((event, channel) -> {
    if (event.getType() == EventType.NOTIFICATION_MESSAGE_NEW && event.getMessage() != null) {
        return client.getUser().hasMuted(event.getMessage().getUser());
    }
    return false;
});
```

#### Listeners

The following listeners can be set

* setOnChannelClickListener
* setOnLongClickListener
* setOnUserClickListener

#### Styling using Attributes

The following attributes are available:

You must use the following properties in your XML to change your ChannelListView.

- **AvatarView**

| Properties                         | Type                  | Default          |
| ---------------------------------- | --------------------- | ---------------- |
| `app:streamAvatarWidth`            | dimension             | 40dp             |
| `app:streamAvatarHeight`           | dimension             | 40dp             |
| `app:streamAvatarBorderWidth`      | dimension             | 3dp              |
| `app:streamAvatarBorderColor`      | color                 | WHITE            |
| `app:streamAvatarBackGroundColor`  | color                 | stream_gray_dark |
| `app:streamAvatarTextSize`         | dimension             | 14sp             |
| `app:streamAvatarTextColor`        | color                 | WHITE            |
| `app:streamAvatarTextStyle`        | normal, bold, italic  | bold             |
| `app:streamAvatarTextFont`         | reference             | -                |
| `app:streamAvatarTextFontAssets`   | string                | -                |

- **ReadStateView**

| Properties                          | Type                  | Default |
| ----------------------------------- | --------------------- | ------- |
| `app:streamShowReadState`           | boolean               | true    |
| `app:streamReadStateAvatarWidth`    | dimension             | 14dp    |
| `app:streamReadStateAvatarHeight`   | dimension             | 14dp    |
| `app:streamReadStateTextSize`       | dimension             | 8sp     |
| `app:streamReadStateTextColor`      | color                 | BLACK   |
| `app:streamReadStateTextStyle`      | normal, bold, italic  | bold    |
| `app:streamReadStateTextFont`       | reference             | -       |
| `app:streamReadStateTextFontAssets` | string                | -       |

- **Channel Title**

| Properties                                 | Type                  | Default              |
| ------------------------------------------ | --------------------- | -------------------- |
| `app:streamChannelTitleTextSize`           | dimension             | 15sp                 |
| `app:streamChannelTitleTextColor`          | color                 | BLACK                |
| `app:streamChannelTitleUnreadTextColor`    | color                 | BLACK                |
| `app:streamChannelTitleTextStyle`          | normal, bold, italic  | bold                 |
| `app:streamChannelTitleUnreadTextStyle`    | normal, bold, italic  | bold                 |
| `app:streamChannelWithOutNameTitleText`    | string                | Channel without name |

- **Last Message**

| Properties                              | Type                  | Default          |
| --------------------------------------- | --------------------- | ---------------- |
| `app:streamLastMessageTextSize`         | dimension             | 13sp             |
| `app:streamLastMessageTextColor`        | color                 | stream_gray_dark |
| `app:streamLastMessageUnreadTextColor`  | color                 | BLACK            |
| `app:streamLastMessageTextStyle`        | normal, bold, italic  | normal           |
| `app:streamLastMessageUnreadTextStyle`  | normal, bold, italic  | bold             |
| `app:streamLastMessageTextFont`         | reference             | -                |
| `app:streamLastMessageTextFontAssets`   | string                | -                |

- **Last Message Date**

| Properties                                 | Type                  | Default          |
| ------------------------------------------ | --------------------- | ---------------- |
| `app:streamLastMessageDateTextSize`        | dimension             | 11sp             |
| `app:streamLastMessageDateTextColor`       | color                 | stream_gray_dark |
| `app:streamLastMessageDateUnreadTextColor` | color                 | BLACK            |
| `app:streamLastMessageDateTextStyle`       | normal, bold, italic  | normal           |
| `app:streamLastMessageDateUnreadTextStyle` | normal, bold, italic  | bold             |
| `app:streamLastMessageDateTextFont`        | reference             | -                |
| `app:streamLastMessageDateTextFontAssets`  | string                | -                |

- **Custom Layout**

| Properties                          | Type                  | Default |
| ----------------------------------- | --------------------- | ------- |
| `app:streamChannelPreviewLayout`    | reference             |  _      |


#### Changing the layout

If you need to make a bigger change you can swap the layout for the channel previews.

```xml
<com.getstream.sdk.chat.view.ChannelListView
    android:id="@+id/channelList"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_marginBottom="10dp"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:streamChannelPreviewLayout="@layout/list_item_channel_custom" />
```

That only works for simple changes where you don't change the IDs of views, or their types.
You can find the default layout in **stream_item_channel.xml**

#### Custom Viewholder

If you need full control over the styling for the channel preview you can overwrite the view holder using a view holder factory. Here is an example view holder factory:

```kotlin
/*
Make the view holder factory return CustomChannelListItemViewHolder
 */
class CustomViewHolderFactory : ChannelViewHolderFactory() {
    override fun createChannelViewHolder(
        adapter: ChannelListItemAdapter,
        parent: ViewGroup,
        viewType: Int
    ): BaseChannelListItemViewHolder? { // get the style object
        val style = adapter.style
        // inflate the layout specified in the style
        val v = LayoutInflater.from(parent.context)
            .inflate(style.channelPreviewLayout, parent, false)
        // configure the viewholder
        val holder = CustomChannelListItemViewHolder(v)
        configureHolder(holder, adapter)
        // return..
        return holder
    }
}
```

And this is how you can tell the ChannelList to use your view holder factory

```kotlin
val  adapter =  ChannelListItemAdapter(activity)
adapter.setViewHolderFactory(CustomViewHolderFactory())
binding.channelList.setViewModel(viewModel, this, adapter)
```

You'll typically want to extend either the `ChannelListItemViewHolder` or the `BaseChannelListItemViewHolder` class.

#### Client usage

Alternatively you can of course build your own ChannelListView using the low level client.
