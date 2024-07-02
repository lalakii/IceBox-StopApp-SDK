package cn.lalaki.demo10086

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AppListAdapter(
    private val list: MutableList<ApplicationInfo>,
) : RecyclerView.Adapter<AppViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AppViewHolder =
        AppViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false),
        )

    override fun onBindViewHolder(
        holder: AppViewHolder,
        position: Int,
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
