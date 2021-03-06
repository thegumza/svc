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
package com.naver.android.svc.sample.dialog.simple

import com.naver.android.annotation.RequireControlTower
import com.naver.android.annotation.RequireListener
import com.naver.android.annotation.RequireViews
import com.naver.android.annotation.SvcDialogFragment

/**
 * @author bs.nam@navercorp.com
 */
@SvcDialogFragment
@RequireViews(SimpleViews::class)
@RequireControlTower(SimpleControlTower::class)
@RequireListener(Unit::class)
class SimpleDialog : SVC_SimpleDialog() {

    companion object {
        fun newInstance(): SimpleDialog {
            val dialog = SimpleDialog()
            dialog.dialogListener = Unit
            return dialog
        }
    }
}
