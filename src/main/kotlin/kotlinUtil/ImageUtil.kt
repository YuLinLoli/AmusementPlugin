package com.yulin.kotlinUtil


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


class ImageUtil {
    companion object {
        private val client = OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(
            60000,
            TimeUnit.MILLISECONDS
        )
        private val headers = Headers.Builder()
            .add(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36 Edg/84.0.522.59"
            )

        /**
         * （群）通过网址直接转换为可发送的Image
         * @param imageUri 图片网址
         * @param event 群消息事件
         * @return Image? 可为空的图片
         * @author 岚雨凛<cheng_ying@outlook.com>
         */
        suspend fun getImage(imageUri: String, event: GroupMessageEvent): Image? {
            val infoStream = ByteArrayOutputStream()

            try {
                val request = Request.Builder().url(imageUri).headers(headers.build()).get().build()
                val response: Response = client.build().newCall(request).execute();


                val `in` = response.body?.byteStream()


                val buffer = ByteArray(2048)
                var len: Int
                if (`in` != null) {
                    while (withContext(Dispatchers.IO) {
                            `in`.read(buffer)
                        }.also { len = it } > 0) {
                        infoStream.write(buffer, 0, len)
                    }
                }
                infoStream.write((Math.random() * 100).toInt() + 1)
                val toByteArray = infoStream.toByteArray()
                val inputStream = ByteArrayInputStream(toByteArray)
                val toExternalResource = inputStream.toExternalResource()
                val uploadImage = event.sender.uploadImage(toExternalResource)

                withContext(Dispatchers.IO) {
                    toExternalResource.close()
                    infoStream.close()
                }

                return uploadImage
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * （私）通过网址直接转换为可发送的Image
         * @param imageUri 图片网址
         * @param event 私聊消息事件
         * @return Image? 可为空的图片
         * @author 岚雨凛<cheng_ying@outlook.com>
         */
        suspend fun getImage(imageUri: String, event: FriendMessageEvent): Image? {
            val infoStream = ByteArrayOutputStream()

            try {
                val request = Request.Builder().url(imageUri).headers(headers.build()).get().build()
                val response: Response = client.build().newCall(request).execute();


                val `in` = response.body?.byteStream()


                val buffer = ByteArray(2048)
                var len: Int
                if (`in` != null) {
                    while (withContext(Dispatchers.IO) {
                            `in`.read(buffer)
                        }.also { len = it } > 0) {
                        infoStream.write(buffer, 0, len)
                    }
                }
                infoStream.write((Math.random() * 100).toInt() + 1)
                val toByteArray = infoStream.toByteArray()
                val inputStream = ByteArrayInputStream(toByteArray)
                val uploadImage = event.sender.uploadImage(inputStream.toExternalResource())
                withContext(Dispatchers.IO) {
                    infoStream.close()
                }

                return uploadImage
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }


    }
}