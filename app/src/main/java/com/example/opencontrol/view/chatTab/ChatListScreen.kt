package com.example.opencontrol.view.chatTab

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.R
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

const val MY_API_KEY = "tpa2jgtu8ezw"
const val DEMO_API_KEY = "b67pax5b2wdq"
const val MY_TOKEN =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHBhMmpndHU4ZXp3In0.QwYeGO4hul_ts2EeO7NinU3h5H2RAquhhcI-Mqqw4PY"
const val DEMO_TOKEN =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"

class ChatListScreen : Screen {
    @Composable
    override fun Content() {
        ChatListScreenContent()
    }
}

@Composable
private fun ChatListScreenContent() {
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow
    // 1 - Set up the OfflinePlugin for offline storage
    val offlinePluginFactory = StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
        ),
        appContext = context,
    )

    // 2 - Set up the client for API calls and with the plugin for offline storage
    val client = ChatClient.Builder(DEMO_API_KEY, context)
        .withPlugin(offlinePluginFactory)
        .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
        .build()

    // 3 - Authenticate and connect the user
    val user = User(
        id = "tutorial-droid",
        name = "Tutorial Droid",
        image = "https://bit.ly/2TIt8NR"
    )
    client.connectUser(
        user = user,
        token = DEMO_TOKEN
    ).enqueue()

    // 4 - Set up the Channels Screen UI
    ChatTheme {
        ChannelsScreen(
            title = stringResource(id = R.string.app_name),
            isShowingSearch = true,
            onItemClick = { channel ->
                navigator.push(ChatMessagesScreen(channelId = channel.cid))
            },
            onBackPressed = {
                navigator.pop()
            }
        )
    }
}