package com.iitism.srijan24.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R
import com.iitism.srijan24.data.Announcement
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AnnouncementsRVAdapter(
    private val context: Context,
    private val onTimestampUpdateListener: OnTimestampUpdateListener
) : RecyclerView.Adapter<AnnouncementsRVAdapter.AnnouncementsViewHolder>() {

    interface OnTimestampUpdateListener {
        fun onUpdateTimestamp(position: Int, timeAgo: String)
    }

    private var announcements = emptyList<Announcement>()

    inner class AnnouncementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvBody: TextView = itemView.findViewById(R.id.tvBody)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }

    fun refreshAnnouncements(newAnnouncements: List<Announcement>) {
        setAnnouncements(newAnnouncements)
    }

    fun updateTimestamps() {
        for (i in announcements.indices) {
            val currentAnnouncement = announcements[i]
            val localTimestamp = convertUtcToLocal(currentAnnouncement.timestamp)
            val timeAgo = getTimeAgo(localTimestamp)

            // Notify the listener to update timestamp
            onTimestampUpdateListener.onUpdateTimestamp(i, timeAgo)
        }
    }

    private fun setAnnouncements(newAnnouncements: List<Announcement>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = announcements.size
            override fun getNewListSize(): Int = newAnnouncements.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return announcements[oldItemPosition].id == newAnnouncements[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return announcements[oldItemPosition] == newAnnouncements[newItemPosition]
            }
        })
        announcements = newAnnouncements
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_announcement, parent, false)
        return AnnouncementsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun onBindViewHolder(holder: AnnouncementsViewHolder, position: Int) {
        val currentAnnouncement = announcements[position]
        holder.tvTitle.text = currentAnnouncement.title
        holder.tvBody.text = currentAnnouncement.body

        val localTimestamp = convertUtcToLocal(currentAnnouncement.timestamp)
        val timeAgo = getTimeAgo(localTimestamp)
        holder.tvTime.text = timeAgo
    }

    private fun convertUtcToLocal(utcTimestamp: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val utcDate = try {
            dateFormat.parse(utcTimestamp)
        } catch (e: Exception) {
            return utcTimestamp
        }

        val localDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        localDateFormat.timeZone = TimeZone.getDefault()

        return localDateFormat.format(utcDate)
    }

    private fun getTimeAgo(localTimestamp: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val localDate = try {
            dateFormat.parse(localTimestamp)
        } catch (e: Exception) {
            return ""
        }

        val now = System.currentTimeMillis()
        val timeDifference = now - localDate.time

        val seconds = timeDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days.toInt() == 1 -> "1 day ago"
            days > 1 -> "${days.toInt()} days ago"
            hours.toInt() == 1 -> "1 hour ago"
            hours > 1 -> "${hours.toInt()} hours ago"
            minutes.toInt() == 1 -> "1 minute ago"
            minutes > 1 -> "${minutes.toInt()} minutes ago"
            else -> "Less than a minute ago"
        }
    }
}
