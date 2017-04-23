//package com.shulan.simplegank.rx2;
//
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Action;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
//
///**
// * Created by houna on 17/4/21.
// */
//
//public class Rx2Timer {
//
//    private Disposable disposable;
//    private long take;
//    private long period;
//    private long initialDelay;
//    private TimeUnit unit;
//    private long pauseTake = 0l;
//    private long resumeTake = 0l;
//    private boolean isPause = false;
//
//
//    public Rx2Timer start(){
//        if(isPause)
//            return restart();
//        if(disposable == null || disposable.isDisposed()){
//            disposable = Observable.interval(initialDelay, period, unit)
//                    .subscribeOn(Schedulers.single())
//                    .take(take + 1)
//                    .map(new Function<Long, Long>() {
//                        @Override
//                        public Long apply(Long aLong) throws Exception {
//                            return null;
//                        }
//                    })
//                    .observeOn(AndroidSch
//                            edulers.mainThread())
//                    .subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//
//                        }
//                    }, new Action() {
//                        @Override
//                        public void run() throws Exception {
//
//                        }
//                    });
//        }
//        return this;
//    }
//
//    public void resume(){
//        if(!isPause)
//            return;
//        isPause = false;
//        if(disposable == null || disposable.isDisposed()){
//            disposable =
//        }
//    }
//
//    private Rx2Timer restart() {
//        stop();
//        return start();
//    }
//
//    private void stop() {
//        if(disposable != null && !disposable.isDisposed()){
//            disposable.dispose();
//        }
//        if(isPause)
//            cleanPauseState();
//    }
//
//    private void cleanPauseState() {
//        isPause = false;
//        resumeTake = 0l;
//        pauseTake = 0l;
//    }
//
//    public static Builder builder(){
//        return new Builder();
//    }
//
//    public static final class Builder{
//        private long take = 60;
//        private long period = 1;
//        private long initialDelay = 0;
//        private TimeUnit unit = TimeUnit.SECONDS;
////        private OnComplete onComplete;
////        private OnCount onCount;
////        private OnError onError;
//
//        Builder(){
//
//        }
//
//        public Builder period(int period){
//            this.period = period;
//            return this;
//        }
//
//    }
//
//
//
//}
