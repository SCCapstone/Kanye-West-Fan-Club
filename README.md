# Legend Stats
<img src="https://user-images.githubusercontent.com/46630936/165160007-e19fae42-ba31-46f4-8f18-881f2476a868.png" width="75">

Legend Stats is an Android app designed to help Teamfight Tactic players improve their gaming performance. The app quickly summarizes an individual's match history for quick reference and allows for searching others' match history. Users can create builds (groups of units during gameplay) and add them to a community list comprised of builds from fellow users. Additionally, there are resources to view current playable characters and what items to build from others.

Detailed description: https://github.com/SCCapstone/Kanye-West-Fan-Club/wiki/Project-Description

## External Requirements
A android emulator from https://developer.android.com/studio

## Setup
Clone the repo, open Android Studio, click File, find the cloned repo in file directory and open it. 

## Running
Run our .apk for Android in Android Studio on a emulator or to a connected Android phone device.

## Deployment

## Testing
Currently our Riot API key expires every 24 hours so we have to continue cycling the key for the release to work.
If you run into an error while testing, contact us and we will generate a new API key and replace the expired one in Firebase.

For future reference, we have applied for a production API key which we do not need to refresh. 
Riot dev APIgets flooded with API key requests so we believe it will take a while for us to have access.

For testing purposes create a email, password, display name with one of the following puiid. You can copy the values and hold down the text prompt on the emulator to paste:
  * puuid: bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng
  * puuid: tHue9Ykg5kKRq3of1RcmYXsLwFonrY6aKQm5cALKuE3K3CLpIq-Qh3SwiNMYmXZzXtmQBsMqOKeMpg
      
For testing search feature you can use:
  * "Liquid Goose" "NA1"
  * "Amde" "NA1"
  * "Jane" "NA1"

For the testing milestone: 
We made unit tests in:
#### Kanye-West-Fan-Club/CSCEProjectRun2/app/src/test/java/com/example/csceprojectrun2/ 
And we used behavorial tests in:
#### Kanye-West-Fan-Club/CSCEProjectRun2/app/src/androidTest/java/com/example/csceprojectrun2/

## Testing Technology

## Running Tests
When running the tests you first need to open Android studio. Next locate which type of test you intend on running based on the folder locations listed in the [Testing](https://github.com/SCCapstone/Kanye-West-Fan-Club/tree/Stephen-Thompson#testing) section. Then if you would like to run all the tests in the folder you can right click on the folder and click "Run 'Tests in '(name of folder location)'. If you wish to run individual tests, you may identify which test you wish to run then right click on the file and select "Run '(file name of tests)'.

## Style Guide
JAVA: https://google.github.io/styleguide/javaguide.html

# Authors
* Jack Snelgrove - snelgrj@email.sc.edu
* Austin Tindal - aptindal@email.sc.edu
* Doug Stokes - stokesdb@email.sc.edu
* Sincere Dixon - sincere@email.sc.edu
* Stephen Thompson - ST16@email.sc.edu

See our about us page [here](https://github.com/SCCapstone/Kanye-West-Fan-Club/blob/main/about.md)

