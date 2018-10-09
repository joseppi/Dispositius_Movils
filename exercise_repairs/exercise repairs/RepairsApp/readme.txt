we must add at build.gradle (Module: app)

compile 'com.google.android.gms:play-services:4.0.30'

nothing else!!!


and at the manifest file we must have to be able to use google maps:

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

<meta-data android:name="com.google.android.maps.v2.API_KEY"
                   android:value="AIzaSyA-0gJYHsyXAtjycdCF-I22qiikCb_pcso"/>

<meta-data android:name="com.google.android.gms.version"
                   android:value="@integer/google_play_services_version" />
or:

<meta-data android:name="com.google.android.gms.version"
                   android:value="4030500" />




it is useless to add: google-play-services.jar as it only contains class file,
the resources folder of the library is missing.

it is not necessary to add: android-support-v4.jar as it is included/linked with
the google play services library.