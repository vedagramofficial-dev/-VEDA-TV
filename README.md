# VEDA TV
Fresh Android starter project for a movies and TV series app.

## Build APK on GitHub
1. Open **Actions**.
2. Select **VEDA TV Android APK**.
3. Select **Run workflow** on `main`.
4. Download **VEDA-TV-debug-APK** from the completed run's Artifacts section.

A push to `main` also triggers the workflow.

The starter includes a Kotlin Android screen and Media3/ExoPlayer playback for a direct HTTPS video URL (including HLS M3U8). Automatic source discovery and the movie catalog are not implemented yet; the next step is integrating a catalog provider and authorized streaming sources.
