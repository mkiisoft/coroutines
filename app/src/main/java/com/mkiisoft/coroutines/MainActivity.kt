package com.mkiisoft.coroutines

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.rx2.await
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference
import java.util.*

class MainActivity : AppCompatActivity() {

    private val imageUrl = "https://m.media-amazon.com/images/I/81IBQNycOOL._SS500_.jpg"

    private lateinit var disposable: Disposable
    private val job = Job()

    private val emitter: Channel<Comment> = Channel()

    private lateinit var adapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearNavigationBar()

        recycler_albums.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        LinearSnapHelper().attachToRecyclerView(recycler_albums)

        adapter = AlbumAdapter { item, list ->
            CoroutineScope(Dispatchers.IO).launch {
                val bitmapClick = Glide.with(this@MainActivity).asBitmap().load(item.album).submit().get()
                val paletteClick = Palette.from(bitmapClick).generate()
                withContext(Dispatchers.Main) {
                    loadImagePalette(bitmapClick, paletteClick)
                    loadAlbum(item)
                    adapter.list = list.filter { it.id != item.id }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        runExample()
        retry.setOnClickListener { runExample() }

        // Coroutines
//        GlobalScope.launch(Dispatchers.Main) {
//            val bitmap = GlobalScope.async { Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }
//            val palette = GlobalScope.async { Palette.from(bitmap.await()).generate() }
//            loadImagePalette(bitmap.await(), palette.await())
//        }


//        GlobalScope.launch(Dispatchers.IO + job) {
//            val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//            val palette = Palette.from(bitmap).generate()
//            withContext(Dispatchers.Main) { loadImagePalette(bitmap, palette) }
//        }

//        CoroutineScope(Dispatchers.IO + job).launch {
//            val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//            bitmap.recycle()
//            try {
//                val palette = Palette.from(bitmap).generate()
//                withContext(Dispatchers.Main) { loadImagePalette(bitmap, palette) }
//            } catch (e: IllegalArgumentException) {
//                e.printStackTrace()
//            }
//        }

//        CoroutineScope(Dispatchers.Main).launch {
//            val bitmap = GlobalScope.async { Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }
//            val palette = GlobalScope.async { Palette.from(bitmap.await()).generate() }
//            loadImagePalette(bitmap.await(), palette.await())
//        }

//        CoroutineScope(Dispatchers.Main).launch {
//            val bitmap = getImage()
//            val palette = getPalette(bitmap)
//            loadImagePalette(bitmap, palette)
//        }

        // Coroutines
//        GlobalScope.launch(Dispatchers.IO) {
//            val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//            withContext(Dispatchers.Main) { loadImage(bitmap) }
//        }

        // Coroutines
//        GlobalScope.launch(Dispatchers.Main + job) { loadImage(withContext(Dispatchers.IO) { Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }) }

        // Coroutines Channel
//        CoroutineScope(Dispatchers.Main).launch {
//            getComment(emitter)
//        }
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val comments = retrofitClient.getComments()
//            comments.forEach {
//                emitter.send(it)
//                delay(500)
//            }
//            emitter.close()
//        }

        // Coroutines Channel
//        CoroutineScope(Dispatchers.IO).launch {
//            println("INIT_FIRST")
//
//            launch { getComment(emitter) }
//
//            launch {
//                val comments = retrofitClient.getComments()
//                comments.forEach {
//                    emitter.send(it)
//                    delay(500)
//                }
//                emitter.close()
//            }
//            println("FINISH_FIRST")
//        }
//
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//            val palette = Palette.from(bitmap).generate()
//            withContext(Dispatchers.Main) { loadImagePalette(bitmap, palette) }
//        }

        // Coroutines Annotation
//        GlobalScope.launch(Dispatchers.IO) {
//            requestImage()
//        }

        // Coroutines with Jobs
//        val uiJob = CoroutineScope(Dispatchers.IO + job)
//
//        uiJob.launch {
//            requestImage()
//        }

        // Retrofit 2.6 + Coroutines
//        CoroutineScope(Dispatchers.IO + job).launch {
//            val comments = retrofitClientRx.getCommentsRx().await()
//            comments.forEach { println(it) }
//        }

//        CoroutineScope(Dispatchers.IO + job).launch {
//            val response = retrofitAlbums.getAlbums()
//            if (response.isSuccessful) {
//                val albums = response.body()!!
//                val random = Random().nextInt(albums.size)
//                val album = albums[random]
//                val bitmap = Glide.with(this@MainActivity).asBitmap().load(album.album).submit().get()
//                val palette = Palette.from(bitmap).generate()
//                withContext(Dispatchers.Main) {
//                    loadImagePalette(bitmap, palette)
//                    loadAlbum(album)
//                    adapter.list = albums.filter { it.id != album.id }
//                    recycler_albums.adapter = adapter
//                }
//            }
//        }

        // RxJava
//        disposable = Single.create<Pair<Bitmap, Palette>> { emitter ->
//            val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//            val palette = Palette.from(bitmap).generate()
//            emitter.onSuccess(Pair(bitmap, palette))
//        }
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { pair ->
//                    loadImagePalette(pair.first, pair.second)
//                }

        // RxJava
//        disposable = Single.fromCallable {
//            Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
//        }
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::loadImage)

        // RxJava
//        disposable = Single.fromCallable {  Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::loadImage)

        // RxJava Retrofit
//        disposable = retrofitClientRx
//                .getCommentsRx()
//                .toObservable()
//                .flatMapIterable { list -> list }
//                .concatMap { Observable.just(it).delay(500, TimeUnit.MILLISECONDS) }
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { println(it) }

        // AsyncTask
//        AsyncJob(this).execute(imageUrl)
    }

    private fun runExample() {
        // Retrofit 2.6 + Coroutines + RecyclerView
        retry.visibility = View.GONE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val albums = retrofitAlbumsRx.getAlbumsRx().await()
                val random = Random().nextInt(albums.size)
                val album = albums[random]
                val bitmap = Glide.with(this@MainActivity).asBitmap().load(album.album).submit().get()
                val palette = Palette.from(bitmap).generate()
                withContext(Dispatchers.Main) {
                    loadImagePalette(bitmap, palette)
                    loadAlbum(album)
                    adapter.list = albums.filter { it.id != album.id }
                    recycler_albums.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    album.visibility = View.GONE
                    root.setBackgroundColor(Color.parseColor("#D12C2A"))
                    window.statusBarColor = Color.parseColor("#D12C2A")
                    retry.visibility = View.VISIBLE
                }
            }
        }
    }

