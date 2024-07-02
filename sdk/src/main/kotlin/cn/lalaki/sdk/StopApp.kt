package cn.lalaki.sdk

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.os.bundleOf

object StopApp : BaseSdk() {
    override var requestCode = 0x2335
    override var permissionIsGranted = false
    const val PACKAGE_NAME = "web1n.stopapp"
    const val SDK_PERMISSION = "$PACKAGE_NAME.permission.disable_api"
    override var content = "content://$PACKAGE_NAME.DISABLE_SDK"

    @androidx.annotation.WorkerThread
    @androidx.annotation.RequiresPermission(SDK_PERMISSION)
    public override fun setAppEnabledSettings(
        context: Context,
        enable: Boolean,
        vararg packageNames: String,
    ) = call(
        context,
        StopApp::class.java.toString(),
        bundleOf(
            "packages" to packageNames,
            "enable" to enable,
        ),
    )

    override fun getAppEnabledSetting(info: ApplicationInfo) = IceBox.getAppEnabledSetting(info)
}
