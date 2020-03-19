package ru.bey_sviatoslav.android.vkcuptask1


import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
import ru.bey_sviatoslav.android.vkcuptask1.adapter.DocumentAdapter
import ru.bey_sviatoslav.android.vkcuptask1.fragments.DocumentFragment
import ru.bey_sviatoslav.android.vkcuptask1.models.VKDocument
import ru.bey_sviatoslav.android.vkcuptask1.requests.VKDeleteDocumentRequest
import ru.bey_sviatoslav.android.vkcuptask1.requests.VKDocumentsRequest
import ru.bey_sviatoslav.android.vkcuptask1.requests.VKRenameDocumentRequest


class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var settings : SharedPreferences

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var documentsRecyclerView: RecyclerView
    private lateinit var documentAdapter : DocumentAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var forFragmentLayout : FrameLayout

    private var chosenDocId : Int = 0
    private var chosenDocPosition: Int = 0

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {

        }
    }
    private var vkDocuments : ArrayList<VKDocument> =  ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        settings = PreferenceManager.getDefaultSharedPreferences(this)

        forFragmentLayout = findViewById(R.id.for_fragment)

        initProgressBar()

        initRefreshLayout()

        loginVK()
    }

    private fun initRefreshLayout(){
        refreshLayout = findViewById(R.id.refresh_layout)
        refreshLayout.setOnRefreshListener {
            loadDocuments()
        }
    }

    private fun initRecyclerView() {
        documentsRecyclerView = findViewById<RecyclerView>(R.id.documents_recycler_view)
        documentsRecyclerView.setLayoutManager(LinearLayoutManager(this))
        documentAdapter = DocumentAdapter(this, vkDocuments)
        documentsRecyclerView.adapter = documentAdapter

        documentsRecyclerView.setHasFixedSize(true)
        documentsRecyclerView.setItemViewCacheSize(20)
        documentsRecyclerView.isDrawingCacheEnabled = true

        refreshLayout.isRefreshing = false
    }

    private fun initProgressBar(){
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                settings.edit().apply{
                    putInt("user_id", token.userId!!).apply()
                }
                loadDocuments()
            }

            override fun onLoginFailed(errorCode: Int) {
                // User didn't pass authorization
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun loginVK(){
        if(!VK.isLoggedIn()) {
            VK.login(this, arrayListOf(VKScope.DOCS))
        }else{
            loadDocuments()
        }

        VK.addTokenExpiredHandler(tokenTracker)
    }

    private fun loadDocuments(){
        VK.execute(VKDocumentsRequest(), object: VKApiCallback<List<VKDocument>> {
            override fun success(result: List<VKDocument>) {
                vkDocuments = result as ArrayList<VKDocument>
                progressBar.visibility = View.GONE
                initRecyclerView()
            }
            override fun fail(error: VKApiExecutionException) {
            }
        })
    }

    internal fun showPopUpWindow(view : View, docId : Int, docPosition: Int){
        setChosenDocId(docId)
        setChosenDocPosition(docPosition)
        val popupMenu = PopupMenu(this, view)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.pop_up_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.rename -> {
                renameDocument()
            }
            R.id.delete-> {
                deleteDocument()
            }
        }
        return true
    }

    private fun setChosenDocId(id : Int){
        chosenDocId = id
    }

    private fun setChosenDocPosition(position : Int){
        chosenDocPosition = position
    }

    private fun deleteDocument(){
        progressBar.visibility = View.VISIBLE
        VK.execute(VKDeleteDocumentRequest(settings.getInt("user_id", 0), chosenDocId), object: VKApiCallback<Int> {
            override fun success(result: Int) {
                if(result == 1){
                    progressBar.visibility = View.GONE
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setMessage("Вы точно хотите удалить этот документ?")
                        .setPositiveButton("OК", DialogInterface.OnClickListener { dialog, which ->
                            documentAdapter.removeDoc(chosenDocPosition)
                            documentAdapter.notifyItemRemoved(chosenDocPosition)
                            documentAdapter.apply {
                                notifyItemRangeChanged(
                                    chosenDocPosition,
                                    getDocumentsSize()
                                )
                            }
                            documentAdapter.notifyDataSetChanged()
                            chosenDocPosition = -1
                            chosenDocId = -1})
                        .setNegativeButton("ОТМЕНА", DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        .show()
                }
            }
            override fun fail(error: VKApiExecutionException) {
                Log.d("MainActivity_", error.toString())
            }
        })
    }

    private fun renameDocument(){
        val dialogBuilder: AlertDialog = Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.rename_dialog, null)

        val editText =
            dialogView.findViewById<View>(R.id.edt_comment) as EditText
        val button1: Button =
            dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2: Button =
            dialogView.findViewById<View>(R.id.buttonCancel) as Button

        button2.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
        button1.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            VK.execute(VKRenameDocumentRequest(chosenDocId, editText.text.toString()), object: VKApiCallback<Int> {
                override fun success(result: Int) {
                    if(result == 1){
                        progressBar.visibility = View.GONE
                        documentAdapter.renameDocument(chosenDocPosition, editText.text.toString())
                        documentAdapter.notifyDataSetChanged()
                        chosenDocPosition = -1
                        chosenDocId = -1
                        dialogBuilder.dismiss()
                    }
                }
                override fun fail(error: VKApiExecutionException) {
                    Log.d("MainActivity_", error.toString())
                }
            })
        })

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    internal fun showDocument(vkDocument: VKDocument){
        forFragmentLayout.visibility = View.VISIBLE
        supportFragmentManager.apply {
            beginTransaction()
                .add(R.id.for_fragment, DocumentFragment(vkDocument), "DocumentFragment")
                .commitAllowingStateLoss()
        }
    }
    internal fun hideDocument(){
        forFragmentLayout.visibility = View.GONE
        supportFragmentManager.apply {
            if(this.findFragmentByTag("DocumentFragment") != null) {
                beginTransaction()
                    .remove(this.findFragmentByTag("DocumentFragment")!!)
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        hideDocument()
    }
}
