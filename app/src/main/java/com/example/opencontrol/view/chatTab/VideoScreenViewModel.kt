package com.example.opencontrol.view.chatTab

import android.content.Context
import android.provider.UserDictionary.Words.APP_ID
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opencontrol.domain.MainRepository
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

class VideoScreenViewModel(private val repository: MainRepository) : ViewModel() {
    private val _hasAudioPermission = mutableStateOf(false)
    val hasAudioPermission: State<Boolean> = _hasAudioPermission

    private val _hasCameraPermission = mutableStateOf(false)
    val hasCameraPermission: State<Boolean> = _hasCameraPermission

    var agoraToken by mutableStateOf("")


    fun initEngine(current: Context, eventHandler: IRtcEngineEventHandler, channelName: String, userRole: String, userId: String): RtcEngine =
        RtcEngine.create(current, "efe9f81f09164d32955260ccc3143b7d", eventHandler).apply {
            enableVideo()
            setChannelProfile(1)
            if (userRole == "Broadcaster") {
                setClientRole(1)
            } else {
                setClientRole(0)
            }
            joinChannel(agoraToken, channelName, "", 0)
        }


    fun onPermissionsResult(
        acceptedAudioPermission: Boolean,
        acceptedCameraPermission: Boolean
    ) {
        _hasAudioPermission.value = acceptedAudioPermission
        _hasCameraPermission.value = acceptedCameraPermission
    }


    fun getAgoraToken(userId: String) {
        viewModelScope.launch {
            repository.getAgoraToken(userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    agoraToken = it.agoraToken
                }
        }
    }
}