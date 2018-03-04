package com.core.base.mybatis.util;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * @ClassName: MybatisUtil 
 * @Description: mybatis xml文件里 test参数校验, 调用方式:<if test="@com.core.base.mybatis.util.MybatisUtil@isNotEmpty(userId)"></if>
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:27:44 
 */
public class MybatisUtil {
	/**
	 * 
	 * @Title: isEmpty 
	 * @Description: 校验属性是否为空, 
	 * @author 张龙龙
	 * @date 2018年3月3日 下午2:29:21
	 * @param @param obj 需要校验的属性(属性可以是:Collection，Array,String,Map)
	 * @param @return     
	 * @return boolean     
	 * @throws 
	 */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            return StringUtils.isEmpty((String) obj);
        } else if (obj instanceof Collection<?>) {
            return CollectionUtils.isEmpty((Collection<?>) obj);
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Map) {
            return MapUtils.isEmpty((Map) obj);
        }

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
