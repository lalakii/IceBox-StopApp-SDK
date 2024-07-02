package cn.lalaki.sdk

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IntDef

abstract class BaseSdk {
    abstract var content: String
    abstract var requestCode: Int
    abstract var permissionIsGranted: Boolean
    private val uri by lazy { Uri.parse(content) }

    protected abstract fun setAppEnabledSettings(
        context: Context,
        enable: Boolean,
        vararg packageNames: String,
    )

    protected abstract fun getAppEnabledSetting(info: ApplicationInfo): Int

    @IntDef(flag = true, value = [IceBox.FLAG_PM_HIDE, IceBox.FLAG_PM_DISABLE_USER])
    protected annotation class AppState

    protected fun call(
        context: Context,
        method: String,
        bundle: Bundle,
    ) {
        context.contentResolver.call(uri, method, null, bundle)
    }
}
