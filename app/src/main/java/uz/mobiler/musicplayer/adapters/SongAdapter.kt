package uz.mobiler.musicplayer.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.databinding.SongItemBinding
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.utils.ConstValues.TAG

class SongAdapter(
    private val songList: List<Song>,
    private val playSong: (Song) -> Unit,
    private val onShareClick: (Song) -> Unit,
    private val onMoreClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(private val binding: SongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(song: Song) {
            binding.apply {
//                song.albumArtUri?.let {
//                    try {
//                        val bitmap = BitmapFactory.decodeStream(binding.root.context.contentResolver.openInputStream(Uri.parse(it)))
//                        binding.songImage.setImageBitmap(bitmap)
//                    } catch (e: Exception) {
//                        binding.songImage.setImageResource(R.drawable.ic_music)
//                    }
//                }
                songTitleTv.isSelected = true
                artistAlbumNameTv.isSelected = true
                songTitleTv.text = song.title
                artistAlbumNameTv.text = "${song.artist}|${song.album}"
                root.setOnClickListener {
                    playSong(song)
                }
                shareIv.setOnClickListener {
                    onShareClick(song)
                }
                moreIv.setOnClickListener {
                    onMoreClick(song)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SongViewHolder(
        SongItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = songList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songList[position])
    }

}