    private val retrofitClient: Webservice = Retrofit.Builder()
//              .baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Webservice::class.java)

    private val retrofitClientRx: Webservice = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(Webservice::class.java)

    private val retrofitAlbums: Webservice = Retrofit.Builder()
            .baseUrl("https://next.json-generator.com/api/json/get/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Webservice::class.java)

    private val retrofitAlbumsRx: Webservice = Retrofit.Builder()
            .baseUrl("https://next.json-generator.com/api/json/get/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(Webservice::class.java)

    @WorkerThread
    suspend fun requestImage() {
        val bitmap = Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
        val palette = Palette.from(bitmap).generate()
        withContext(Dispatchers.Main) {
            loadImagePalette(bitmap, palette)
        }
    }

    @UiThread
    private fun loadImage(bitmap: Bitmap) {
        album.setImageBitmap(bitmap)
    }

    @UiThread
    private fun loadImagePalette(bitmap: Bitmap, palette: Palette) {
        album.visibility = View.VISIBLE
        album.setImageBitmap(bitmap)
        root.setBackgroundColor(palette.getDarkVibrantColor(Color.WHITE))
        window.statusBarColor = palette.getDarkVibrantColor(Color.WHITE)
    }

    @UiThread
    private fun loadAlbum(album: Album) {
        album_text.text = album.title
        album_artist.text = album.artist
    }

    private suspend fun handleImage() {
        val bitmap = GlobalScope.async { Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }
        val palette = GlobalScope.async { Palette.from(bitmap.await()).generate() }
        loadImagePalette(bitmap.await(), palette.await())
    }

    private suspend fun getImage(): Bitmap = withContext(Dispatchers.IO) { Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get() }

    private suspend fun getPalette(bitmap: Bitmap): Palette = coroutineScope {
        withContext(Dispatchers.Default) { Palette.from(bitmap).generate() }
    }

    private suspend fun getComment(receiver: ReceiveChannel<Comment>) {
        for (comment in receiver) {
            println("COMMENT: ${comment.email}")
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        job.cancel()
        super.onDestroy()
    }

    private fun clearNavigationBar() {

        val uiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = uiVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#40000000")
        }
    }


    class AsyncJob internal constructor(context: Activity) : AsyncTask<String, Void, Pair<Bitmap, Palette>>() {

        private val activityInstance: WeakReference<Activity> = WeakReference(context)

        override fun doInBackground(vararg params: String): Pair<Bitmap, Palette> {
            val bitmap = Glide.with(activityInstance.get()!!).asBitmap().load(params[0]).submit().get()
            val palette = Palette.from(bitmap).generate()

            return Pair(bitmap, palette)
        }

        override fun onPostExecute(result: Pair<Bitmap, Palette>) {
            super.onPostExecute(result)

            val activity = activityInstance.get()
            if (activity == null || activity.isFinishing) return

            val image = activity.findViewById<ImageView>(R.id.album)
            image.setImageBitmap(result.first)

            val root = activity.findViewById<ConstraintLayout>(R.id.root)
            root.setBackgroundColor(result.second.getDarkVibrantColor(Color.WHITE))
            activity.window.statusBarColor = result.second.getDarkVibrantColor(Color.WHITE)
        }
    }
}