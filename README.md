## Table of Contents
* [General Info](#general-info)
* [Demo](#demo)
* [Future Updates](#future-updates)
* [Step by Step Implementation](#step-by-step-implementation)

## General Info
Report any issue of your city to the authorities with this Android Application with Google Maps intergration.

## Demo
By downloading and installing to your Android device the <b>.apk</b> file you can:
* Sign In using a Google account
* Monitor your current location
<!-- TODO Continue Demo with application abilities -->

## Future Updates
Add Anonymous Authentication with Firebase
Implement a Drawer Menu that opens when user clicks their top-left profile picture that will contain a:
* <b>Profile</b> button that will lead to user's Profile (Organize Profile layout)
* <b>My Reports</b> button that will lead to a layout where all the user's report will be shown
* <b>City Issues</b> button that will lead to a layout where all the reports of the user's city will be shown 
* <b>Important Contacts</b> button that will lead at a layout with phone numbers and information about authorities and emergency services for more direct reports
* <b>About</b> button that will lead to a layout with information about the application
* <b>Logout</b> button and icon (remove it from profile layout)
<!-- TODO: Continue Future Updates with application expansion ideas -->

## Step by Step Implementation
Every step I took for the implementation of this project:
* Designed User Interface using https://app.diagrams.net/
![UI Design](https://github.com/Ntelos/City-Connect/blob/main/images/UI_Design.png?raw=true)
* Created Android Studio Project (with no activity selection)
* Created 3 activities (SignIn, Maps, Profile)
* Created a Firebase Project at https://console.firebase.google.com/
* Got a Google Maps API key with the help of the default comments at the <i>google_maps_api.xml</i> file
* Intergrated Googled Sign In option following this documentation: https://firebase.google.com/docs/auth/android/google-signin
* Designed Sign In layout
* Finished Sign In intergration
* Connected Sign In with Profile for now
* Designed Profile to show signed in user's name, email and ID
* Intergrated [Glide](https://github.com/bumptech/glide) library to show sign in user's profile picture
* Added app icon
* Connected Interfaces correctly
* Corrected exiting app methods
* Created pop up dialog
* Get every information needed for a report
* Firebase Database connection
* Report class
* Add records to database
