package com.me.ml.utils;

import static android.Manifest.permission.VIBRATE;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

/**
 * 设备震动
 */
public final class VibrateUtils {

    /**
     * 控制设备震动的服务
     */
    private static Vibrator vibrator;

    //私有构造器防止外部通过new关键字实例化该类
    private VibrateUtils() {
        //抛出异常明确表示不支持实例化
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 基本震动
     * Vibrate.
     * RequiresPermission注解表明调用此方法需要VIBRATE权限
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param milliseconds The number of milliseconds to vibrate. 使设备震动指定的毫秒数
     */
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long milliseconds) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.vibrate(milliseconds);
    }

    /**
     * 带音频属性的震动（Android 5及以上）
     * Vibrate.
     * RequiresApi注解表明此方法在API级别21（Android 5.0 Lollipop）及以上才可使用。
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param milliseconds The number of milliseconds to vibrate.
     * @param attributes   {@link AudioAttributes} corresponding to the vibration. For example,
     *                     specify {@link AudioAttributes#USAGE_ALARM} for alarm vibrations or
     *                     {@link AudioAttributes#USAGE_NOTIFICATION_RINGTONE} for
     *                     vibrations associated with incoming calls.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long milliseconds, AudioAttributes attributes) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.vibrate(milliseconds, attributes);
    }

    /**
     * 兼容震动
     * 根据Android版本选择合适的震动方法，以支持后台震动
     * VibrateCompat - Can vibrate in background
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param milliseconds he number of milliseconds to vibrate.
     */
    @RequiresPermission(VIBRATE)
    public static void vibrateCompat(final long milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vibrate(milliseconds, getAudioAttributes());
        } else {
            vibrate(milliseconds);
        }
    }

    /**
     * 震动模式（重复震动）
     * Vibrate.
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param pattern An array of longs of times for which to turn the vibrator on or off.
     * @param repeat  The index into pattern at which to repeat, or -1 if you don't want to repeat.
     */
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long[] pattern, final int repeat) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.vibrate(pattern, repeat);
    }

    /**
     * 带音频属性的震动模式（Android 5及以上）
     * Vibrate.
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param pattern    An array of longs of times for which to turn the vibrator on or off.
     * @param repeat     The index into pattern at which to repeat, or -1 if you don't want to repeat.
     * @param attributes {@link AudioAttributes} corresponding to the vibration. For example,
     *                   specify {@link AudioAttributes#USAGE_ALARM} for alarm vibrations or
     *                   {@link AudioAttributes#USAGE_NOTIFICATION_RINGTONE} for
     *                   vibrations associated with incoming calls.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long[] pattern, final int repeat, AudioAttributes attributes) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.vibrate(pattern, repeat, attributes);
    }

    /**
     * 兼容震动模式（重复震动）
     * VibrateCompat - Can vibrate in background
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param pattern An array of longs of times for which to turn the vibrator on or off.
     * @param repeat  The index into pattern at which to repeat, or -1 if you don't want to repeat.
     */
    @RequiresPermission(VIBRATE)
    public static void vibrateCompat(final long[] pattern, final int repeat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vibrate(pattern, repeat, getAudioAttributes());
        } else {
            vibrate(pattern, repeat);
        }
    }

    /**
     * 取消震动
     * Cancel vibrate.
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     */
    @RequiresPermission(VIBRATE)
    public static void cancel() {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.cancel();
    }

    /**
     * 私有方法：获取Vibrator实例
     */
    private static Vibrator getVibrator() {
        //单例模式获取Vibrator实例
        if (vibrator == null) {
            vibrator = (Vibrator) Utils.getApp().getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static AudioAttributes getAudioAttributes() {
        return new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
    }
}
