package ru.true_ip.trueip;

/**
 * Created by Andrey Filimonov on 28.03.2018.
 */

public class ANR {
    /*
    03-28 07:44:17.988 437-461/system_process E/ActivityManager: ANR in ru.true_ip.trueip
                                                             PID: 2961
                                                             Reason: executing service ru.true_ip.trueip/service.SipService
                                                             Load: 0.0 / 0.0 / 0.0
                                                             CPU usage from 471180ms to 0ms ago (2018-03-28 07:36:26.753 to 2018-03-28 07:44:17.932):
                                                               18% 145/mdnsd: 9.9% user + 8% kernel
                                                               3.2% 321/audioserver: 1.6% user + 1.5% kernel
                                                               1.3% 437/system_server: 0.7% user + 0.6% kernel / faults: 3894 minor
                                                               1.3% 2961/ru.true_ip.trueip: 0.6% user + 0.6% kernel / faults: 27936 minor 1 major
                                                               1% 318/surfaceflinger: 0.3% user + 0.7% kernel / faults: 147 minor
                                                               0.7% 260/local_opengl: 0% user + 0.6% kernel / faults: 53 minor
                                                               0.3% 139/redis: 0.2% user + 0.1% kernel
                                                               0.2% 577/com.android.systemui: 0.1% user + 0.1% kernel / faults: 3999 minor
                                                               0.1% 310/android.hardware.sensors@1.0-service: 0% user + 0.1% kernel
                                                               0.1% 7/rcu_preempt: 0% user + 0.1% kernel
                                                               0.1% 140/adbd: 0% user + 0.1% kernel / faults: 2161 minor
                                                               0% 114/logd: 0% user + 0% kernel / faults: 41 minor
                                                               0% 112/jbd2/sdb3-8: 0% user + 0% kernel
                                                               0% 331/android.hardware.gnss@1.0-service: 0% user + 0% kernel
                                                               0% 20/ksoftirqd/3: 0% user + 0% kernel
                                                               0% 367/healthd: 0% user + 0% kernel
                                                               0% 312/batteryd: 0% user + 0% kernel
                                                               0% 2432/com.android.vending:instant_app_installer: 0% user + 0% kernel / faults: 42 minor
                                                               0% 400/logcat: 0% user + 0% kernel
                                                               0% 316/network_profile: 0% user + 0% kernel
                                                               0% 388/logcat: 0% user + 0% kernel
                                                               0% 3025/com.android.packageinstaller: 0% user + 0% kernel / faults: 398 minor
                                                               0% 564/com.android.inputmethod.latin: 0% user + 0% kernel / faults: 325 minor
                                                               0% 263/netd: 0% user + 0% kernel / faults: 63 minor
                                                               0% 99/kworker/3:1H: 0% user + 0% kernel
                                                               0% 309/android.hardware.graphics.allocator@2.0-service: 0% user + 0% kernel / faults: 93 minor
                                                               0% 648/com.android.phone: 0% user + 0% kernel / faults: 51 minor
                                                               0% 3239/kworker/u8:1: 0% user + 0% kernel
                                                               0% 319/vinput: 0% user + 0% kernel
                                                               0% 1084/com.google.android.gms.persistent: 0% user + 0% kernel / faults: 82 minor
                                                               0% 1142/com.android.launcher3: 0% user + 0% kernel / faults: 307 minor
                                                               0% 102/kworker/0:1H: 0% user + 0% kernel
                                                               0% 115/servicemanager: 0% user + 0% kernel
                                                               0% 118/kworker/2:1H: 0% user + 0% kernel
                                                               0% 333/rild: 0% user + 0% kernel
                                                               0% 2321/com.android.vending: 0% user + 0% kernel / faults: 157 minor
                                                               0% 16/ksoftirqd/2: 0% user + 0% kernel
                                                               0% 87/kworker/3:1: 0% user + 0% kernel
                                                               0% 120/kworker/1:1H: 0% user + 0% kernel
                                                               0% 311/sh: 0% user + 0% kernel
                                                               0% 359/genybaseband: 0% user + 0% kernel / faults: 144 minor
                                                               0% 1335/com.google.android.gms: 0% user + 0% kernel / faults: 24 minor
                                                               0% 1708/kworker/2:2: 0% user + 0% kernel
                                                               0% 1855/com.google.android.gms.ui: 0% user + 0% kernel
                                                               0% 3200/kworker/1:1: 0% user + 0% kernel
                                                              +0% 3306/kworker/0:2: 0% user + 0% kernel
                                                              +0% 3312/kworker/u8:2: 0% user + 0% kernel
                                                              +0% 3511/kworker/2:0: 0% user + 0% kernel
                                                             4.7% TOTAL: 2.3% user + 2.2% kernel + 0% iowait + 0.1% softirq
                                                             CPU usage from 3187955ms to 3187955ms ago (1970-01-01 00:00:00.000 to 1970-01-01 00:00:00.000) with 0% awake:
                                                             0% TOTAL: 0% user + 0% kernel

     */

