package cn.com.bbut.iy.itemmaster.util;

import java.lang.reflect.Method;

import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.cache.interceptor.KeyGenerator;

import lombok.SneakyThrows;

/**
 * @author shiy
 */
public class IyCacheKeyGenerator implements KeyGenerator {
	private static final int NO_PARAM_KEY = 0;
	private static final int NULL_PARAM_KEY = -1;

	/** hash function */

	@SneakyThrows
	@Override
	public Object generate(Object target, Method method, Object... params) {
		if (params == null || params.length == 0) {
			return NO_PARAM_KEY;
		}
		if (params.length == 1) {
			return (params[0] == null ? NULL_PARAM_KEY
					: MurmurHash3.hash64(params[0].toString().getBytes("UTF-8")));
		}
		long hashCode = 17L;
		for (Object object : params) {
			hashCode = 31L * hashCode + (object == null ? NULL_PARAM_KEY
					: MurmurHash3.hash64(params[0].toString().getBytes("UTF-8")));
		}
		return hashCode;
	}

}
