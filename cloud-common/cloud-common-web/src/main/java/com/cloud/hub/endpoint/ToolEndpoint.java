package com.cloud.hub.endpoint;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 通用工具
 */
@RequestMapping("tool")
@RestController
public class ToolEndpoint {

    @Value("${system.upload-path:/upload}")
    private String uploadPath;

    @RequestMapping("upload")
    public ResponseResult upload(HttpServletRequest request) {
        return FileUploadUtil.upload(uploadPath, request);
    }

    @RequestMapping("download")
    public void download(String path, HttpServletResponse response) {
        if (StringUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        OutputStream outputStream = null;
        String errorMsg = null;
        try {
            outputStream = response.getOutputStream();
            if (file.exists()) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition","attachment;filename=\"" + file.getName() + "\"");
                response.setHeader("Content-Length", String.valueOf(file.length()));
                InputStream inputStream = new FileInputStream(file);
                byte[] b = new byte[2048];
                while (inputStream.read(b) != -1) {
                    outputStream.write(b);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } else {
                errorMsg = "该文件不存在！";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMsg = "下载失败！";
        } finally {
            if (errorMsg != null) {
                try {
                    response.setHeader("content-type", "text/html;charset=UTF-8");
                    outputStream.write(errorMsg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
