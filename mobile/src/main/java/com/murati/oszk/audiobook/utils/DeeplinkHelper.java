package com.murati.oszk.audiobook.utils;

import android.net.Uri;

public class DeeplinkHelper {

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

    public static String getMekLinkForBook(String ebook) {
        return "";
    }
}
