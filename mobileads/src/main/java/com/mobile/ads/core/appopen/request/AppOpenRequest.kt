package com.mobile.ads.core.appopen.request

import android.content.Context
import com.mobile.ads.core.IMobileAdRequest

/**
 * Created by KO Huyn on 12/03/2024.
 */
abstract class AppOpenRequest(override val context: Context, override val adUnitId: String) : IMobileAdRequest