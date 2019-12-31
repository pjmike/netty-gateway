package com.pjmike.utils;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/31
 */
public class LockUtil {
    public static final CountDownLatch countdownlatch = new CountDownLatch(1);
}
