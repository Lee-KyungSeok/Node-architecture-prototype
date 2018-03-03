package com.levnovikov.system_base

import android.util.Log
import com.levnovikov.system_base.back_handling.BackHandler
import com.levnovikov.system_base.exceptions.NodeAlreadyAttachedException
import com.levnovikov.system_base.exceptions.RouterAlreadyAttachedException
import com.levnovikov.system_base.node_state.NodeState
import java.util.*

/**
 * Author: lev.novikov
 * Date: 20/11/17.
 */

abstract class Router {

    private val children = HashMap<Class<out Router>, Router>()

    private var stateDataProvider: StateDataProvider? = null //TODO set to null on node destroy

    private var backHandler: BackHandler? = null

    private fun getChildrenState(): MutableMap<String, NodeState> {
        val stateMap = HashMap<String, NodeState>()
        for (router in children.values) {
            stateMap.putAll(router.getState())
        }
        return stateMap
    }

    fun getState(): Map<String, NodeState> {
        val state = getChildrenState()
        val stateData = stateDataProvider?.run { this.onSaveData() }
        state[this.javaClass.simpleName] = NodeState(stateData, nodes())
        return state
    }

    fun setStateDataProvider(provider: StateDataProvider) {
        stateDataProvider = provider
    }

    fun setBackHandler(handler: BackHandler) {
        backHandler = handler
    }

    protected fun attachNode(nodeHolder: NodeHolder<*>) {
        try {
            attachRouter(nodeHolder.build())
        } catch (e: NodeAlreadyAttachedException) {
            Log.i(">>>>", e.message)
        }
    }

    protected fun detachNode(nodeHolder: NodeHolder<out Router>) {
        nodeHolder.router?.let {
            detachRouter(it.javaClass)
            nodeHolder.destroy()
        }
    }

    private fun attachRouter(router: Router) {
        Log.i(">>>>", "attachRouter " + router.javaClass.simpleName + " from " +
                this.javaClass.simpleName)
        if (children.containsKey(router.javaClass)) {
            throw RouterAlreadyAttachedException(router)
        }
        children[router.javaClass] = router
    }

    private fun detachRouter(router: Class<out Router>) {
        Log.i(">>>>", "detachRouter " + router.javaClass.simpleName + " from " +
                this.javaClass.simpleName)
        children.remove(router)
    }

    protected fun detachChildren() {
        Log.i(">>>>", "detachChildren " + this.javaClass.simpleName)
        children.clear()
    }

    fun destroyNode() {
        holders.forEach(::detachNode)
        detachChildren()
    }

    abstract val holders: Set<NodeHolder<*>>

    fun nodes(): Set<String> = holders
            .filter(NodeHolder<*>::isActive)
            .map { it::class.java.simpleName }
            .toSet()

    fun setState(state: NodeState) = holders
            .filter { state.contains(it.javaClass) }
            .forEach(::attachNode)

    fun onBackPressed(): Boolean {
        for (router in children.values) {
            if (router.onBackPressed()) {
                return true
            }
        }
        backHandler?.let {
            if (it.isLastInStack(this.javaClass)) {
                it.popLastInStack()
                return it.onBackPressed()
            }
        }
        return false
    }

    fun removeFromBackStack() {
        backHandler?.removeFromBackStack(this.javaClass)
    }
}
