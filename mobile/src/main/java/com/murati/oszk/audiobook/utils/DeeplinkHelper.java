package com.murati.oszk.audiobook.utils;

import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.murati.oszk.audiobook.model.MusicProvider;
import com.murati.oszk.audiobook.model.MusicProviderSource;

import java.io.File;

public class DeeplinkHelper {
    private static final String TAG = LogHelper.makeLogTag(DeeplinkHelper.class);
    private static final String MEK_URL_REGEXP = "(.*(\\d{5,})[/](\\d{5,})).*";

    // https://firebase.google.com/docs/dynamic-links/android/create?authuser=0

    public static void generateDeeplinkForBook(String book) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.example.com/"))
            .setDomainUriPrefix("https://example.page.link")
            // Open links with this app on Android
            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
            // Open links with com.example.ios on iOS
            .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
            .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
    }

    public static String getMekLinkForMediaId(String mediaId) {
        String book = MediaIDHelper.getCategoryValueFromMediaID(mediaId);
        Iterable<MediaMetadataCompat> tracks = MusicProvider.getTracksByEbook(book);
        MediaMetadataCompat first = tracks.iterator().next();
        //TODO fix to filesource

        String urlString = first.getDescription().getIconUri().toString();
        return urlString.replaceAll(MEK_URL_REGEXP, "$1");
    }

    public static String getMediaIdFromMekLink(String url) {
        return "";
    }
}
