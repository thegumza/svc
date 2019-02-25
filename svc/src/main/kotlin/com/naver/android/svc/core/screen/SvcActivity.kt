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

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.naver.android.svc.core.controltower.ActivityControlTowerManager
import com.naver.android.svc.core.controltower.ControlTower
import com.naver.android.svc.core.qualifiers.RequireControlTower
import com.naver.android.svc.core.utils.BundleUtils
import com.naver.android.svc.core.views.Views

/**
 * @author bs.nam@navercorp.com 2017. 6. 8..
 */

@Suppress("PrivatePropertyName")
abstract class SvcActivity<out V : Views> : AppCompatActivity(), Screen<V>, DialogPlug {

    private val CONTROLTOWER_KEY = "controlTower"
    private var CLASS_SIMPLE_NAME = javaClass.simpleName
    private val TAG: String = CLASS_SIMPLE_NAME

    val views by lazy { createViews() }
    lateinit var controlTower: ControlTower

    override val hostActivity: androidx.fragment.app.FragmentActivity?
        get() = this

    override val fragmentManagerForDialog: androidx.fragment.app.FragmentManager?
        get() = supportFragmentManager

    override val screenFragmentManager: androidx.fragment.app.FragmentManager?
        get() = supportFragmentManager

    open var statusbarColor: Int? = null

    override fun getParentFragment(): androidx.fragment.app.Fragment? {
        throw IllegalAccessError("Activity doesn't have parentFragment, If you want to use this method you should override this method and provide one")
    }

    override fun getChildFragmentManager(): androidx.fragment.app.FragmentManager {
        throw IllegalAccessError("Activity doesn't have childFragmentManager, If you want to use this method you should override this method and provide one")
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarBGColor(bgColor: Int?) {
        if (bgColor == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        window.statusBarColor = bgColor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(views.layoutResId)

        // create ControlTower
        assignControlTower(null)

        // initialize SVC
        initializeSVC(this, views, controlTower)

        val rootView: FrameLayout = window.decorView.findViewById(android.R.id.content)
        views.rootView = rootView

        setStatusBarBGColor(statusbarColor)

        super.onCreate(savedInstanceState)
        lifecycle.addObserver(views)
        lifecycle.addObserver(controlTower)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(controlTower)
        lifecycle.removeObserver(views)

        // destroy controlTower
        ActivityControlTowerManager.instance.destroy(controlTower)
    }

    override fun onBackPressed() {
        if (controlTower.onBackPressed() || views.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    /**
     * assign ControlTower
     */
    private fun assignControlTower(controlTowerBundle: Bundle?) {
        val annotation = javaClass.getAnnotation(RequireControlTower::class.java)
        annotation?.let {
            val controlTowerClass = it.value
            this.controlTower = ActivityControlTowerManager.instance.fetch(this,
                    controlTowerClass,
                    views,
                    BundleUtils.maybeGetBundle(controlTowerBundle, CONTROLTOWER_KEY))
        }
    }

    override val isActive: Boolean
        get() = !isFinishing

}
