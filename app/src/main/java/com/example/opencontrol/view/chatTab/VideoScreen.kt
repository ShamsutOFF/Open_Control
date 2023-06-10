package com.example.opencontrol.view.chatTab

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

const val AGORA_APP_ID = "efe9f81f09164d32955260ccc3143b7d"

data class VideoScreen(
    val roomName: String
) : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            VideoScreenContent(roomName = roomName)
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
private fun VideoScreenContent(
    roomName: String,
    //viewModel: VideoScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context =LocalContext.current
    val userId = getViewModel<MainViewModel>().userId
    val viewModel = getViewModel<VideoScreenViewModel>()
    viewModel.getAgoraToken(userId)
    var remoteUserMap by remember {
        mutableStateOf(mapOf<Int, TextureView?>())
    }
    val localSurfaceView: TextureView? by remember {
        mutableStateOf(RtcEngine.CreateTextureView(context))
    }
    val engine = viewModel.initEngine(context, object : IRtcEngineEventHandler() {
        @SuppressLint("TimberArgCount")
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            Timber.tag(TAG).d("%s%s", "%s,elapsed:", "%s%s", "%s,uid:", "channel:%s", channel, uid, elapsed)
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Timber.tag(TAG).d("onUserJoined:%s", uid)
            val desiredUserList = remoteUserMap.toMutableMap()
            desiredUserList[uid] = null
            remoteUserMap = desiredUserList.toMap()
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            Timber.tag(TAG).d("onUserOffline:%s", uid)
            val desiredUserList = remoteUserMap.toMutableMap()
            desiredUserList.remove(uid)
            remoteUserMap = desiredUserList.toMap()
        }

    }, "efe9f81f09164d32955260ccc3143b7d", "Broadcaster", userId)
    Timber.d("@@@ VideoScreenContent (roomName = $roomName)")
    val navigator = LocalNavigator.currentOrThrow
    var agoraView: AgoraVideoViewer? = null
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            viewModel.onPermissionsResult(
                acceptedAudioPermission = perms[Manifest.permission.RECORD_AUDIO] == true,
                acceptedCameraPermission = perms[Manifest.permission.CAMERA] == true
            )
        })

    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }

    BackHandler {
        agoraView?.leaveChannel()
        RtcEngine.destroy()
        navigator.pop()
    }

   /* if (viewModel.hasAudioPermission.value && viewModel.hasCameraPermission.value) {

        AndroidView(
            factory = { context ->*/
                Box(Modifier.fillMaxSize()) {
                    localSurfaceView?.let { local ->
                        AndroidView(factory = { local }, Modifier.fillMaxSize())
                    }
                    RemoteView(remoteListInfo = remoteUserMap, mEngine = engine)
                    UserControls(mEngine = engine)
                }
                /*AgoraVideoViewer(
                    context = context,
                    connectionData = AgoraConnectionData(appId = AGORA_APP_ID)
                ).also {
                    it.style = AgoraVideoViewer.Style.FLOATING
                    it.join(roomName)
                    agoraView = it
                }*/
            /*},
            modifier = Modifier.fillMaxSize()
        )
    }*/

}

@Composable
private fun RemoteView(remoteListInfo: Map<Int, TextureView?>, mEngine: RtcEngine) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.2f)
            .horizontalScroll(state = rememberScrollState())
    ) {
        remoteListInfo.forEach { entry ->
            val remoteTextureView =
                RtcEngine.CreateTextureView(context).takeIf { entry.value == null }?:entry.value
            AndroidView(
                factory = { remoteTextureView!! },
                modifier = Modifier.size(180.dp, 240.dp)
            )
            mEngine.setupRemoteVideo(
                VideoCanvas(
                    remoteTextureView,
                    Constants.RENDER_MODE_HIDDEN,
                    entry.key
                )
            )
        }
    }
}

@Composable
private fun UserControls(mEngine: RtcEngine) {
    var muted by remember { mutableStateOf(false) }
    var videoDisabled by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
        Arrangement.SpaceEvenly,
        Alignment.Bottom
    ) {
        OutlinedButton(
            onClick = {
                muted = !muted
                mEngine.muteLocalAudioStream(muted)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (muted) Color.Blue else Color.White)
        ) {
            if (muted) {
                Icon(Icons.Rounded.MicOff, contentDescription = "Tap to unmute mic", tint = Color.White)
            } else {
                Icon(Icons.Rounded.Mic, contentDescription = "Tap to mute mic", tint = Color.Blue)
            }
        }
        OutlinedButton(
            onClick = {
                mEngine.leaveChannel()
                activity?.finish()
            },
            shape = CircleShape,
            modifier = Modifier.size(70.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Red)
        ) {
            Icon(Icons.Rounded.CallEnd, contentDescription = "Tap to disconnect Call", tint = Color.White)

        }
        OutlinedButton(
            onClick = {
                videoDisabled = !videoDisabled
                mEngine.muteLocalVideoStream(videoDisabled)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (videoDisabled) Color.Blue else Color.White)
        ) {
            if (videoDisabled) {
                Icon(Icons.Rounded.VideocamOff, contentDescription = "Tap to enable Video", tint = Color.White)
            } else {
                Icon(Icons.Rounded.Videocam, contentDescription = "Tap to disable Video", tint = Color.Blue)
            }
        }
    }
}