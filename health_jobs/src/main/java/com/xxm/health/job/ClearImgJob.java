package com.xxm.health.job;

import com.xxm.health.constant.RedisConstant;
import com.xxm.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @Program: IntelliJ IDEA health_parent
 * @Description: TODO
 * @Author: Mr Liu
 * @Creed: Talk is cheap,show me the code
 * @CreateDate: 2019-10-27 15:44:29 周日
 * @LastModifyDate:
 * @LastModifyBy:
 * @Version: V1.0
 */
public class ClearImgJob {
    @Autowired
    JedisPool jedisPool;

    public void executeJob() {
        /*计算setmealPicResources与setmealPicDbResources集合的差值，清理图片*/
        /*Redis下的方法sdiff()能够自动过滤相同的元素，留下不同元素的集合*/
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String imgName = iterator.next();
            System.out.println("删除的图片名称为：" + imgName);
            QiniuUtils.deleteFileFromQiniu(imgName);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, imgName);
        }
    }
}
