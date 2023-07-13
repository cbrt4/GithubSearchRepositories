package inc.alex.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<Intent, State> : ViewModel() {

    abstract val state: StateFlow<State>
    open val message = MutableSharedFlow<Any>()

    abstract fun dispatchIntent(intent: Intent)

    fun collectState(collector: (State) -> Unit) {
        viewModelScope.launch {
            state.collect(collector)
        }
    }

    fun collectMessage(collector: (message: Any?) -> Unit) {
        viewModelScope.launch {
            message.collect(collector)
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}
