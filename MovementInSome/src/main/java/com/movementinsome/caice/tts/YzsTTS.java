package com.movementinsome.caice.tts;

/**
 * 云知声TTS语音合成初始化
 * Created by zzc on 2017/10/31.
 */

public class YzsTTS {
//    public SpeechSynthesizer mTTSPlayer;
//    private final String mFrontendModel= "/sdcard/unisound/tts/frontend_model";
//    private final String mBackendModel = "/sdcard/unisound/tts/backend_lzl";
//    private Context context;
//
//    public YzsTTS(Context context) throws IOException {
//        this.context=context;
//        // 初始化语音合成对象
//        mTTSPlayer = new SpeechSynthesizer(context, OkHttpParam.appKey, OkHttpParam.secret);
////        // 设置本地合成
////        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);
////
////        CreateFiles.copyFilesFassets(context,"frontend_model",mFrontendModel);
////        CreateFiles.copyFilesFassets(context,"backend_lzl",mBackendModel);
////
////        File _FrontendModelFile = new File(mFrontendModel);
////        if (!_FrontendModelFile.exists()) {
////            toastMessage("文件：" + mFrontendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
////        }
////        File _BackendModelFile = new File(mBackendModel);
////        if (!_BackendModelFile.exists()) {
////            toastMessage("文件：" + mBackendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
////        }
////        // 设置前端模型
////        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, mFrontendModel);
////        // 设置后端模型
////        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, mBackendModel);
//        // 设置回调监听
//        mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {
//
//            @Override
//            public void onEvent(int type) {
//                switch (type) {
//                    case SpeechConstants.TTS_EVENT_INIT:
//                        // 初始化成功回调
//                        log_i("onInitFinish");
//                        break;
//                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
//                        // 开始合成回调
//                        log_i("beginSynthesizer");
//                        break;
//                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
//                        // 合成结束回调
//                        log_i("endSynthesizer");
//                        break;
//                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
//                        // 开始缓存回调
//                        log_i("beginBuffer");
//                        break;
//                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
//                        // 缓存完毕回调
//                        log_i("bufferReady");
//                        break;
//                    case SpeechConstants.TTS_EVENT_PLAYING_START:
//                        // 开始播放回调
//                        log_i("onPlayBegin");
//                        break;
//                    case SpeechConstants.TTS_EVENT_PLAYING_END:
//                        // 播放完成回调
//                        log_i("onPlayEnd");
//                        break;
//                    case SpeechConstants.TTS_EVENT_PAUSE:
//                        // 暂停回调
//                        log_i("pause");
//                        break;
//                    case SpeechConstants.TTS_EVENT_RESUME:
//                        // 恢复回调
//                        log_i("resume");
//                        break;
//                    case SpeechConstants.TTS_EVENT_STOP:
//                        // 停止回调
//                        log_i("stop");
//                        break;
//                    case SpeechConstants.TTS_EVENT_RELEASE:
//                        // 释放资源回调
//                        log_i("release");
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onError(int type, String errorMSG) {
//                // 语音合成错误回调
//                log_i("onError");
//                toastMessage(errorMSG);
//            }
//        });
//        // 初始化合成引擎
//        mTTSPlayer.init(null);
//    }
//
//    public void onPause() {
//        // 主动停止识别
//        if (mTTSPlayer != null) {
//            mTTSPlayer.stop();
//        }
//    }
//
//    public void onDestroy() {
//        // 主动释放离线引擎
//        if (mTTSPlayer != null) {
//            mTTSPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
//        }
//    }
//
//    private void log_i(String log) {
//        Log.i("demo", log);
//    }
//
//    private void toastMessage(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
}
