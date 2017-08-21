#配置CMake的最低版本
cmake_minimum_required(VERSION 3.4.1)

#配置工程路径
#set(path_project /home/wuhuannan/AndroidPrj/Live)
set(path_project D:/AndroidStudio/Live)

#配置头文件的包含路径
include_directories(${path_project}/app/src/main/cpp/include/x264)
include_directories(${path_project}/app/src/main/cpp/include/faac)
include_directories(${path_project}/app/src/main/cpp/include/rtmpdump)
include_directories(${path_project}/app/src/main/cpp/include/queue)

#添加自己写的库
add_library(live-lib
            SHARED
            src/main/cpp/live.c
            )

add_library(queue-lib
            SHARED
            src/main/cpp/queue.c
            )

#添加视频编码x264标准库
add_library( x264
             STATIC
             IMPORTED
            )

#设置预编译静态库的路径
set_target_properties(
                       x264
                       PROPERTIES IMPORTED_LOCATION
                       ${path_project}/app/libs/${ANDROID_ABI}/libx264.a
                    )

#添加音频编码faac库
add_library( faac
             STATIC
             IMPORTED
            )

set_target_properties(
                       faac
                       PROPERTIES IMPORTED_LOCATION
                       ${path_project}/app/libs/${ANDROID_ABI}/libfaac.a
                    )

#添加RTMP库
add_library( rtmpdump
             STATIC
             IMPORTED
            )

set_target_properties(
                       rtmpdump
                       PROPERTIES IMPORTED_LOCATION
                       ${path_project}/app/libs/${ANDROID_ABI}/librtmp.a
                    )

#找到Android的log库（这个库已经在Android平台中了）
find_library(
            log-lib
            log
            )

#找到Android的的库（这个库已经在Android平台中了）
find_library(
            android-lib
            android
            )

#把需要的库链接到自己的库中
target_link_libraries(
            live-lib
            ${log-lib}
            ${android-lib}
            queue-lib
            x264
            faac
            rtmpdump
            )