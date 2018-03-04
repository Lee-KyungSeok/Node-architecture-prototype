package com.levnovikov.postbus.root.home

import com.levnovikov.feature_map.MapNodeHolder
import com.levnovikov.postbus.root.home.allocating.AllocatingNodeHolder
import com.levnovikov.postbus.root.home.di.HomeScope
import com.levnovikov.postbus.root.home.prebooking.PrebookingNodeHolder
import com.levnovikov.system_base.NodeHolder
import com.levnovikov.system_base.Router
import javax.inject.Inject

/**
 * Author: lev.novikov
 * Date: 14/12/17.
 */

@HomeScope
class HomeRouter @Inject
constructor(
        private val prebookingHolder: PrebookingNodeHolder,
        private val allocatingHolder: AllocatingNodeHolder,
        private val mapHolder: MapNodeHolder) : Router() {

    fun startPrebooking() {
        detachNode(allocatingHolder)
        attachNode(prebookingHolder)
    }

    fun startAllocating() {
        detachNode(prebookingHolder)
        attachNode(allocatingHolder)
    }

    fun loadMap() {
        attachNode(mapHolder)
    }

    fun startTracking() {
        detachNode(allocatingHolder)
        detachNode(prebookingHolder)
    }

    override val holders: Set<NodeHolder<*>> = setOf(mapHolder, prebookingHolder, allocatingHolder)

}
