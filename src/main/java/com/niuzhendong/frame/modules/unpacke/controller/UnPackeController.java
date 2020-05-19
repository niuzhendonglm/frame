package com.niuzhendong.frame.modules.unpacke.controller;

import com.niuzhendong.frame.common.util.Base64Util;
import com.niuzhendong.frame.common.util.returns.ReturnVO;
import com.niuzhendong.frame.modules.unpacke.service.UnPackeServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author niuzhendong
 * @package_name com.niuzhendong.frame.modules.unpacke.service.controller
 * @project_name frame
 * @date 2019/10/14
 */
@RestController
public class UnPackeController {
    @Autowired
    UnPackeServiceImpl unPackeService;

    /**
     * 上传素材包
     *
     * @param file
     * @param tpaId
     * @return
     */
    @ApiOperation(value = "上传素材包", notes = "上传素材包")
    @PostMapping("application/upload")
    public ReturnVO uploadResource(MultipartFile file, String tpaId) {
        return unPackeService.uploadResource(file, tpaId);
    }

    /**
     * config.json 素材包下载
     *
     * @param url
     * @param response
     * @return
     */
    @GetMapping("sendResource/{url}")
    @ApiIgnore
    public HttpServletResponse sendResource(@PathVariable("url") String url, HttpServletResponse response) {
        try {
            String resourceUrl = Base64Util.decodeData(url);

            // path是指欲下载的文件的路径。
            File file = new File(resourceUrl);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(resourceUrl));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
