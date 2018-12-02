package io.github.itfinally.jvm.monitors;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import static java.lang.System.currentTimeMillis;

public class ThreadMonitor {
  public void collecteThreadInfo() {
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    long currentMillis = currentTimeMillis();

    threadMXBean.getTotalStartedThreadCount();
  }
}
