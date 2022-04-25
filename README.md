# Legend Stats

Legend Stats is an Android app designed to help Teamfight Tactic players improve their gaming performance. The app quickly summarizes an individual's match history for quick reference and allows for searching others' match history. Users can create builds (groups of units during gameplay) and add them to a community list comprised of builds from fellow users. Additionally, there are resources to view current playable characters and what items to build from others.

Detailed description: https://github.com/SCCapstone/Kanye-West-Fan-Club/wiki/Project-Description

## External Requirements

## Setup

## Running
Run our .apk for Android in Android Studio on a emulator or to a connected Android phone device.

## Deployment

## Testing
Currently our Riot API key expires every 24 hours so we have to continue cycling the key for the release to work.
We are trying to work on a small fix for this and will try and have it put in before you test it. If you run into
an error while testing before we implement this fix, contact us and we will generate a new API key and upload the 
new key to the code on the github.

For future reference, we have applied for a production API key which we do not need to refresh. Riot dev API
gets flooded with API key requests so we believe it will take a while for us to have access.

For testing purposes create a email and password with one of the following (tft name and puiid). You can copy the values and hold down the text prompt on the emulator to paste:
  #### 1.)
  #### puiid: bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng
  #### tftname: Dangu
  #### 2.)
  #### puiid: tHue9Ykg5kKRq3of1RcmYXsLwFonrY6aKQm5cALKuE3K3CLpIq-Qh3SwiNMYmXZzXtmQBsMqOKeMpg
  #### tftname: stanlee8
      
For testing search feature you can use "Liquid Goose" "NA1".

For the testing milestone: 
We made unit tests in:
Kanye-West-Fan-Club/CSCEProjectRun2/app/src/test/java/com/example/csceprojectrun2/ 
And we used behavorial tests in:
Kanye-West-Fan-Club/CSCEProjectRun2/app/src/androidTest/java/com/example/csceprojectrun2/

## Testing Technology

## Running Tests

When running the tests you first need to open Android studio. Next locate which type of test you intend on running based on the folder locations listed in the [Testing](https://github.com/SCCapstone/Kanye-West-Fan-Club/tree/Stephen-Thompson#testing) section. Then if you would like to run all the tests in the folder you can right click on the folder and click "Run 'Tests in '(name of folder location)'.. If you wish to run individual tests, you may identify which test you wish to run then right click on the file and select "Run '(file name of tests)'.

## Style Guide
JAVA: https://google.github.io/styleguide/javaguide.html
Kotlin: https://developer.android.com/kotlin/style-guide

# Authors
Jack Snelgrove - snelgrj@email.sc.edu
Austin Tindal - aptindal@email.sc.edu
Doug Stokes - stokesdb@email.sc.edu
Sincere Dixon - sincere@email.sc.edu
Stephen Thompson - ST16@email.sc.edu

