package com.example.myapplication

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.database.CountriesData
import com.example.myapplication.mainScreen.CountriesListAdapter
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        //GlideToVectorYou.justLoadImage(imgView.context as Activity?, Uri.parse(imgUrl),imgView)
        //Log.i("image","set")


        val requestBuilder:RequestBuilder<PictureDrawable> = GlideToVectorYou
            .init()
            .with(imgView.context)
            .requestBuilder

        requestBuilder
            .load(Uri.parse(imgUrl))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imgView)
    }
}

@BindingAdapter("imageUrll")
fun bindImagee(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }}



//val requestBuilder:RequestBuilder<PictureDrawable> = GlideToVectorYou
  //  .init()
  //  .requestBuilder



@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CountriesData>?) {
    val adapter = recyclerView.adapter as CountriesListAdapter
    adapter.submitList(data)
}
