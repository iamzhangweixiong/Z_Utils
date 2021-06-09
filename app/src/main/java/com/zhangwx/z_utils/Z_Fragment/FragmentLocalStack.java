package com.zhangwx.z_utils.Z_Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FragmentLocalStack {

    private static final Map<String, Stack<Fragment>> mStackMap = new HashMap<>();

    public static Fragment pop(String stackTag) {
        final Stack<Fragment> fragmentStack = mStackMap.get(stackTag);
        if (fragmentStack == null || fragmentStack.empty()) {
            return null;
        }
        return fragmentStack.pop();
    }

    public static boolean popAndRemove(String stackTag, FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        final Stack<Fragment> fragmentStack = mStackMap.get(stackTag);
        if (fragmentStack == null || fragmentStack.empty()) {
            return false;
        }
        final Fragment fragment = fragmentStack.pop();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        return true;
    }

    public static void push(String stackTag, Fragment fragment) {
        if (fragment == null) {
            return;
        }
        Stack<Fragment> fragmentStack = mStackMap.get(stackTag);
        if (fragmentStack == null) {
            fragmentStack = new Stack<>();
            mStackMap.put(stackTag, fragmentStack);
        }
        fragmentStack.push(fragment);
    }

    public static void clearStack(String stackTag, FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        final Stack<Fragment> fragmentStack = mStackMap.get(stackTag);
        if (fragmentStack == null || fragmentStack.empty()) {
            return;
        }
        for (int i = 0; i <= fragmentStack.size(); i++) {
            final Fragment fragment = fragmentStack.pop();
            if (fragment == null) {
                continue;
            }
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    public static void release() {
        mStackMap.clear();
    }
}
