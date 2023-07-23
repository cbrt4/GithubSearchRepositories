package inc.alex.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<I : MviIntent, S : MviState>(initialState: S) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(initialState)
    private val stateFlow: StateFlow<S> = mutableStateFlow.asStateFlow()

    private val mutableEventFlow = MutableSharedFlow<Any>()
    private val eventFlow: SharedFlow<Any> = mutableEventFlow.asSharedFlow()

    val state: S get() = stateFlow.value

    abstract fun dispatchIntent(intent: I)

    fun setState(reducer: S.() -> S) = mutableStateFlow.update(reducer)

    fun sendEvent(event: Any) {
        viewModelScope.launch { mutableEventFlow.emit(event) }
    }

    fun collectState(collector: (S) -> Unit) {
        viewModelScope.launch { stateFlow.collect(collector) }
    }

    fun collectEvent(collector: (Any?) -> Unit) {
        viewModelScope.launch { eventFlow.collect(collector) }
    }
}
