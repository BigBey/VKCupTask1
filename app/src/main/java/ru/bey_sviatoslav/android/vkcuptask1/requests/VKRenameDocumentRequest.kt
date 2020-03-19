package ru.bey_sviatoslav.android.vkcuptask1.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKRenameDocumentRequest: VKRequest<Int> {
    constructor(docId : Int, title : String) : super("docs.edit"){
        addParam("doc_id", docId)
        addParam("title", title)
    }

    override fun parse(r: JSONObject): Int {
        val result = r.getInt("response")
        return result
    }
}
