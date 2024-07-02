package cn.lalaki.sdk

import android.app.PendingIntent
import android.content.pm.ApplicationInfo

/**
 * IceBox SDK
 */
object IceBox : BaseSdk() {
    override var requestCode = 0x2333
    override var permissionIsGranted = false
    const val PACKAGE_NAME = "com.catchingnow.icebox"
    const val SDK_PERMISSION = "$PACKAGE_NAME.SDK"
    override var content = "content://$SDK_PERMISSION"
    const val FLAG_PM_DISABLE_USER = 1
    const val FLAG_PM_HIDE = 2
    private const val PRIVATE_FLAG_HIDDEN = 1
    private const val FLAG_HIDDEN = 1 shl 27
    private var authorizePendingIntent: PendingIntent? = null

    @Suppress("DiscouragedPrivateApi", "PrivateApi")
    private val mField by lazy {
        ApplicationInfo::class.java.getDeclaredField("privateFlags").apply { isAccessible = true }
    }

    /**
     * 获取App状态
     * @param info ApplicationInfo
     * @return 返回值  IceBox.FLAG_PM_HIDE or IceBox.FLAG_PM_DISABLE_USER,
     *                其他返回值一般表示应用当前是正常状态
     */
    @AppState
    public override fun getAppEnabledSetting(info: ApplicationInfo): Int {
        @AppState var flag = 0
        if (isAppHidden(info)) flag += FLAG_PM_HIDE
        if (!info.enabled) flag += FLAG_PM_DISABLE_USER
        return flag
    }

    /**
     * 冻结解冻 App
     * PS: 冰箱并不是所有的引擎都支持多用户，所以暂时禁用掉多用户功能。
     *
     * @param context      context
     * @param packageNames 包名
     * @param enable       true for 解冻，false for 冻结
     */
    @androidx.annotation.WorkerThread
    @androidx.annotation.RequiresPermission(SDK_PERMISSION)
    public override fun setAppEnabledSettings(
        context: android.content.Context,
        enable: Boolean,
        vararg packageNames: String,
    ) {
        if (authorizePendingIntent == null) {
            authorizePendingIntent =
                androidx.core.app.PendingIntentCompat.getBroadcast(
                    context,
                    requestCode,
                    android.content.Intent(),
                    PendingIntent.FLAG_CANCEL_CURRENT,
                    false,
                )
        }
        call(
            context,
            "set_enable",
            androidx.core.os.bundleOf(
                "enable" to enable,
                "authorize" to authorizePendingIntent,
                "package_names" to packageNames,
                "user_handle" to
                    android.os.Process
                        .myUserHandle()
                        .hashCode(),
            ),
        )
    }

    private fun isAppHidden(info: ApplicationInfo) =
        if (mField.isAccessible) {
            try {
                val flags = mField.getInt(info)
                flags or PRIVATE_FLAG_HIDDEN == flags
            } catch (_: Throwable) {
                info.flags or FLAG_HIDDEN == info.flags
            }
        } else {
            info.flags or FLAG_HIDDEN == info.flags
        }
}
