package com.cyc.bookweb.context;

public class BlackListThreadLoacal {
  private static final ThreadLocal<Boolean> flagBlackIp = new ThreadLocal<>();

  public static boolean getFlagBlackIp() {
    return flagBlackIp.get() == null ? false : flagBlackIp.get();
  }

  public static void setFlagBlackIp(boolean b) {
    flagBlackIp.set(b);
  }

}
