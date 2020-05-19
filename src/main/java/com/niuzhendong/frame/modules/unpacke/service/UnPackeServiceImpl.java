package com.niuzhendong.frame.modules.unpacke.service;

import com.niuzhendong.frame.common.enums.FileTypeEnum;
import com.niuzhendong.frame.common.util.UnPackeUtil;
import com.niuzhendong.frame.common.util.UrlToImageUtil;
import com.niuzhendong.frame.common.util.returns.ReturnVO;
import com.niuzhendong.frame.common.util.returns.ReturnVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author niuzhendong
 * @package_name com.niuzhendong.frame.modules.unpacke.service
 * @project_name frame
 * @date 2019/10/14
 */
@Service
@Slf4j
public class UnPackeServiceImpl {
    /**
     * 上传素材包
     * @param file
     * @param tpaId
     * @return
     */
    public ReturnVO uploadResource(MultipartFile file, String tpaId) {
        if (null == file) {
            return ReturnVOUtils.error(500,"请上传压缩文件!");
        }

        String fileContentType = file.getContentType();

        String destPath = new File("").getAbsolutePath();
        destPath = destPath.substring(0,destPath.indexOf(File.separator))+File.separator+"litchifactory";
        //文件夹添加时间
        Calendar calendar = Calendar.getInstance();
        destPath+=File.separator+calendar.get(Calendar.YEAR)+File.separator+(calendar.get(Calendar.MONTH)+1)+File.separator+calendar.get(Calendar.DATE);
        //新建文件夹
        File destFile = new File(destPath);
        if(!destFile.exists()) {
            destFile.mkdirs();
        }

        String l = calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DATE) + "" + System.currentTimeMillis();
        //将压缩包保存在指定路径
        String packFilePath = destPath + File.separator;//file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."))
        if (FileTypeEnum.FILE_TYPE_ZIP.type.equals(fileContentType)) {
            //zip解压缩处理
            packFilePath += l + FileTypeEnum.FILE_TYPE_ZIP.fileStufix;
        } else if (FileTypeEnum.FILE_TYPE_RAR.type.equals(fileContentType)) {
            //rar解压缩处理
            packFilePath += l + FileTypeEnum.FILE_TYPE_RAR.fileStufix;
        } else {
            return ReturnVOUtils.error(500,"上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        }
        File saveFile = new File(packFilePath);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            log.error("zip file save to " + destPath + " error", e.getMessage(), e);
            return ReturnVOUtils.error(500,"保存压缩文件到:" + destPath + " 失败!");
        }

        return ReturnVOUtils.success("上传成功");
    }

    public void createApplication4(String resouceUrl, HttpServletRequest request) {

        //解压素材包，将图标放进去
        if(!StringUtils.isEmpty(resouceUrl)) {
            String resourceUrl = resouceUrl;
            int index = resourceUrl.lastIndexOf(".");
            String destPath = resourceUrl.substring(0,index);
            String fileStufix = resourceUrl.substring(index);
            File saveFile = new File(resourceUrl);

            if (FileTypeEnum.FILE_TYPE_ZIP.fileStufix.equals(fileStufix)) {
                //zip压缩包
                UnPackeUtil.unPackZip(saveFile, "", destPath);
            } else if(FileTypeEnum.FILE_TYPE_RAR.fileStufix.equals(fileStufix)){
                //rar压缩包
                UnPackeUtil.unPackRar(saveFile, destPath);
            }

            //换文件
            Map<String, String> iconMap = new HashMap<>();

            if(!ObjectUtils.isEmpty(iconMap)) {
                Set<String> keySet = iconMap.keySet();
                if (keySet.size() > 0) {
                    for (String iconName : keySet) {
                        String filePath = destPath + File.separator+ "tabIcon" + File.separator + iconName + ".png";
                        UrlToImageUtil.saveToFile(iconMap.get(iconName)+"", filePath);
                    }
                }
            }

            //压缩
            FileOutputStream file1 = null;
            try {
                file1 = new FileOutputStream(new File(resourceUrl));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UnPackeUtil.toZip(destPath,file1,true);

            //删除解压的文件夹
            File file2 = new File(destPath);
            deleteFile(file2);
        }
    }

    /**
     * 如果是文件 ==》直接删除
     * 如果是目录 ==》必须先删除里面每一层目录里的所有文件，最后才能删除外层的目录
     *              原因：不为空的话 删不了
     */
    public static void deleteFile(File file) {
        //判断路径是否存在
        if(file.exists()) {
            //boolean isFile():测试此抽象路径名表示的文件是否是一个标准文件。
            if(file.isFile()){
                file.delete();
            }else{
                //不是文件，对于文件夹的操作
                //保存 路径D:/1/新建文件夹2  下的所有的文件和文件夹到listFiles数组中
                //listFiles方法：返回file路径下所有文件和文件夹的绝对路径
                File[] listFiles = file.listFiles();
                for (File file2 : listFiles) {
                    /**
                     * 递归作用：由外到内先一层一层删除里面的文件 再从最内层 反过来删除文件夹
                     *    注意：此时的文件夹在上一步的操作之后，里面的文件内容已全部删除
                     *         所以每一层的文件夹都是空的  ==》最后就可以直接删除了
                     */
                    deleteFile(file2);
                }
            }
            file.delete();
        }else {
            System.out.println("该file路径不存在！！");
        }
    }
}
