package com.example.memeshare

import  android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var MainImageUrl:String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme();
    }
    private  fun loadMeme(){
        ProgressBar.visibility=View.VISIBLE
// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
               MainImageUrl= response.getString("url")
                Glide.with(this).load(MainImageUrl).listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBar.visibility=View.GONE
                        return  false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBar.visibility= View.GONE
                        return false
                    }
                }).into(memes)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }

    fun NextMeme(view: View) {
        loadMeme();

    }

    fun ShareMeme(view: View) {
        //ImageView imageview= setCo;
        //Glide.with(this).load(MainImageUrl).into()
        val bitmap=Bitmap()
        Picasso
            .with(this)
            .load(MainImageUrl)
            .into(new Target() {
            @Override fun onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                val intent  = Intent(Intent.ACTION_SEND)
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(intent, "Share this meme Using ...."));
            }
                @Override  fun onBitmapFailed(Drawable errorDrawable) { }
                @Override  fun onPrepareLoad(Drawable placeHolderDrawable) { }
            });

        val uri:Uri
        uri= Uri.parse(MainImageUrl)
        val intent  = Intent(Intent.ACTION_SEND)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        val chooser= Intent.createChooser(intent,"Share this meme Using ....")
        startActivity(chooser)



    }
}