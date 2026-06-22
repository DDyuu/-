package com.proj4.controller;

import cn.hutool.core.io.FileUtil;
import com.proj4.common.Result;
import com.proj4.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final String filePath = System.getProperty("user.dir").replace("\\springboot", "") + "/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);  //创建files文件
        }
        //文件存储的完整路径
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        //返回一个网络链接http://localhost:8080/files/download/xxxx.png
        String url = "http://localhost:8080/files/download/" + fileName;
        return Result.success(url);
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response){
        try {
            // 根据文件后缀设置正确的 Content-Type
            String contentType = "application/octet-stream";
            if (filename != null) {
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                switch (ext) {
                    case "jpg":
                    case "jpeg":
                        contentType = "image/jpeg";
                        break;
                    case "png":
                        contentType = "image/png";
                        break;
                    case "gif":
                        contentType = "image/gif";
                        break;
                    case "bmp":
                        contentType = "image/bmp";
                        break;
                    case "webp":
                        contentType = "image/webp";
                        break;
                    case "svg":
                        contentType = "image/svg+xml";
                        break;
                    case "ico":
                        contentType = "image/x-icon";
                        break;
                }
            }
            // 使用 inline 而非 attachment，让浏览器直接显示图片而非下载
            response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            response.setContentType(contentType);
            OutputStream os = response.getOutputStream();
            String realPath = filePath + filename;
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e){
            e.printStackTrace();
            throw new CustomException("500", "文件下载失败");
        }
    }

    @PostMapping("wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);  //创建files文件
        }
        //文件存储的完整路径
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        //返回一个网络链接http://localhost:8080/files/download/xxxx.png
        String url = "http://localhost:8080/files/download/" + fileName;
        //wangEditor上传图片成功后,需要返回的参数
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
        resMap.put("errno", 0);
        resMap.put("data", list);
        return resMap;
    }
}
