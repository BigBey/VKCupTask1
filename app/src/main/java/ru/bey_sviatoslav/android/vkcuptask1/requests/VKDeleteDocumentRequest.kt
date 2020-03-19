package ru.bey_sviatoslav.android.vkcuptask1.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.bey_sviatoslav.android.vkcuptask1.models.VKDocument

class VKDeleteDocumentRequest: VKRequest<Int> {
    constructor(userId : Int,docId : Int) : super("docs.delete"){
        addParam("owner_id", userId)
        addParam("doc_id", docId)
    }

    override fun parse(r: JSONObject): Int {
        val result = r.getInt("response")
        return result
    }
}