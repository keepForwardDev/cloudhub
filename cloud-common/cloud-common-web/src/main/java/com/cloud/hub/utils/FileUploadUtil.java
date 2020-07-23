package com.cloud.hub.utils;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.consts.ResponseConst;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Random;

public class FileUploadUtil {

    /**
     * 默认上传存于 项目相对路径 upload
     */
    public final static String DEFAULT_SAVE_PATH_NAME = "upload";

    public final static String DEFAULT_DOWNLOAD_PATH = "/tool/download";

    /**
     *
     * @param path 保存的位置
     * @param request
     * @return
     */
    public static ResponseResult upload(String path, HttpServletRequest request) {
        ResponseResult responseResult = new ResponseResult();
        String downLoadUrl = getDownLoadUrl(request);
        if (!ServletFileUpload.isMultipartContent(request)) {
            responseResult.setMsg("请上传文件！");
            return responseResult;
        }
        if (StringUtils.isEmpty(path)) {
            ServletContext application = request.getSession().getServletContext();
            path = application.getRealPath("/") + DEFAULT_SAVE_PATH_NAME;
        }
        File uploadDir = new File(path);
        String fileName = null;
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> itr = mRequest.getFileNames();
            while (itr.hasNext()) {
                MultipartFile mFile = mRequest.getFile(itr.next());
                fileName = mFile.getOriginalFilename();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if(fileExt.equals("blob")){
                    fileExt="jpg";
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String newFileName = fileName + "_" + new Random().nextInt(1000) + "." + fileExt;
                File uploadedFile = new File(path, newFileName);
                if (!uploadedFile.exists()) {
                    FileCopyUtils.copy(mFile.getInputStream(), new FileOutputStream(uploadedFile));
                    responseResult.setCode(ResponseConst.CODE_SUCCESS);
                    responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
                    // 返回全路径
                    responseResult.setData(downLoadUrl + URLEncoder.encode(uploadedFile.getAbsolutePath(), "UTF-8"));
                    responseResult.setExtraData(fileName);
                } else {
                    responseResult.setMsg("当前文件名已存在!");
                }
                break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    public static String getDownLoadUrl(HttpServletRequest request) {
        String viewUrl = request.getScheme()+"://"+ request.getServerName();
        if (request.getServerPort() != 80) {
            viewUrl += ":" + request.getServerPort();
        }
        return viewUrl + DEFAULT_DOWNLOAD_PATH + "?path=";
    }
}
