package com.sso.rpc;

import com.alibaba.fastjson.JSONObject;

import java.io.File;

/**
 * 
 * @ClassName: QiniuApi 
 * @Description: 七牛文件处理
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:43:12 
 */
public interface QiniuApi {
    /**
     * 上传本地临时图片至云端 , 保存记录在本地表中
     * @param localImagePath 本地临时图片相对路径
     * @param tableName 图片所在表名
     * @param fieldName 图片据在字段名
     * @param recId  图片所在记录id
     * @return  云端访问url
     */
    String uploadImageToYun(String localImagePath, String tableName, String fieldName, String recId) throws Exception;

    /**
     * 上传本地临时图片至云端 , 保存记录在本地表中
     * @param localImagePath 本地临时图片相对路径
     * @param tableName 图片所在表名
     * @param fieldName 图片据在字段名
     * @param recId  图片所在记录id
     * @param zipImage  压缩图片
     * @return  云端访问url
     */
    String uploadImageToYun(String localImagePath, String tableName, String fieldName, String recId, boolean zipImage) throws Exception;
    /**
     * 直接上传图片或其它文件至云
     * @param file 图片或文件
     * @param key
     * @return
     * @throws Exception
     */
    String uploadToYun(File file, String key) throws Exception;

    /**
     * 上传本地临时图片至云端 , 保存记录在本地表中,并且将文件转为pof
     * @param localImagePath 本地临时图片相对路径
     * @param tableName 图片所在表名
     * @param fieldName 图片据在字段名
     * @param recId  图片所在记录id
     * @return path 文件本身的云路径
     * @return onlinePath pdf文件的云路径
     */
    JSONObject uploadImageToYunAndTansformPdf(String localImagePath, String tableName, String fieldName, String recId) throws Exception;

    /**
     * 上传本地临时图片至云端 , 保存记录在本地表中,并且将文件转为pof
     *  是否是本地base的文件，这里专门为了pcos中的能源等需要上传文件作处理
     * @param absolutePath ovu-pcos中的文件
     * @param tableName 图片所在表名
     * @param fieldName 图片据在字段名
     * @param recId  图片所在记录id
     * @return  云端访问url
     */
    JSONObject uploadImageToYunAndTansformPdfNoLocal(String absolutePath, String tableName, String fieldName, String recId) throws Exception;
}
