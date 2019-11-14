### 直播技术从入门到放弃

![直播技术从入门到放弃](https://upload-images.jianshu.io/upload_images/2570030-479d0badf9050b98.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 音视频基础知识

#### 从一个电影播放说起

我们想看一部电影的时候，需要用到各种视频播放器来播放视频：

![视频播放](https://upload-images.jianshu.io/upload_images/2570030-c3af9ec11535f3fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

视频的播放包含着很多的原理，涉及到很多音视频的基础知识。这次要讲的视频直播技术无非就是音视频知识的应用，所以我们有必要先来补充一些音视频的基础知识。

#### 视频播放原理

我们先从一个简单的视频播放器的原理开始讲述，下图是一个最简单的视频播放的过程（不包括视频加密等等过程）：

![视频播放原理](http://upload-images.jianshu.io/upload_images/2570030-378928c16d48f49d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是一个视频播放的最基本的原理流程图，从这个图可以很整体得看到视频处理的一些主要步骤，后面我们会详细介绍一些这里提到的基本概念。


#### 为什么视频需要经过封装处理呢？

因为摄像头采集到的画面、以及麦克风采集到的音频数据是经过压缩的处理，不然视频文件就会很大。

也就是说：

1. 录像、录音，实质是一个压缩采集的图像或者声音的过程。这个过程就是视频编码压缩的过程。
2. 播放视频、音频文件实质上就是解压缩的过程，这个过程又称为解码。

#### 视频的封装格式介绍

封装格式的作用是：视频码流和音频码流按照一定的格式存储在一个文件中。

封装格式分析工具：Elecard Format Analyzer

为什么要音视频分开存储呢？因为音视频的编码格式各种各样，同时编码必然会造成混乱。

常见的视频封装格式有：

![视频封装格式](http://upload-images.jianshu.io/upload_images/2570030-8d0b33ee909d23a1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

以两个格式为例子，介绍一下原理：

![常见视频封装格式原理](http://upload-images.jianshu.io/upload_images/2570030-380bc36cf0a4e26f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1. MPEG2-TS格式是由一个一个数据大小固定的TS-Packet组成，因此可以支持快进。
2. FLY格式由FLV HEADER以及一个一个大小不固定的Tag组成。因为FLV格式直接能够用flash（浏览器）播放，因此常用于视频直播邻域。我们在做RTMP推流的时候，一开始就需要发送头信息。因为数据单元大小不固定，因此原生的视频播放器不支持FLV视频的快进（有些播放器进行了处理可以快进）。

#### 视频编解码常见格式介绍

视频的压缩算法很多，因此编码格式就会有很多种，下面介绍一些常见的编解码格式：

1. 常见的视频编码格式有：H.264、MPEG2、VP8等（谷歌收购的WebRTC视频通话就是用VP8）。
2. 视频解码得到的像素数据YUV、RGB。YUV格式中，Y代表亮度，UV代表色度，人眼对亮度比较敏感，两者比例为4:1，与生物学的理论有关。

![视频编码格式](http://upload-images.jianshu.io/upload_images/2570030-9254164f6838c8aa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

原理分析：

![视频编码格式原理分析](http://upload-images.jianshu.io/upload_images/2570030-56998f35bca5f31b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

以H264为例，H264是由大小不固定的NALU构成。（NALU实质是一种数据结构）。H264里面有很多子压缩算法，原理比较复杂，包括了熵编码，环路滤波，帧内检测，帧间检测等知识。H264编码原理比较复杂，因此H264的压缩效率是几百到几千倍。

###### 在做视频直播的时候：我们需要学会FFmpeg即可，因为这个库封装了H264等格式的处理，也可以X264这个库来进行视频编码。

视频解码（摄像机获取）得到的是视频像素数据，保存了屏幕上每个像素点的像素值。常见的像素数据格式有RGB24， RGB32， YUV420P，YUV422P，YUV444P等。压缩编码中一般使用的是YUV格式的像素数据，最为常见的格式为YUV420P。

YUV视频格式是没有经过压缩的，很大。早期在电视上面用得比较多，比如古老的黑白、彩电。彩电播放早期的黑白视频实质上是只播放了Y（亮度）的数据，因为黑白视频只有Y的数据嘛。

![YUV格式简介](http://upload-images.jianshu.io/upload_images/2570030-6095892b656be550.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

RGB也有很多种，比如RGB24，不同的RGB编码色彩丰富度不同。

![RGB格式简介](http://upload-images.jianshu.io/upload_images/2570030-4a49dbbd7f8d862f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 音频编解码常见格式介绍

1. 常见的音频编码格式有：AAC、MP3。
2. 音频解码得到的是音频采样数据，然后喇叭才能播放。常见格式是PCM，实质是一个一个的采样值。单位时间内震动的数据，包括振幅和频率。常用采样率44100，人耳朵能够擦觉到的最高采样率。

![常见的音频编码格式](http://upload-images.jianshu.io/upload_images/2570030-7d2ae10dae49e7c3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###### 在做视频直播的时候：音频常用AAC来进行编码，用FAAC库来处理；视频用H264编码。

![音频编码格式AAC介绍](http://upload-images.jianshu.io/upload_images/2570030-0227273d33bd806d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

音频采样数据PCM：保存了音频中每个采样点的值，音频采样数据体积很大，一般需要进过压缩，我们平常说的“无损”实质上是没有损失的压缩的意思。

![PCM格式简介](http://upload-images.jianshu.io/upload_images/2570030-52fed0426fc8f091.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 相关播放（编辑）工具介绍

1. YUV：YUV Player
2. PCM：Adobe Audition
3. 查看视频信息：MediaInfo
4. 视频编码数据：Elecard Format Analyzer
5. 视频编码分析工具：Elecard Stream Eye

有兴趣可以下载玩玩。

#### FFmpeg介绍

![FFmpeg](https://upload-images.jianshu.io/upload_images/2570030-9b746adfa726d801.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

FFmpeg是开源的C/C++音视频处理的类库，这个库十分优秀，以至于很多大公司都在用。主流的视频播放器几乎都使用了FFmpeg。下面是FFmpeg的百度百科介绍：

![FFmpeg百科](https://upload-images.jianshu.io/upload_images/2570030-28a5d203a0f48ee8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们利用FFmpeg进行编程的时候几乎就是基于这个流程图来进行。比如说，编程的时候我们会拿到解码器，解码读取数据，绘制到屏幕上面的时候可能还需要把YUV数据转换为RGB等等。

##### FFmpeg的八个函数库的基本介绍

如下图所示：

![FFmpeg库简介](http://upload-images.jianshu.io/upload_images/2570030-241003f297926d1f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 直播核心技术

#### 总体架构

常见的直播APP如下：

![直播APP](https://upload-images.jianshu.io/upload_images/2570030-5467ee60e90d8abd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面是一个最基本的视频直播的基本总体架构图：

![直播基本架构](http://upload-images.jianshu.io/upload_images/2570030-e3565e21b756577e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们需要有一个主播客户端进行音视频采集，压缩，然后通过RTMP协议进行推流，推到流媒体服务器，然后其他客户端统一从流媒体服务器引流，播放。关于这里的过程的一些细节我将会在后续文章中慢慢道来。

我们主要是实现RTMP推流，引流的部分通过一些直播RTMP协议的播放器来实现。

因为项目比较大，设计的知识也比较广，例如h264编码标准，aac编码，RTMP协议。这里我们只概述一些关键的核心逻辑与实现思路，具体的实现可以参考源代码，因为工作原因源代码晚点再上。

推流的流程主要分为以下几个步骤：

1. 调用Java的相关API进行音视频的采集。
2. 初始化一些C相关的库，然后用户点击开始推流。
3. 因为摄像头、麦克风采集到的数据是原始的数据，需要经过编码。其中，视频编码使用h264编码格式，对应x264库；音频编码使用aac编码，使用faac库。
4. 使用rtmpdump（librtmp）库进行推流。

#### Linux（Ubuntu系统或者虚拟机）搭建流媒体服务器

先来了解一下俄罗斯人开发的Nginx服务器，Nginx ("engine x") 是一个高性能的HTTP和反向代理服务器，也是一个IMAP/POP3/SMTP服务器。Nginx是由Igor Sysoev为俄罗斯访问量第二的Rambler.ru站点开发的，第一个公开版本0.1.0发布于2004年10月4日。其将源代码以类BSD许可证的形式发布，因它的稳定性、丰富的功能集、示例配置文件和低系统资源的消耗而闻名。

因为Nginx服务器支持RTMP协议，因此这里我们作为流媒体服务器。

######Tips：实际开发可能是多台服务器，需要高并发架构支持。反向代理：外网的请求先转发到内网，然后返回的时候再转到外网。

Nginx是一种模块化的服务器，开源，我们可以自由添加删除我们自己的模块，不同的模块使用不同的端口号，互不冲突，如下图所示：

![模块化的Nginx服务器](http://upload-images.jianshu.io/upload_images/2570030-220d6eb81fa41aac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一、先下载安装  nginx 和 nginx-rtmp 编译依赖工具。

	sudo apt-get install build-essential libpcre3 libpcre3-dev libssl-dev

二、创建一个工作目录，并切换到工作目录。

因为后续可能还需要进行编译等操作，最好先给目录递归赋予权限：

	mkdir Live
	chmod +x -R Live/

三、下载 nginx 和 nginx-rtmp源码（wget是一个从网络上自动下载文件的自由工具）

	wget http://nginx.org/download/nginx-1.8.1.tar.gz
	wget https://github.com/arut/nginx-rtmp-module/archive/master.zip

四、解压

如果你用的是Ubuntu系统，直接通过界面操作解压即可。

安装unzip工具，解压下载的安装包
	sudo apt-get install unzip

5.解压 nginx 和 nginx-rtmp安装包

	tar -zxvf nginx-1.8.1.tar.gz
	unzip master.zip

其中：

	-zxvf分别是四个参数
	x : 从 tar 包中把文件提取出来
	z : 表示 tar 包是被 gzip 压缩过的，所以解压时需要用 gunzip 解压
	v : 显示详细信息
	f : xxx.tar.gz :  指定被处理的文件是 xxx.tar.gz

五、添加 nginx-rtmp 模板编译到 nginx

切换到 nginx-目录

	cd nginx-1.8.1
	./configure --with-http_ssl_module --add-module=../nginx-rtmp-module-master

六、编译、安装nginx

	编译nginx源码
	make
	安装需要超级权限
	sudo make install

######Tips：make、安装编译默认就是调用configure脚本进行编译安装，因此安装路径可以在configure找到。编译过程就是先生成目标文件.o，然后进行链接得到可执行程序。安装的过程就是把一些文件复制到系统目录里面去。

七、安装nginx init 脚本

	下载init脚本到/etc/init.d/nginx目录中，其中/etc/init.d目录放是Linux进程启动的时候会执行的一些脚本
	sudo wget https://raw.github.com/JasonGiedymin/nginx-init-ubuntu/master/nginx -O /etc/init.d/nginx
	给目录添加执行权限
	sudo chmod +x /etc/init.d/nginx
	刷新一下
	sudo update-rc.d nginx defaults

八、启动和停止nginx 服务，生成配置文件

	sudo service nginx start

这时候在浏览器输入http://127.0.0.1/，就可以看到页面，证明服务器已经成功安装好了。然后我们停止服务器，进行后续操作。

	sudo service nginx stop

![Nginx安装成功之后的首页](http://upload-images.jianshu.io/upload_images/2570030-2313833fef5c2efb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

九、安装 FFmpeg

这里FFmpeg是用于做音视频的编解码的。同时我们测试直播功能的时候，也可以通过ffplay进行测试，ffplay支持RTMP协议。

	cd ffmpeg-2.6.9

	编译FFmpeg
	./configure --disable-yasm
	安装FFmpeg（这个过程比较久，耐心等待）
	sudo make install

输入下面的命令测试是否安装好：

	输出安装信息
	ffmpeg -v

######Tips：服务器内存不足需要配置虚拟内存。

十、配置 nginx-rtmp 服务器

Nginx安装在/usr/local/nginx中，其中：

1. conf是配置目录
2. html是网页的目录

打开 /usr/local/nginx/conf/nginx.conf

在末尾添加如下配置：

	rtmp {
	    server {
	            listen 1935;
	            chunk_size 4096;
	
	            application live {
	                    live on;
	                    record off;
	                    exec ffmpeg -i rtmp://localhost/live/$name -threads 1 -c:v libx264 -profile:v baseline -b:v 350K -s 640x360 -f flv -c:a aac -ac 1 -strict -2 -b:a 56k rtmp://localhost/live360p/$name;
	            }
	            application live360p {
	                    live on;
	                    record off;
	        }
	    }
	}

主要是配置RTMP协议，Nginx是一种模块化的服务器，可以自由添加功能。这里主要是配置RTMP模块的一些参数，包括端口号，视频的编解码参数、格式等等。

保存上面配置文件，然后重新启动nginx服务：

	sudo service nginx restart

######Tips：Nginx的安装目录可以通过在configure查找“install”关键字找到。
######这里也可以看到http的配置，默认是80端口，如果有冲突的话，可以修改其他端口。不同功能使用不同端口，互不冲突。
######如果你使用了防火墙，请允许端口 tcp 1935。

#### Windows平台下流媒体服务器搭建

由于Windows平台编译Nginx源码比较麻烦，因此我们用其他人编译好的版本即可，下载地址如下：

	https://github.com/illuspas/nginx-rtmp-win32

同理，我们需要配置nginx-rtmp-win32\conf\nginx.conf文件，然后点击根目录下面的nginx.exe即可打开服务器。

#### 流媒体服务器测试

引流有两个方法测试（将来也可以通过手机客户端进行测试）：

一、服务器配置测试播放器：将Flash播放器的所有文件复制到目录：/usr/local/nginx/html（Windows是www目录）/，然后修改播放地址

播放器下载地址：

	链接：http://pan.baidu.com/s/1bo9ePRp 密码：627r

###### 方法一需要把index.html的推流IP地址改为你自己的

二、用ffplay播放RTMP直播流：

在终端输入一下命令即可：

	ffplay rtmp://172.17.120.44/live/test

###### 方法二需要把命令中的推流IP地址改为你自己的，并且最好把FFmpeg添加到环境变量

推流的测试：

目前来说也是有两种方法（将来会添加Android端引流测试）：

一、使用之前创建的FFmpeg Visual项目，博客地址：

	http://www.jianshu.com/p/5b7c18285667

二、使用之前创建的FFmpeg Android Studio项目，相关博客地址：

	http://www.jianshu.com/p/91e07b7dc8ca
	http://www.jianshu.com/p/da140cffadba

#### 准备Native依赖库

编译faac、x264、rtmpdump库，输出静态库.a文件，添加配置到Android Studio项目里面。

#### 音频采集

初始化一个AudioRecord：

```java
int channelConfig = audioParams.getChannel() == 1 ?
        AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
//最小缓冲区大小
minBufferSize = AudioRecord.getMinBufferSize(audioParams.getSampleRateInHz(), channelConfig, AudioFormat.ENCODING_PCM_16BIT);
mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
        audioParams.getSampleRateInHz(),
        channelConfig,
        AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
```

开启一个音频的录制线程进行录制，并且实时发送到Native层，由Native层进行aac编码并且推流。录音线程的核心逻辑如下：

````java
@Override
public void run() {
    //开始录音
    mAudioRecord.startRecording();

    while (isPushing) {
        //通过AudioRecord不断读取音频数据
        byte[] buffer = new byte[minBufferSize];
        int len = mAudioRecord.read(buffer, 0, buffer.length);
        if (len > 0) {
            //传给Native代码，进行音频编码
            mPushNative.fireAudio(buffer, len);
        }
    }
}
````

#### 音频编码

调用faacEncEncode函数进行音频编码，然后发送RTMP信息，其中AAC头信息在push线程中发送：

```c
/**
 * 对音频采样数据进行AAC编码
 */
JNIEXPORT void JNICALL
Java_com_nan_live_pusher_PushNative_fireAudio(JNIEnv *env, jobject instance, jbyteArray buffer_,
                                              jint length) {

    int *pcmbuf;
    unsigned char *bitbuf;
    jbyte *b_buffer = (*env)->GetByteArrayElements(env, buffer_, 0);
    pcmbuf = (short *) malloc(nInputSamples * sizeof(int));
    bitbuf = (unsigned char *) malloc(nMaxOutputBytes * sizeof(unsigned char));
    int nByteCount = 0;
    unsigned int nBufferSize = (unsigned int) length / 2;
    unsigned short *buf = (unsigned short *) b_buffer;
    while (nByteCount < nBufferSize) {
        int audioLength = nInputSamples;
        if ((nByteCount + nInputSamples) >= nBufferSize) {
            audioLength = nBufferSize - nByteCount;
        }
        int i;
        for (i = 0; i < audioLength; i++) {//每次从实时的pcm音频队列中读出量化位数为8的pcm数据。
            int s = ((int16_t *) buf + nByteCount)[i];
            pcmbuf[i] = s << 8;//用8个二进制位来表示一个采样量化点（模数转换）
        }
        nByteCount += nInputSamples;
        //利用FAAC进行编码，pcmbuf为转换后的pcm流数据，audioLength为调用faacEncOpen时得到的输入采样数，bitbuf为编码后的数据buff，nMaxOutputBytes为调用faacEncOpen时得到的最大输出字节数
        int byteslen = faacEncEncode(audio_encode_handle, pcmbuf, audioLength,
                                     bitbuf, nMaxOutputBytes);
        if (byteslen < 1) {
            continue;
        }
        add_aac_body(bitbuf, byteslen);//从bitbuf中得到编码后的aac数据流，放到数据队列
    }
    if (bitbuf)
        free(bitbuf);
    if (pcmbuf)
        free(pcmbuf);

    (*env)->ReleaseByteArrayElements(env, buffer_, b_buffer, 0);
}
```

#### 视频采集

视频采集主要是调用Camera的相关API，基本步骤如下：

1. 打开摄像头，初始化一些信息，开始预览（预览需要一个SurfaceView的Holder）。
2. 如果需要实时获取摄像头采集数据的时候，还需要调用addCallbackBuffer设置缓冲区，然后添加Callback。

核心代码如下：

```java
/**
 * 开始预览
 */
private void startPreview() {
    try {
        //SurfaceView初始化完成，可以进行预览
        mCamera = Camera.open(mVideoParams.getCameraId());
        Camera.Parameters param = mCamera.getParameters();
        //设置预览图像的像素格式？？？？？？？？？NV-21
        param.setPreviewFormat(ImageFormat.NV21);
        //设置预览画面宽高
        param.setPreviewSize(mVideoParams.getWidth(), mVideoParams.getHeight());
        //设置预览帧频，但是x264压缩的时候还是有另外一个帧频的
        //param.setPreviewFpsRange(mVideoParams.getFps() - 1, mVideoParams.getFps());
        mCamera.setParameters(param);

        mCamera.setPreviewDisplay(mSurfaceHolder);

        //如果是正在直播的话需要实时获取预览图像数据
        //缓冲区，大小需要根据摄像头的分辨率而定，x4换算为字节
        buffers = new byte[mVideoParams.getWidth() * mVideoParams.getHeight() * 4];
        mCamera.addCallbackBuffer(buffers);
        mCamera.setPreviewCallbackWithBuffer(this);

        //开始预览
        mCamera.startPreview();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

获取摄像头数据data，传到Native层，然后由Native层负责h264编码并且推流：

```java
/**
 * 摄像头数据更新回调，获取摄像头的数据，并且推流
 *
 * @param data
 * @param camera
 */
@Override
public void onPreviewFrame(byte[] data, Camera camera) {
    if (mCamera != null) {
        mCamera.addCallbackBuffer(buffers);
    }

    if (isPushing) {
        mPushNative.fireVideo(data);
    }
}
```

#### 视频编码

主要调用x264_encoder_encode方法进行视频编码，然后通过add_264_sequence_header方法添加RTMP头信息，通过add_264_body添加RTMP body：

```c
/**
 * 将采集到视频数据进行H264编码
 */
JNIEXPORT void JNICALL
Java_com_nan_live_pusher_PushNative_fireVideo(JNIEnv *env, jobject instance, jbyteArray buffer_) {

    //视频数据转为YUV420P
    //NV21->YUV420P
    jbyte *nv21_buffer = (*env)->GetByteArrayElements(env, buffer_, NULL);
    jbyte *u = pic_in.img.plane[1];
    jbyte *v = pic_in.img.plane[2];
    //nv21 4:2:0 Formats, 12 Bits per Pixel
    //nv21与yuv420p，y个数一致，uv位置对调
    //nv21转yuv420p  y = w*h,u/v=w*h/4
    //nv21 = yvu yuv420p=yuv y=y u=y+1+1 v=y+1
    //如果要进行图像处理（美颜），可以再转换为RGB
    //还可以结合OpenCV识别人脸等等
    memcpy(pic_in.img.plane[0], nv21_buffer, y_len);
    int i;
    for (i = 0; i < u_len; i++) {
        *(u + i) = *(nv21_buffer + y_len + i * 2 + 1);
        *(v + i) = *(nv21_buffer + y_len + i * 2);
    }

    //h264编码得到NALU数组
    x264_nal_t *nal = NULL; //NAL
    int n_nal = -1; //NALU的个数
    //进行h264编码
    if (x264_encoder_encode(video_encode_handle, &nal, &n_nal, &pic_in, &pic_out) < 0) {
        LOGE("%s", "编码失败");
        return;
    }
    //使用rtmp协议将h264编码的视频数据发送给流媒体服务器
    //帧分为关键帧和普通帧，为了提高画面的纠错率，关键帧应包含SPS和PPS数据
    int sps_len, pps_len;
    unsigned char sps[100];
    unsigned char pps[100];
    memset(sps, 0, 100);
    memset(pps, 0, 100);
    pic_in.i_pts += 1; //顺序累加
    //遍历NALU数组，根据NALU的类型判断
    for (i = 0; i < n_nal; i++) {
        if (nal[i].i_type == NAL_SPS) {
            //复制SPS数据
            sps_len = nal[i].i_payload - 4;
            memcpy(sps, nal[i].p_payload + 4, sps_len); //不复制四字节起始码
        } else if (nal[i].i_type == NAL_PPS) {
            //复制PPS数据
            pps_len = nal[i].i_payload - 4;
            memcpy(pps, nal[i].p_payload + 4, pps_len); //不复制四字节起始码

            //发送序列信息
            //h264关键帧会包含SPS和PPS数据
            add_264_sequence_header(pps, sps, pps_len, sps_len);

        } else {
            //发送帧信息
            add_264_body(nal[i].p_payload, nal[i].i_payload);
        }

    }

    (*env)->ReleaseByteArrayElements(env, buffer_, nv21_buffer, 0);
}
```

#### 音视频推流

进行RTMP推流的时候，需要使用生产者消费者的线程模型，编码属于生产者，推流属于消费者。并且需要一个双向链表进行数据的进出。



核心代码如下：

```c
/**
 * 从队列中不断拉取RTMPPacket发送给流媒体服务器）
 */
void *push_thread(void *arg) {
    JNIEnv *env;//获取当前线程JNIEnv
    (*javaVM)->AttachCurrentThread(javaVM, &env, NULL);

    //建立RTMP连接
    RTMP *rtmp = RTMP_Alloc();
    if (!rtmp) {
        LOGE("rtmp初始化失败");
        goto end;
    }
    RTMP_Init(rtmp);
    rtmp->Link.timeout = 5; //连接超时的时间
    //设置流媒体地址
    RTMP_SetupURL(rtmp, rtmp_path);
    //发布rtmp数据流
    RTMP_EnableWrite(rtmp);
    //建立连接
    if (!RTMP_Connect(rtmp, NULL)) {
        LOGE("%s", "RTMP 连接失败");
        throwNativeError(env, CONNECT_FAILED);
        goto end;
    }
    //计时
    start_time = RTMP_GetTime();
    if (!RTMP_ConnectStream(rtmp, 0)) { //连接流
        LOGE("%s", "RTMP ConnectStream failed");
        throwNativeError(env, CONNECT_FAILED);
        goto end;
    }
    is_pushing = TRUE;
    //发送AAC头信息
    add_aac_sequence_header();

    while (is_pushing) {
        //发送
        pthread_mutex_lock(&mutex);
        pthread_cond_wait(&cond, &mutex);
        //取出队列中的RTMPPacket
        RTMPPacket *packet = queue_get_first();
        if (packet) {
            queue_delete_first(); //移除
            packet->m_nInfoField2 = rtmp->m_stream_id; //RTMP协议，stream_id数据
            int i = RTMP_SendPacket(rtmp, packet, TRUE); //TRUE放入librtmp队列中，并不是立即发送
            if (!i) {
                LOGE("RTMP 断开");
                RTMPPacket_Free(packet);
                pthread_mutex_unlock(&mutex);
                goto end;
            } else {
                LOGI("%s", "rtmp send packet");
            }
            RTMPPacket_Free(packet);
        }

        pthread_mutex_unlock(&mutex);
    }
    end:
    LOGI("%s", "释放资源");
    free(rtmp_path);
    RTMP_Close(rtmp);
    RTMP_Free(rtmp);
    (*javaVM)->DetachCurrentThread(javaVM);
    return 0;
}
```

#### RTMP引流

最后，需要进行RTMP引流，我们直接使用Vitamio或者IJK Player等播放器即可，该播放器支持RTMP协议，当然我们也可以自己直接使用FFmpeg来对RTMP流进行解码播放。

```java
video_live = (VideoView) findViewById(R.id.video_live);

// RTMP地址
String rtmpUrl = PreferenceUtils.getInstance(this).getRTMPUrl();
video_live.setVideoPath(rtmpUrl);
video_live.setMediaController(new MediaController(this));
video_live.requestFocus();

video_live.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setPlaybackSpeed(1.0f);
    }
});
```

### 回顾与总结

最后，我们通过一张图来回顾一下：

![视频直播架构](https://upload-images.jianshu.io/upload_images/2570030-e34ba03bc9e292ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1. 本次分享只是带大家入门，而不会十分深入地展开相关知识。
2. 多媒体开发需要很多音视频基础知识、各种多媒体协议、另外还需要会使用常见的音视频处理库。
3. 本次只是一次直播技术的入门性质的分享，Demo本身是不完善的，通常我们项目开发都是采用成熟稳定的直播SDK比较多，比如腾讯直播SDK等等，他们功能强大而且稳定，但是一般企业级的接入都是要收费的，而且费用不便宜。
4. 如果对音视频直播技术感兴趣的话，可以针对以下功能点学习并且对Demo进行优化：

    * UI界面优化，包括推流预览界面与引流播放界面的优化
    * 加入新的功能，比如点赞功能、主播列表页、弹幕、评论功能等
    * 直播优化，比如音视频降噪、直播延迟优化，推流与播放分标率的切换、实时美颜功能、镜像翻转、伴音、连麦、纯音/视频推流、动态水印、回音消除、录制与回放等功能
    * 另外还有Android本身的的一些API的使用优化，比如使用Camera2 API、优化Wake Lock的使用、权限申请优化等等