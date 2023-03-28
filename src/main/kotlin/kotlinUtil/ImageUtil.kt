package com.mirai.kotlinUtil


import com.mirai.AmusementPlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.pathString


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
         * 从本地加载Image
         */
        suspend fun loadImage(event: GroupMessageEvent,imageName: String): Image?{
            try {
                val s =
                    AmusementPlugin.dataFolderPath.pathString + File.separator + "image" + File.separator + imageName
                val file = File(s)
                if (file.exists()){
                    val input = withContext(Dispatchers.IO) {
                        FileInputStream(file)
                    }
                    val out = ByteArrayOutputStream()
                    val buffer = ByteArray(2048)
                    var len: Int
                    while (withContext(Dispatchers.IO) {
                            input.read(buffer)
                        }.also { len = it } > 0) {
                        out.write(buffer, 0, len)
                    }
                    val toExternalResource =
                        out.toByteArray().toExternalResource()
                    val imageId: String = toExternalResource.uploadAsImage(event.group).imageId
                    withContext(Dispatchers.IO) {
                        toExternalResource.close()
                    }
                    return Image(imageId)
                }
                println("error in loadImage!62")
                return null
            }catch (e: Exception){
                println(e.message)
                println("error in loadImage!65")
                return null
            }

        }

        /**
         * 保存图片到指定位置
         * @param imageUri 图片网址
         * @return Boolean 是否保存成功
         * @author 岚雨凛<cheng_ying@outlook.com>
         */

        suspend fun saveImage(imageUri: String, imageName: String): Boolean {
            val infoStream = ByteArrayOutputStream()
            try {
                val request = Request.Builder().url(imageUri).headers(headers.build()).get().build()
                val response: Response = client.build().newCall(request).execute()


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
//                infoStream.write((Math.random() * 100).toInt() + 1)


                val imagePath = AmusementPlugin.dataFolderPath.resolve("image")
                if (!imagePath.exists()){
                    imagePath.createDirectory()
                }
                withContext(Dispatchers.IO) {
                    FileOutputStream(imagePath.pathString + File.separator + imageName)
                        .write(infoStream.toByteArray())
                }

                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

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