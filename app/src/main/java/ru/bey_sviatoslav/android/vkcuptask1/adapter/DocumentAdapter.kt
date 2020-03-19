package ru.bey_sviatoslav.android.vkcuptask1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.bey_sviatoslav.android.vkcuptask1.MainActivity
import ru.bey_sviatoslav.android.vkcuptask1.R
import ru.bey_sviatoslav.android.vkcuptask1.extra.getSize
import ru.bey_sviatoslav.android.vkcuptask1.extra.toDate
import ru.bey_sviatoslav.android.vkcuptask1.models.VKDocument


//Унаследуем наш класс DocumentAdapter от класса RecyclerView.Adapter\\
//Тут же указываем наш собственный ViewHolder, который предоставит нам доступ к view компонентам
class DocumentAdapter(private val activity: MainActivity, private val documents : ArrayList<VKDocument>) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.document_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = documents.size

    override fun onBindViewHolder(holder: DocumentAdapter.ViewHolder, position: Int) {
        holder.bind(documents.get(position), position)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var documentIconImageView: ImageView? = null
        var documentTitleTextView : TextView? = null
        var documentFileTypeTextView : TextView? = null
        var documentSizeTextView : TextView? = null
        var documentDateTextView : TextView? = null
        var documentBageImageView : ImageView? = null
        var documentAboutInformationInsideTextView : TextView? = null
        var documentSettingsImageView : ImageView? = null
        var documentInformationLayout: LinearLayout? = null
        init{
            documentIconImageView = itemView?.findViewById(R.id.document_image_view)
            documentTitleTextView = itemView?.findViewById(R.id.document_title_text_view)
            documentFileTypeTextView = itemView?.findViewById(R.id.document_file_type_text_view)
            documentSizeTextView = itemView?.findViewById(R.id.document_size_text_view)
            documentDateTextView = itemView?.findViewById(R.id.document_date_text_view)
            documentBageImageView = itemView?.findViewById(R.id.bage_image_view)
            documentAboutInformationInsideTextView = itemView?.findViewById(R.id.document_about_information_inside_text_view)
            documentSettingsImageView = itemView?.findViewById(R.id.document_settings_image_view)
            documentInformationLayout = itemView?.findViewById(R.id.document_information_layout)
        }

        fun bind(vkDocument: VKDocument, position: Int){
            documentTitleTextView!!.setText(vkDocument.title)
            when(vkDocument.type) {
                1 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_text_72)
                }
                2 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_archive_72)
                }
                3 -> {
                    this.setIsRecyclable(false)
                    Glide.with(activity).load(vkDocument.url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.camera_200).into(documentIconImageView!!)
                    documentIconImageView!!.visibility = if (vkDocument.url != null) View.VISIBLE else View.GONE
                }
                4 -> {
                    this.setIsRecyclable(false)
                    Glide.with(activity).load(vkDocument.url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.camera_200).into(documentIconImageView!!)
                    documentIconImageView!!.visibility = if (vkDocument.url != null) View.VISIBLE else View.GONE
                }
                5 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_music_72)
                }
                6 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_video_72)
                }

                7 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_book_72)
                }
                8 -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_other_72)
                }
                else -> {
                    documentIconImageView!!.setBackgroundResource(R.drawable.ic_placeholder_document_other_72)
                }
            }

            documentFileTypeTextView!!.setText("${vkDocument.ext.toUpperCase()} · ")

            documentSizeTextView!!.setText("${vkDocument.size.getSize()} · ")
            documentDateTextView!!.setText(vkDocument.date.toDate())
            documentSettingsImageView!!.setOnClickListener {
                activity.showPopUpWindow(documentSettingsImageView!!, vkDocument.id, position)
            }
            documentInformationLayout!!.setOnClickListener {
                activity.showDocument(vkDocument)
            }
        }
    }

    internal fun removeDoc(position: Int){
        documents.removeAt(position)
    }

    internal fun getDocumentsSize() : Int{
        return documents.size
    }

    internal fun renameDocument(position: Int, title : String){
        val doc = documents[position]
        val newDoc = VKDocument(doc.id, title, doc.size, doc.ext, doc.url, doc.date, doc.type)
        documents[position] = newDoc
    }
}