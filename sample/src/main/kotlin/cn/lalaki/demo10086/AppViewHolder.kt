package cn.lalaki.demo10086

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.lalaki.sdk.IceBox
import java.lang.Exception
import kotlin.concurrent.thread

class AppViewHolder(
    itemView: View,
) : ViewHolder(itemView),
    OnClickListener {
    init {
        itemView.findViewById<Button>(R.id.enable_app).setOnClickListener(this)
        itemView.findViewById<Button>(R.id.disable_app).setOnClickListener(this)
    }

    private lateinit var appName: TextView
    private lateinit var packageName: String

    fun bind(applicationInfo: ApplicationInfo) {
        packageName = applicationInfo.packageName
        val packageManager = itemView.context.packageManager
        appName = itemView.findViewById(R.id.appName)
        appName.text = packageManager.getApplicationLabel(applicationInfo)
        itemView
            .findViewById<ImageView>(R.id.appIcon)
            .setImageDrawable(packageManager.getApplicationIcon(applicationInfo))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.enable_app -> {
                Toast
                    .makeText(
                        v.context,
                        v.context.getString(R.string.done) + appName.text,
                        Toast.LENGTH_SHORT,
                    ).show()
                thread {
                    IceBox.setAppEnabledSettings(v.context, true, packageName)
                    v.post {
                        try {
                            val launchIntent =
                                v.context.packageManager.getLaunchIntentForPackage(
                                    packageName,
                                )
                            if (launchIntent == null) return@post
                            v.context.startActivity(
                                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            )
                        } catch (_: Exception) {
                            Toast.makeText(v.context, R.string.err, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            R.id.disable_app -> {
                thread {
                    IceBox.setAppEnabledSettings(v.context, false, packageName)
                }
                Toast
                    .makeText(
                        v.context,
                        "${appName.text} ${v.context.getString(R.string.ok)}",
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }
}
