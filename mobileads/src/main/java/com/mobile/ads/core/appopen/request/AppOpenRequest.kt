package com.mobile.ads.core.appopen.request

import android.content.Context
import com.mobile.ads.core.IMobileAdRequest

/**
 * Created by KO Huyn on 12/03/2024.
 */
abstract class AppOpenRequest(open val context: Context, open val adUnitId: String) : IMobileAdRequest