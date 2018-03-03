package com.levnovikov.system_base

import com.levnovikov.system_base.node_state.ActivityState

abstract class StateInteractor<R : Router>(router: R, activityState: ActivityState) : Interactor<R>(router, activityState), StateDataProvider {

    init {
        /*
         * If interactor need to store data after Activity recreation, we can set data source to router.
         * Router will get and save data when Activity will call onSaveInstanceState.
         */
        @Suppress("LeakingThis")
        router.setStateDataProvider(this)
    }

    override fun restoreState() {
        val state = nodeState
        if (state != null) {
            router.setState(state)
        }
    }
}
