package com.zhangwx.z_utils.Z_Widget.WebImageView.cache;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruHashMap<K, V> {
	private static Method mEldestMethod;

	static {
		try {
			final Class[] empty = new Class[] {};
			mEldestMethod = LinkedHashMap.class.getMethod("eldest", empty);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static <K, V> Map.Entry<K, V> getEldest(LinkedHashMap<K, V> map) {
		try {
			if (mEldestMethod != null)
				return (Map.Entry<K, V>) mEldestMethod.invoke(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
		return it.hasNext() ? it.next() : null;
	}

	private final LinkedHashMap<K, V> mMap;
	private final long mMaxSize;
	private long mSize;

	public LruHashMap(long maxSize) {
		mMap = new LinkedHashMap<>(4, 0.75f, true);
		mMaxSize = maxSize;
	}

	public int count() {
		synchronized (mMap) {
			return mMap.size();
		}
	}

	public Map.Entry<K, V> eldest() {
		synchronized (mMap) {
			return getEldest(mMap);
		}
	}

	public boolean isEmpty() {
		synchronized (mMap) {
			return mMap.isEmpty();
		}
	}

	public V get(K key) {
		synchronized (mMap) {
			return mMap.get(key);
		}
	}

	public V put(K key, V value) {
		final int size = value != null ? sizeOf(key, value) : 0;
		trimToSize(mMaxSize - size);

		final V prev;
		synchronized (mMap) {
			prev = mMap.put(key, value);
			mMap.get(key); // for access order
			if (value != null)
				mSize += size;
			if (prev != null)
				mSize -= sizeOf(key, prev);
		}
		if (prev != null)
			onRemoved(key, prev);
		return prev;
	}

	public V remove(K key) {
		final V prev;
		synchronized (mMap) {
			prev = mMap.remove(key);
			if (prev != null)
				mSize -= sizeOf(key, prev);
		}
		if (prev != null)
			onRemoved(key, prev);
		return prev;
	}

	public void clear() {
		trimToSize(0);
	}

	public long size() {
		return mSize;
	}

	public long trimToSize(long maxSize) {
		long trimmed = 0;
		for (;;) {
			final Map.Entry<K, V> entry;
			synchronized (mMap) {
				entry = mSize > maxSize ? getEldest(mMap) : null;
			}
			if (entry != null) {
				final K key = entry.getKey();
				final V value = entry.getValue();
				int size = value != null ? sizeOf(key, value) : 0;
				remove(entry.getKey());
				entry.setValue(null); // for garbage collector
				trimmed += size;
			} else {
				break;
			}
		}
		return trimmed;
	}

	protected void onRemoved(K key, V value) {
	}

	protected int sizeOf(K key, V value) {
		return 1;
	}
}
