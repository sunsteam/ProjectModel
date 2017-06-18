package com.yomii.core.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

public class OkHttpGlideModule implements GlideModule {

    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //5G缓存
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, "smartstandardimage",5000*1024*1024)
                //new ExternalCacheDiskCacheFactory(context, "smartstandardimage",30000*1024*1024)
        );
    }

    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
