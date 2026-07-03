package com.fishing.android

import androidx.lifecycle.ViewModel
import com.fishing.android.screens.FishData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FishingViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(FishingGameState())
    val gameState = _gameState.asStateFlow()

    private fun updateGameState(newState: FishingProcessState) {
        _gameState.update { currentState ->
            currentState.copy(currentFishingProcessState = newState)
        }
    }

    fun updateCastDistance(distance: Float) {
        _gameState.update { currentState ->
            currentState.copy(currentCastDistance = distance)
        }
    }

    fun updateFishingProcessState(state: FishingProcessState) {
        _gameState.update { currentState ->
            currentState.copy(currentFishingProcessState = state)
        }
    }

    fun updateFishList(fishList: List<FishData>) {
        _gameState.update { currentState ->
            currentState.copy(fishList = fishList.toMutableList())
        }
    }

    fun setJustCaughtFishMessage(message: String?) {
        _gameState.update { currentState ->
            currentState.copy(justCaughtFishMessage = message)
        }
    }

    fun handleStateTransition() {
        when (gameState.value.currentFishingProcessState) {
            FishingProcessState.IDLE -> handleIdleState()
            FishingProcessState.CASTING -> handleCastingState()
            FishingProcessState.LINE_IN_WATER -> handleLuringState()
            FishingProcessState.REELING -> handleReelingState()
            else -> {} // Handle other states
        }
    }

    private fun handleIdleState() {
        // Logic for handling the IDLE state
        updateGameState(FishingProcessState.CASTING)
    }

    private fun handleCastingState() {
        // Logic for handling the CASTING state
        updateGameState(FishingProcessState.LINE_IN_WATER)
    }

    private fun handleLuringState() {
        // Logic for handling the LINE_IN_WATER state
        updateGameState(FishingProcessState.REELING)
    }

    private fun handleReelingState() {
        // Logic for handling the REELING state
        updateGameState(FishingProcessState.IDLE)
    }
}