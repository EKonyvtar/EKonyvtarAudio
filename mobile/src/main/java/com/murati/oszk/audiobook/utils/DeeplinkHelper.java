package com.murati.oszk.audiobook.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.murati.oszk.audiobook.model.MusicProvider;
import com.murati.oszk.audiobook.model.MusicProviderSource;
import com.murati.oszk.audiobook.ui.MusicPlayerActivity;

import java.io.File;

public class DeeplinkHelper {
    private static final String TAG = LogHelper.makeLogTag(DeeplinkHelper.class);

    //TODO: move as remote config
    private static final String IOS_PACKAGE_NAME = "com.murati.oszk.audiobook";
    private static final String MEK_URL_REGEXP = "(.*(\\d{5,})[/](\\d{5,})).*";
    private static final String DYNAMIC_LINK_URL = "https://hangoskonyvtar.page.link";


    public static void createShareIntent(Activity activity, String message) {
        //Prepare intent
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    // https://firebase.google.com/docs/dynamic-links/android/create?authuser=0
    public static void shareDeeplinkForMediaID(final Activity activity, String mediaId) {
        //final String dynamicLinkUri;

        //First content_url
        final String content_url = getMekLinkForMediaId(mediaId);
        //dynamicLinkUri = content_url;

        DynamicLink.Builder dynamicLinkBuilder = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(content_url))
            .setDomainUriPrefix(DYNAMIC_LINK_URL)
            // Open links with this app on Android
            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
            // Open links with com.example.ios on iOS
            .setIosParameters(new DynamicLink.IosParameters.Builder(IOS_PACKAGE_NAME).build());

        //Build shortened URL
        Task<ShortDynamicLink> shortLinkTask = dynamicLinkBuilder
            .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
            .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                @Override
                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        //dynamicLinkUri = shortLink.toString();
                        Uri flowchartLink = task.getResult().getPreviewLink();

                        createShareIntent(activity,
                            "Hey, check this out: " + shortLink.toString()); //+ myDynamicLink;

                    } else {
                        Toast.makeText(activity, "Cannot share book", Toast.LENGTH_LONG).show();
                        Exception ex = task.getException();
                        Log.e(TAG, "Error getting shortened link for " + content_url);
                        if (ex != null)
                            Log.e(TAG, ex.getMessage());
                    }
                }
            });
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
