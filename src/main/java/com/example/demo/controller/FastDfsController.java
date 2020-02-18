package com.example.demo.controller;

import com.example.demo.utils.FastDFSClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yy
 */

@RestController
@RequestMapping("/fdfs")
public class FastDfsController {

    @Autowired
    private FastDFSClient fdfsClient;


    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload")
    public Map<String,Object> upload(MultipartFile file) throws Exception{

        Map<String,Object> result = new HashMap<>();

        long size = file.getSize();
        System.out.println("文件大小为："+size);
        if(size>10*1024*1024){
            result.put("code", 500);
            result.put("msg", "上传失败,文件最大为10MB");
        }else {
            String url = fdfsClient.uploadFile(file);
            result.put("code", 200);
            result.put("msg", "上传成功");
            result.put("url", url);
        }
        return result;
    }

    /**
     * 文件下载
     * @param fileUrl  url 开头从组名开始
     * @param response
     * @throws Exception
     */
    @RequestMapping("/download")
    public void  download(String fileUrl, HttpServletResponse response) throws Exception{

        byte[] data = fdfsClient.download(fileUrl);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("test.jpg", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }

}
