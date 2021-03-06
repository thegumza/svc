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
package com.naver.android.svc.recyclerview.screen

import androidx.recyclerview.widget.RecyclerView
import com.naver.android.svc.core.screen.Screen

/**
 * @author bs.nam@navercorp.com 2017. 8. 16..
 */

abstract class ScreenBaseAdapter<VH : RecyclerView.ViewHolder>(val screen: Screen) : RecyclerView.Adapter<VH>() {
    final override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.adapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        onBindHolder(holder, holder.adapterPosition)
    }

    abstract fun onBindHolder(holder: VH, adapterPosition: Int)
}
