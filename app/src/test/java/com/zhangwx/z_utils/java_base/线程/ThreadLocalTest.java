package com.zhangwx.z_utils.java_base.线程;


public class ThreadLocalTest {

  private static ThreadLocal<String> local = new ThreadLocal<String>() {
    @Override
    protected String initialValue() {
      return "One";
    }
  };

  public static void main(String[] args) {
    local.set("Main");

    new Thread() {
      @Override
      public void run() {
        super.run();
        local.set("Two");
        System.out.println("set Two   "  + local.get());
      }
    }.start();

    new Thread() {
      @Override
      public void run() {
        super.run();
        local.set("Three");
        System.out.println("set Three   " + local.get());
      }
    }.start();

    new Thread() {
      @Override
      public void run() {
        super.run();
        System.out.println("set null   " + local.get());
      }
    }.start();

    System.out.println(local.get());
  }
}
