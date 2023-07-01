
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.

project("audio_engine")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# In order to run build, it is necessary to have Oboe library plugged from source
# More details here: https://github.com/google/oboe/blob/main/docs/GettingStarted.md

find_package (oboe REQUIRED CONFIG)

# Specifying location of ffmpeg shared objects
set(ffmpeg_DIR ffmpeg)
add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavutil.so)

add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavformat.so)

add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavcodec.so)

add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libswresample.so)

add_library( # Sets the name of the library.
        audio_engine

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        # temporary measure, these two should be header files
        player/AudioPlayer.cpp
        decoder/DecodingUtilities.cpp
        utils/AudioBuffer.cpp

        audio_engine.cpp
)

# Specifying location of ffmpeg headers
include_directories(${ffmpeg_DIR}/${ANDROID_ABI}/include)

# Specify folders included into this module
include_directories(decoder)
include_directories(player)
include_directories(utils)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        audio_engine

        # Links the target library to the log library
        # included in the NDK.
        PRIVATE oboe::oboe
        PRIVATE log
        PRIVATE android
        PRIVATE mediandk

        avutil
        avformat
        avcodec
        swresample
)