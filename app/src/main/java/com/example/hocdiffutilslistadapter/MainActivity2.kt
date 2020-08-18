package com.example.hocdiffutilslistadapter

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hocdiffutilslistadapter.adapter.AdapterRating
import com.example.hocdiffutilslistadapter.adapter.Handle
import com.example.hocdiffutilslistadapter.model.Rating
import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        init()
    }

    private fun init() {
        DoBackground(this).execute("http://192.168.1.51/Core300/api/danhgia/9")
    }

    class DoBackground(context: MainActivity2) : AsyncTask<String, List<Rating>, List<Rating>>(),
        Handle {
        private lateinit var recyclerView: RecyclerView
        private lateinit var adapterRating: AdapterRating
        private var listRating: MutableList<Rating> = mutableListOf()
        private val weakReference: WeakReference<MainActivity2> = WeakReference(context)
        private val context = weakReference.get()!!
        private val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        override fun doInBackground(vararg p0: String?): List<Rating> {
            val url = URL(p0[0])
            val data: String?
            var idUser: Int
            var idPhong: Int
            var noidung: String
            var sao: Double
            var ngayDanhgia: String

            try {
                val connection: HttpURLConnection = httpConnection(url)
                connection.doInput = true
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream
                    val stream = BufferedInputStream(connection.inputStream)
                    data = readStream(stream)
                    val jsonArray = JSONArray(data)

                    for (i in 0 until jsonArray.length()) {
                        val childJsonObject = jsonArray[i] as JSONObject
                        idUser = childJsonObject.getInt("idUser")
                        idPhong = childJsonObject.getInt("idPhong")
                        noidung = childJsonObject.getString("noidung")
                        sao = childJsonObject.getDouble("sao")
                        ngayDanhgia = childJsonObject.getString("ngayDanhgia")
                        val rating = Rating(idUser, idPhong, noidung, sao, ngayDanhgia)
                        listRating.add(rating)
                    }
                    connection.disconnect()
                }
            } catch (e: Exception) {
                Log.d(e.message, "doInBackground: ")
            } finally {

            }
            publishProgress(listRating)
            return listRating
        }


        private fun readStream(stream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(stream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { str -> stringBuilder.append(str) }
            return stringBuilder.toString()
        }

        override fun onPostExecute(result: List<Rating>?) {
            super.onPostExecute(result)
            adapterRating =
                AdapterRating(
                    result as MutableList<Rating>,
                    context
                    , this
                )
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView = context.findViewById(R.id.recycleView2)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapterRating
        }

        override fun sendList(list: List<Rating>) {
            listRating.clear()
            listRating.addAll(list)
        }

        override fun editRating(rating: Rating) {
            context.edtInputContent.setText(rating.noidung)
            context.edtInputRating.setText(rating.sao.toString())
            context.btnUpdate.setOnClickListener {
                val newContent = context.edtInputContent.text.toString()
                val newRating = context.edtInputRating.text.toString()
                val ratingNew = Rating(2, 9, newContent, newRating.toDouble(), rating.ngayDanhGia)
                adapterRating.updateData(updateData(rating, ratingNew))
                recyclerView.scrollToPosition(adapterRating.itemCount - 1)
            }
        }

        private fun updateData(rating: Rating, ratingNew: Rating): MutableList<Rating> {
            val listUpdate = mutableListOf<Rating>()
            listUpdate.addAll(listRating)
            listUpdate.remove(rating)
            listUpdate.add(ratingNew)
            return listUpdate
        }

        override fun onPreExecute() {
            super.onPreExecute()
            context.btnUpdate.setOnClickListener {

            }
            context.btnAdd.setOnClickListener {
                val ratingNew = Rating(
                    2,
                    9,
                    context.edtInputContent.text.toString(),
                    context.edtInputRating.text.toString().toDouble(),
                    date
                )

                val newList = arrayListOf<Rating>()
                newList.add(ratingNew)
                adapterRating.insertData(newList)
                recyclerView.scrollToPosition(adapterRating.itemCount - 1)

                Thread(Runnable {
                    val url = URL("http://192.168.1.51/Core300/api/danhgia")
                    try {
                        val httpURLConnection = httpConnection(url)
                        httpURLConnection.requestMethod = "POST"
                        httpURLConnection.setRequestProperty(
                            "Content-Type",
                            "application/json; utf-8"
                        )
                        httpURLConnection.doOutput = true
                        val jsonObject = createJson()
                        setPostRequestContent(httpURLConnection, jsonObject = jsonObject)
                        httpURLConnection.connect()
                        httpURLConnection.responseMessage
                    } catch (e: Exception) {
                        Log.d(e.message, "onPreExecute: ")
                    }

                }).start()
            }

        }

        private fun createJson(): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put("idUser", 2)
            jsonObject.put("idPhong", 9)
            jsonObject.put("noidung", context.edtInputContent.text.toString())
            jsonObject.put("sao", context.edtInputRating.text.toString().toDouble())
            jsonObject.put("ngayDanhgia", date)
            return jsonObject
        }

        private fun httpConnection(url: URL): HttpURLConnection {
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.connectTimeout = 10000
            httpURLConnection.readTimeout = 10000
            return httpURLConnection
        }

        private fun setPostRequestContent(conn: HttpURLConnection, jsonObject: JSONObject) {

            val os = conn.outputStream as OutputStream
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(jsonObject.toString())
            writer.flush()
            writer.close()
            os.close()
        }
    }


}