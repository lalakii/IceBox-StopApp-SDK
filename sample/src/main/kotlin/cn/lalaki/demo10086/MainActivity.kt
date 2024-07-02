package cn.lalaki.demo10086

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.lalaki.sdk.IceBox
import kotlin.concurrent.thread

@Suppress("SetTextI18n")
class MainActivity : Activity() {
    private val requestCode = 0x233
    private val textView by lazy {
        findViewById<TextView>(R.id.info).apply {
            text = "尚未授予冰箱权限，功能无法使用。如果授权窗口无法弹出，尝试卸载重装此APP"
            textSize = 30f
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == this.requestCode) {
            IceBox.permissionIsGranted = grantResults.contains(PackageManager.PERMISSION_GRANTED)
            Log.d(packageName, "has icebox permission..")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        textView.setTextColor(Color.RED)
        // 检查权限 这里演示冰箱的相关功能。如果需要小黑屋，只需要将IceBox改为StopApp
        IceBox.permissionIsGranted =
            checkSelfPermission(IceBox.SDK_PERMISSION) == PackageManager.PERMISSION_GRANTED
        if (!IceBox.permissionIsGranted) {
            requestPermissions(
                arrayOf(IceBox.SDK_PERMISSION),
                requestCode,
            )
            Log.d(packageName, "request icebox permission.")
        } else {
            test()
        }
        val installedApps =
            packageManager.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES)
        val adapter =
            AppListAdapter(
                installedApps
                    .filter { it.sourceDir.startsWith("/data") }
                    .toMutableList(),
            )
        val recyclerView = findViewById<RecyclerView>(R.id.apps)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (IceBox.permissionIsGranted) {
            test()
            textView.text = "已取得冰箱权限"
        }
    }

    @Suppress("InlinedApi")
    private fun test() {
        thread {
            val packageNames = arrayOf("org.example.app1", "org.example.app2", "org.example.appN")
            /**
             *  冻结或解冻应用，指定需要冻结App的包名，放在线程中执行
             *  修改 enable = true 冻结，enable = false 解冻
             */
            IceBox.setAppEnabledSettings(applicationContext, true, *packageNames)
        }
        /**
         * 获取应用状态，判断以何种方式冻结（pm disable-user or pm hide）,
         * 如果应用不存在，会引发异常，一般需要加上 try ... catch
         **/
        val state = IceBox.getAppEnabledSetting(applicationInfo)
        when (state) {
            IceBox.FLAG_PM_DISABLE_USER -> {
                Log.d(packageName, "disable-user.")
            }

            IceBox.FLAG_PM_HIDE -> {
                Log.d(packageName, "pm hide.")
            }

            else -> {
                Log.d(packageName, "normal.")
            }
        }
        // 冰箱App的包名
        val iceBoxPackageName = IceBox.PACKAGE_NAME
        Toast.makeText(this, iceBoxPackageName, Toast.LENGTH_SHORT).show()
        textView.setTextColor(Color.BLUE)
    }
}
