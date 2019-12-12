package com.etstc.ekits.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 缓存工具类(软引用方式，OOM前会自动清除部分缓存) */
public class MemCache {
	private static Map<Object, SoftReference<CacheObject<?>>> cacheMap;

	private static MemCache cache;

	private MemCache() {
		cacheMap = new ConcurrentHashMap<>();
		// 守护线程清除过期
		Thread cleanerThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					cacheMap.entrySet().removeIf(entry -> Optional.ofNullable(entry.getValue()).map(SoftReference::get)
							.map(CacheObject::isExpired).orElse(false));
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		cleanerThread.setDaemon(true);
		cleanerThread.start();
	}

	/**
	 * 缓存(不过期)
	 * 
	 * @param key   键
	 * @param value 值
	 */
	public <K, V> void put(K key, V value) {
		this.put(key, value, 0);// 默认不过期
	}

	/**
	 * 缓存
	 * 
	 * @param key            键
	 * @param value          值
	 * @param periodInMillis 过期时间(小于0:移除键对应值,0:不过期,大于0:过期时间)
	 */
	public <K, V> void put(K key, V value, long periodInMillis) {
		if (key == null) {
			return;
		}
		if (value == null || periodInMillis < 0) {
			cacheMap.remove(key);
		} else {
			long expiryTime = Long.MAX_VALUE;
			if (periodInMillis > 0) { //
				expiryTime = System.currentTimeMillis() + periodInMillis;
			}
			SoftReference<CacheObject<?>> sr = new SoftReference<>(new CacheObject<V>(value, expiryTime));
			cacheMap.put(key, sr);
		}
	}

	/** 获取缓存 */
	public <K, V> V get(K key) {
		@SuppressWarnings("unchecked")
		V value = (V) Optional.ofNullable(cacheMap.get(key)).map(SoftReference::get)
				.filter(cacheObject -> !cacheObject.isExpired()).map(CacheObject::getValue).orElse(null);
		return value;
	}

	/** 移除缓存 */
	public <K, V> V remove(K key) {
		V value = get(key);
		cacheMap.remove(key);
		return value;
	}

	/** 清除缓存 */
	public void clear() {
		cacheMap.clear();
	}

	/** 单例 */
	public static MemCache getInstance() {
		if (cache == null) {
			synchronized (MemCache.class) {
				if (cache == null) {
					cache = new MemCache();
				}
			}
		}
		return cache;
	}

	/** 缓存对象 */
	private static class CacheObject<T> {

		private T value;
		private long expiryTime;

		private CacheObject(T value, long expiryTime) {
			this.value = value;
			this.expiryTime = expiryTime;
		}

		boolean isExpired() {
			return System.currentTimeMillis() > expiryTime;
		}

		public T getValue() {
			return value;
		}
	}
}
