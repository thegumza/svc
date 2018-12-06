/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naver.android.svc.core.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.naver.android.svc.BuildConfig
import com.naver.android.svc.core.controltower.ControlTower
import com.naver.android.svc.core.views.Views

/**
 * @author bs.nam@navercorp.com 2017. 6. 8..
 */

abstract class SvcFragment<out V : Views, out C : ControlTower<*, *>> : Fragment(),
        Screen<V, C>,
        DialogPlug,
        StatusbarChanger {

    val CLASS_SIMPLE_NAME = javaClass.simpleName
    var TAG: String = CLASS_SIMPLE_NAME

    companion object {
        const val EXTRA_TAG_ID = BuildConfig.APPLICATION_ID + ".EXTRA_TAG_ID"
    }

    val views by lazy { createViews() }
    val controlTower by lazy { createControlTower() }

    override val hostActivity: FragmentActivity?
        get() = activity

    override val fragmentManagerForDialog: FragmentManager?
        get() = this.hostActivity?.supportFragmentManager

    override val screenFragmentManager: FragmentManager?
        get() = this.fragmentManager

    override var statusbarColor: Int? = null
        set(value) {
            field = value
            setStatusBarBGColor(value)
        }

    override fun getWindow(): Window {
        return activity!!.window
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        addExtraTagId()
        super.onCreate(savedInstanceState)
    }

    private fun addExtraTagId() {
        val extraId = arguments?.getString(EXTRA_TAG_ID)
        extraId?.apply {
            controlTower.TAG = "${controlTower.CLASS_SIMPLE_NAME}_$this"
            views.TAG = "${views.CLASS_SIMPLE_NAME}_$this"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (views.isInitialized) views.rootView
        else inflater.inflate(views.layoutResId, container, false) as ViewGroup?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeSVC(this, views, controlTower)

        if (!views.isInitialized) {
            views.rootView = view as ViewGroup
        }

        lifecycle.addObserver(views)
        lifecycle.addObserver(controlTower)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(controlTower)
        lifecycle.removeObserver(views)
    }

    open fun onBackPressed(): Boolean {
        if (controlTower.onBackPressed() || views.onBackPressed()) {
            return true
        }
        return false
    }

    override val isActive: Boolean
        get() = hostActivity != null && context != null && isAdded && !isRemoving && !isDetached
}