    /*
    03-29 10:19:13.550 789-803/? E/ActivityManager: ANR in ru.true_ip.trueip
                                                PID: 21297
                                                Reason: executing service ru.true_ip.trueip/service.SipService
                                                Load: 8.4 / 8.83 / 8.63
                                                CPU usage from 85183ms to 1263ms ago (2018-03-29 10:17:45.109 to 2018-03-29 10:19:09.029):
                                                  6.4% 789/system_server: 4% user + 2.3% kernel / faults: 36809 minor
                                                  5% 440/surfaceflinger: 2.3% user + 2.6% kernel / faults: 100 minor
                                                  2.6% 1004/com.android.systemui: 1.8% user + 0.7% kernel / faults: 10952 minor
                                                  1.7% 3673/com.google.android.googlequicksearchbox:search: 1.5% user + 0.1% kernel / faults: 11889 minor
                                                  1.2% 3566/com.google.android.googlequicksearchbox: 0.8% user + 0.3% kernel / faults: 4004 minor
                                                  1.1% 19312/mdss_fb0: 0% user + 1.1% kernel
                                                  0.7% 3441/com.google.android.gms.persistent: 0.4% user + 0.2% kernel / faults: 1562 minor 1 major
                                                  0.6% 258/mmcqd/0: 0% user + 0.6% kernel
                                                  0.5% 10/rcu_preempt: 0% user + 0.5% kernel
                                                  0.5% 3846/com.google.android.gms: 0.4% user + 0.1% kernel / faults: 3196 minor
                                                  0.4% 17442/kworker/u16:6: 0% user + 0.4% kernel
                                                  0.4% 587/audioserver: 0.2% user + 0.2% kernel / faults: 92 minor
                                                  0.4% 249/cfinteractive: 0% user + 0.4% kernel
                                                  0.4% 301/msm-core:sampli: 0% user + 0.4% kernel
                                                  0.3% 18504/kworker/u16:7: 0% user + 0.3% kernel
                                                  0.3% 3/ksoftirqd/0: 0% user + 0.3% kernel
                                                  0.3% 20881/kworker/2:0: 0% user + 0.3% kernel
                                                  0.3% 57/system: 0% user + 0.3% kernel
                                                  0.3% 485/irq/215-fc38800: 0% user + 0.3% kernel
                                                  0.3% 17205/kworker/u16:2: 0% user + 0.3% kernel
                                                  0.2% 179/kgsl_worker_thr: 0% user + 0.2% kernel
                                                  0.2% 438/msm_irqbalance: 0% user + 0.1% kernel
                                                  0.2% 15/ksoftirqd/1: 0% user + 0.2% kernel
                                                  0.2% 577/perfd: 0% user + 0.1% kernel / faults: 208 minor
                                                  0.1% 18990/kworker/0:1: 0% user + 0.1% kernel
                                                  0.1% 40/kworker/u17:0: 0% user + 0.1% kernel
                                                  0.1% 20/ksoftirqd/2: 0% user + 0.1% kernel
                                                  0.1% 159/mdss_dsi_event: 0% user + 0.1% kernel
                                                  0.1% 16847/kworker/0:0: 0% user + 0.1% kernel
                                                  0.1% 365/logd: 0% user + 0% kernel / faults: 2 minor
                                                  0.1% 566/jbd2/dm-2-8: 0% user + 0.1% kernel
                                                  0.1% 19345/irq/504-synapti: 0% user + 0.1% kernel
                                                  0.1% 544/dmcrypt_write: 0% user + 0.1% kernel
                                                  0.1% 575/thermal-engine: 0% user + 0.1% kernel
                                                  0.1% 3314/kworker/1:1: 0% user + 0.1% kernel
                                                  0% 160/vsync_retire_wo: 0% user + 0% kernel
                                                  0% 432/android.hardware.graphics.allocator@2.0-service: 0% user + 0% kernel / faults: 41 minor
                                                  0% 262/irq/224-spdm_bw: 0% user + 0% kernel
                                                  0% 16076/kworker/u16:4: 0% user + 0% kernel
                                                  0% 576/qmuxd: 0% user + 0% kernel / faults: 32 minor
                                                  0% 5/kworker/0:0H: 0% user + 0% kernel
                                                  0% 8/rcuc/0: 0% user + 0% kernel
                                                  0% 434/android.hardware.wifi@1.0-service: 0% user + 0% kernel
                                                  0% 329/ueventd: 0% user + 0% kernel
                                                  0% 16609/kworker/u17:1: 0% user + 0% kernel
                                                  0% 16864/kworker/u17:4: 0% user + 0% kernel
                                                  0% 17586/kworker/u17:3: 0% user + 0% kernel
                                                  0% 18991/kworker/3:0: 0% user + 0% kernel
                                                  0% 366/servicemanager: 0% user + 0% kernel
                                                  0% 585/zygote: 0% user + 0% kernel / faults: 230 minor
                                                  0% 600/wificond: 0% user + 0% kernel
                                                  0% 991/com.google.android.inputmethod.latin: 0% user + 0% kernel / faults: 43 minor
                                                  0% 4301/com.duapps.recorder: 0% user + 0% kernel / faults: 16 minor
                                                  0% 5757/com.google.android.apps.turbo: 0% user + 0% kernel / faults: 54 minor
                                                  0% 13/rcuc/1: 0% user + 0% kernel
                                                  0% 18/rcuc/2: 0% user + 0% kernel
                                                  0% 25/ksoftirqd/3: 0% user + 0% kernel
                                                  0% 44/irq/51-cpr: 0% user + 0% kernel
                                                  0% 47/ksoftirqd/4: 0% user + 0% kernel
                                                  0% 169/hwrng: 0% user + 0% kernel
                                                  0% 257/nanohub: 0% user + 0% kernel
                                                  0% 420/irq/449-wcd9xxx: 0% user + 0% kernel
                                                  0% 435/healthd: 0% user + 0% kernel
                                                  0% 439/lmkd: 0% user + 0% kernel
                                                  0% 470/ksoftirqd/5: 0% user + 0% kernel
                                                  0% 597/mediaserver: 0% user + 0% kernel / faults: 8 minor
                                                  0% 598/netd: 0% user + 0% kernel / faults: 41 minor
                                                  0% 602/rild: 0% user + 0% kernel / faults: 4 minor
                                                  0% 1151/com.android.phone: 0% user + 0% kernel / faults: 20 minor
                                                  0% 17282/kworker/3:2: 0% user + 0% kernel
                                                  0% 17718/kworker/u17:8: 0% user + 0% kernel
                                                  0% 21042/kworker/u17:2: 0% user + 0% kernel
                                                  0% 45/rcuc/4: 0% user + 0% kernel

     */
}
