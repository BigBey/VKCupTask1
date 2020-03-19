package ru.bey_sviatoslav.android.vkcuptask1.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.bey_sviatoslav.android.vkcuptask1.models.VKDocument

class VKDocumentsRequest: VKRequest<List<VKDocument>> {
    constructor() : super("docs.get")

    override fun parse(r: JSONObject): List<VKDocument> {
        val response = r.getJSONObject("response")
        val documents = response.getJSONArray("items")
        val result = ArrayList<VKDocument>()
        for (i in 0 until documents.length()) {
            result.add(VKDocument.parse(documents.getJSONObject(i)))
        }
        return result
    }
}