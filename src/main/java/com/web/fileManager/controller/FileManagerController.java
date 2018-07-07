package com.web.fileManager.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.util.FileUtils;
import com.util.ImageUtils;
import com.util.SysLog;
import com.web.fileManager.entity.ActOwnFile;
import com.web.fileManager.entity.ActStudyFile;
import com.web.fileManager.service.IActOwnFileService;
import com.web.fileManager.service.IActStudyFileService;


@Controller
@RequestMapping("/fileManager")
public class FileManagerController {
	@Autowired
	private IActOwnFileService actOwnFileService;
	
	@Autowired
	private IActStudyFileService actStudyFileService;
	
	/**
	 * 
	 * @Title: studyFileUploadByContent 
	 * @Description: 以内容形式保存学习过程中的文件， 针对富文本与思维导图。【注】此时文件已经存在，这里是更新
	 * @author 张龙龙
	 * @date 2018年3月18日 上午11:19:27
	 * @param @param request
	 * @param @return     
	 * @return String     
	 * @throws 
	 */
	@RequestMapping(value="/studyFileUploadByContent")  
    public @ResponseBody void studyFileUploadByContent(HttpServletRequest request, HttpServletResponse response){
		JSONObject result = new JSONObject();
		
		PrintWriter res = null;
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			res = response.getWriter();
			
			String userId = request.getParameter("userId");
			String fileId = request.getParameter("fileId");
			String fileName = request.getParameter("fileName");
			//String createType = request.getParameter("createType");
			String fileStr = request.getParameter("fileStr");
			
			if(StringUtils.isBlank(userId) || StringUtils.isBlank(fileId)){
				result.put("errorMsg", "缺少用户id【userId】或者文件id【fileId】，不能保存!");
				res.write(result.toString());
			}else{
				ActStudyFile actStudyFile = actStudyFileService.selectByPrimaryKey(fileId);
				if(actStudyFile == null){
					result.put("errorMsg", "找不到目标文件，不能保存!");
					res.write(result.toString());
				}else{
					//如果不是思维导图， 默认就是富文本内容
					if(!"jm".equals(actStudyFile.getFiletype().toLowerCase())){
						fileStr = "<!DOCTYPE html><html><head><meta charset='utf-8' /></head><body>"+fileStr+"</body><html>";
					}
					//保存文件内容
					OutputStream out = new FileOutputStream(new File(FileUtils.getFileRootDir() + File.separator + actStudyFile.getFilepath()));
					OutputStreamWriter write = new OutputStreamWriter(out, "UTF-8");
					write.write(fileStr);
					write.flush();
					write.close();
					
					int size = fileStr.getBytes().length;
					if(size <= 1024){
						actStudyFile.setFilesize(size + "B");
					}else if(size <= 1024*1024){
						actStudyFile.setFilesize(size / 1024 + "KB");
					}else if(size <= 1024*1024*1024){
						actStudyFile.setFilesize(size/(1024*1024) + "M");
					}else{
						actStudyFile.setFilesize(size/(1024*1024*1024) + "G");
					}
					actStudyFile.setLastupdatetime(actStudyFile.getDateStr(null));
					if(StringUtils.isNoneBlank(fileName) && (fileName.indexOf(".html") > 0 || fileName.indexOf(".jm") > 0)){
						actStudyFile.setFilename(fileName);
					}
					//更新记录
					actStudyFileService.updateByPrimaryKeySelective(actStudyFile);
					
					res.write(result.toString());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.put("errorMsg", "保存文件时出现问题！");
			res.write(result.toString());
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	/**
	 * 
	 * @Title: studyFileUpload 
	 * @Description: 以文件流的方式上传学习文件， 针对office文档类的保存。【注】此时文件已经存在，这里是更新
	 * @author 张龙龙
	 * @date 2018年3月18日 上午11:42:45
	 * @param @param request
	 * @param @return     
	 * @return String     
	 * @throws 
	 */
	@RequestMapping(value="/studyFileUpload")  
    public @ResponseBody String studyFileUpload(HttpServletRequest request){
		Map<String, String> rMap = new HashMap<String, String>();
		try {
			
		}catch(Exception e){
			e.printStackTrace();
			rMap.put("errorMsg", "保存文件时出现问题！");
		}
		
		return new Gson().toJson(rMap);
	}
	
	/**
	 * 
	 * @Title: ownFileUploadByContent 
	 * @Description: 以内容形式保存文件， 针对富文本与思维导图。【注】此时文件不存在，这里是insert
	 * @author 张龙龙
	 * @date 2018年3月18日 上午11:45:32
	 * @param @param request
	 * @param @return     
	 * @return String     
	 * @throws 
	 */
	@RequestMapping(value="/ownFileUploadByContent")  
    public @ResponseBody void ownFileUploadByContent(HttpServletRequest request, HttpServletResponse response){
		String errorMsg = null, fileSize = "0", relativePath = null;
		
		PrintWriter res = null;
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			res = response.getWriter();
			
			String userId = request.getParameter("userId");
			String fileId = request.getParameter("fileId");
			String fileName = request.getParameter("fileName");
			String fileStr = request.getParameter("fileStr");
			
			if(StringUtils.isBlank(userId) || StringUtils.isBlank(fileName)){
				errorMsg =  "缺少用户id或文件名，不能保存!";
			}else{
				String last = null;
				UUID uuid = UUID.randomUUID();
				if(StringUtils.isBlank(fileId)){//create new
					if(fileName != null && fileName.endsWith(".jm")){
						last = "jm";
					}else{
						fileStr = "<!DOCTYPE html><html><head><meta charset='utf-8' /></head><body>"+fileStr+"</body><html>";
						last = "html";
					}
					relativePath = FileUtils.getFileRelativePath(FileUtils.getFileRootDir(), last, userId, uuid + "." + last, "own");
					if(relativePath == null){
						errorMsg = "抱歉，系统不允许格式为【"+last+"】的文件上传，请选择上传其他常规的文件类型！";
					}
				}else{//edit
					ActOwnFile actOwnFile = actOwnFileService.selectByPrimaryKey(fileId);
					if(actOwnFile == null){
						errorMsg =  "找不到目标文件，不能保存!";
					}else{
						last = actOwnFile.getFiletype().toLowerCase();
						//如果不是思维导图， 默认就是富文本内容
						if(!"jm".equals(last)){
							fileStr = "<!DOCTYPE html><html><head><meta charset='utf-8' /></head><body>"+fileStr+"</body><html>";
						}
						relativePath = actOwnFile.getFilepath();
					}
				}
				
				if(errorMsg == null){
					int size = fileStr.getBytes().length;
					if(size <= 1024){
						fileSize = size + "B";
					}else if(size <= 1024*1024){
						fileSize = size / 1024 + "KB";
					}else if(size <= 1024*1024*1024){
						fileSize = size/(1024*1024) + "M";
					}else{
						fileSize = size/(1024*1024*1024) + "G";
					}
					
					//保存文件内容
					OutputStream out = new FileOutputStream(new File(FileUtils.getFileRootDir() + File.separator + relativePath));
					OutputStreamWriter write = new OutputStreamWriter(out, "UTF-8");
					write.write(fileStr);
					write.flush();
					write.close();
				}
				
				if(errorMsg != null){
					JSONObject errResult = new JSONObject();
					errResult.put("errorMsg", errorMsg);
					res.write(errResult.toString());
					//return errResult.toString();
				}else{
					if(StringUtils.isBlank(fileId)){//create new
						ActOwnFile actOwnFile = new ActOwnFile(null, relativePath, fileName, userId, fileSize, null, last);
						actOwnFile.setFileid(uuid.toString());
						
						if(actOwnFileService.insertSelective(actOwnFile) > 0){
							SysLog.info(userId, "", new Gson().toJson(actOwnFile));
							JSONObject result = new JSONObject();
							result.put("fileId", actOwnFile.getFileid());
							result.put("filePath", actOwnFile.getFilepath());
							result.put("fileSize", actOwnFile.getFilesize());
							result.put("fileType", actOwnFile.getFiletype());
							result.put("fileName", actOwnFile.getFilename());
							
							res.write(result.toString());
						}else{
							JSONObject errResult = new JSONObject();
							errResult.put("errorMsg", "抱歉，你上传的文件的记录保存失败，请尝试重新上传！");
							SysLog.error(userId, "", errResult.getString("errorMsg"));
							
							res.write(errResult.toString());
						}
					}else{//update
						ActOwnFile actOwnFile = actOwnFileService.selectByPrimaryKey(fileId);
						actOwnFile.setFileid(fileId);
						actOwnFile.setFilesize(fileSize);
						actOwnFile.setFilename(fileName);
						actOwnFile.setLastupdatetime(actOwnFile.getDateStr(null));
						
						if(actOwnFileService.updateByPrimaryKeySelective(actOwnFile) > 0){
							SysLog.info(userId, "", new Gson().toJson(actOwnFile));
							JSONObject result = new JSONObject();
							result.put("fileId", actOwnFile.getFileid());
							result.put("filePath", actOwnFile.getFilepath());
							result.put("fileSize", actOwnFile.getFilesize());
							result.put("fileType", actOwnFile.getFiletype());
							result.put("fileName", actOwnFile.getFilename());
							
							res.write(result.toString());
						}else{
							JSONObject errResult = new JSONObject();
							errResult.put("errorMsg", "抱歉，你保存的文件的记录保存失败，请尝试重新保存！");
							SysLog.error(userId, "", errResult.getString("errorMsg"));
							
							res.write(errResult.toString());
						}

					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			JSONObject errResult = new JSONObject();
			errResult.put("errorMsg", "保存文件时出现问题！");
			res.write(errResult.toString());
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	/**
	 * 
	 * @Title: ownFileUpload 
	 * @Description: 以文件流的方式上传文件， 针对office文档类的保存。【注】此时文件不存在，这里是insert
	 * @author 张龙龙
	 * @date 2018年3月18日 上午11:46:41
	 * @param @param request
	 * @param @return     
	 * @return String     
	 * @throws 
	 */
	@RequestMapping(value="/ownFileUpload")  
    public @ResponseBody void ownFileUpload(@RequestParam("file") MultipartFile sourceFile, HttpServletRequest request, HttpServletResponse response){
		Map<String, String> rMap = new HashMap<String, String>();
		List<String> imageZooms = new ArrayList<String>(3);
		UUID uuid = UUID.randomUUID();
		String errorMsg = null, fileSize = "0", relativePath = null;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter res = null;
		
		try {
			res = response.getWriter();
			
			String fileRootDir = FileUtils.getFileRootDir();
			File file_cache = new File(fileRootDir);
			if (!file_cache.exists()) {  
				file_cache.mkdirs();  
	        }
			
			long size = sourceFile.getSize();
			String fileName = sourceFile.getOriginalFilename();
			String fileId = request.getParameter("fileId");
			String userId = request.getParameter("userId");
			String imageSizes = request.getParameter("imageSizes");
			//System.out.println(size + " " +fileName + " " + userId + " " + createType);
			int lastIndex = fileName.lastIndexOf(".");
			String last = fileName.substring(lastIndex+1).toLowerCase();//文件后缀
			 
			//检查userId
			if(StringUtils.isBlank(userId)){
				errorMsg = "上传请求里缺少用户id属性，不能上传!";
			}
			
			if(errorMsg == null){
				if(size <= 1024)
					fileSize = size + "B";
				else if(size <= 1024*1024)
					fileSize = size / 1024 + "KB";
				else if(size <= 1024*1024*1024)
					fileSize = size/(1024*1024) + "M";
				else
					fileSize = size/(1024*1024*1024) + "G";
				
				if(StringUtils.isBlank(fileId)){//create new
					relativePath = FileUtils.getFileRelativePath(fileRootDir, last, userId, uuid + "." + last, "own");
					if(relativePath == null){
						errorMsg = "抱歉，系统不允许格式为【"+last+"】的文件上传，请选择上传其他常规的文件类型";
					}else if(relativePath.contains("videos") && !"3pg".equals(last.toLowerCase()) && !"mp4".equals(last.toLowerCase())){ //临时处理
						errorMsg = "抱歉，暂时不允许上传"+last+"格式的视频文件，请将视频文件转换为3pg或mp4格式后再上传！";
					}
				}else{
					ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileId);
					if(actOwnFile != null){
						relativePath = actOwnFile.getFilepath();
						last = actOwnFile.getFiletype();//文件后缀
					}else{
						errorMsg = "找不到要更新的文件！";
					}
				}
				
				if(errorMsg == null){
					File targetFile = new File(fileRootDir + File.separator + relativePath);
					sourceFile.transferTo(targetFile);
					
					if(StringUtils.isNoneBlank(imageSizes)){//图片裁剪
						String fileTypeDir = FileUtils.getFileTypeDir(last);
						if(fileTypeDir != null && "images".equals(fileTypeDir)){
							String[] imageSizeArr = imageSizes.split("_");
							for(int iSize = 0; iSize < imageSizeArr.length;iSize ++){
								String[] currSizeArr = imageSizeArr[iSize].split("m");
								if(currSizeArr.length != 2){
									errorMsg = "sizeError：裁剪图片的格式错误！";
									break;
								}else{
									int x = Integer.parseInt(currSizeArr[0]);
									int y = Integer.parseInt(currSizeArr[0]);
									
									int cutLastIndex = relativePath.indexOf(fileTypeDir);
									StringBuffer sCutImagePath = new StringBuffer();
									sCutImagePath.append(relativePath.substring(0, cutLastIndex-1)).append(File.separator).append("cutImages");
									FileUtils.makeFileDir(fileRootDir, sCutImagePath.toString());
									
									sCutImagePath.append(File.separator).append("zoom_").append(x).append("m").append(y).append(".").append(last);
									ImageUtils.zoomImage(fileRootDir + File.separator + relativePath, fileRootDir + File.separator + sCutImagePath.toString(), x, y);
									imageZooms.add(sCutImagePath.toString());
								}
							}
						}
					}
				}
			}
			
			if(errorMsg != null){
				JSONObject errResult = new JSONObject();
				errResult.put("errorMsg", errorMsg);
				res.write(errResult.toString());
				//return errResult.toString();
			}else{
				if(StringUtils.isBlank(fileId)){//create new
					ActOwnFile actOwnFile = new ActOwnFile(null, relativePath, sourceFile.getOriginalFilename(), userId, fileSize, null, last);
					actOwnFile.setFileid(uuid.toString());
					if(imageZooms.size() > 0){
						actOwnFile.setImagezooms(imageZooms.toString());
					}
					
					if(actOwnFileService.insertSelective(actOwnFile) > 0){
						SysLog.info(userId, "", new Gson().toJson(actOwnFile));
						JSONObject result = new JSONObject();
						result.put("fileId", actOwnFile.getFileid());
						result.put("filePath", actOwnFile.getFilepath());
						result.put("fileSize", actOwnFile.getFilesize());
						result.put("fileType", actOwnFile.getFiletype());
						result.put("fileName", actOwnFile.getFilename());
						
						res.write(result.toString());
					}else{
						JSONObject errResult = new JSONObject();
						errResult.put("errorMsg", "抱歉，你上传的文件的记录保存失败，请尝试重新上传！");
						SysLog.error(userId, "", errResult.getString("errorMsg"));
						
						res.write(errResult.toString());
					}
				}else{//update
					ActOwnFile actOwnFile = actOwnFileService.selectByPrimaryKey(fileId);
					actOwnFile.setFileid(fileId);
					actOwnFile.setFilesize(fileSize);
					actOwnFile.setFilename(fileName);
					actOwnFile.setLastupdatetime(actOwnFile.getDateStr(null));
					
					if(actOwnFileService.updateByPrimaryKeySelective(actOwnFile) > 0){
						SysLog.info(userId, "", new Gson().toJson(actOwnFile));
						JSONObject result = new JSONObject();
						result.put("fileId", actOwnFile.getFileid());
						result.put("filePath", actOwnFile.getFilepath());
						result.put("fileSize", actOwnFile.getFilesize());
						result.put("fileType", actOwnFile.getFiletype());
						result.put("fileName", actOwnFile.getFilename());
						
						res.write(result.toString());
					}else{
						JSONObject errResult = new JSONObject();
						errResult.put("errorMsg", "抱歉，你保存的文件的记录保存失败，请尝试重新保存！");
						SysLog.error(userId, "", errResult.getString("errorMsg"));
						
						res.write(errResult.toString());
					}

				}
			}
		}catch(Exception e){
			e.printStackTrace();
			rMap.put("errorMsg", "保存文件时出现问题！");
			res.write(rMap.toString());
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	
	/**
	 * 文件上传
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileUpload")  
    public @ResponseBody void fileUpload(HttpServletRequest request, HttpServletResponse response){ 
		long size = 0;
		String relativePath = null, last = null, fileSize = null, fileStr = null, upload_filename = null, createType = null,
			   fieldName = null, errorMsg = null, myuuid = null, userId = null, imageSizes = null;
		String opttype = "",  taskid = "",  taskdefinekey = "", processinstanceid = "";
		boolean online = false;
		List<String> imageZooms = new ArrayList<String>(3);
		InputStream in = null;
		UUID uuid = UUID.randomUUID();
		
		/*等同
		getServletContext().getInitParameter("");
		request.getSession().getServletContext().getInitParameter("");
		*/
		String fileRootDir = FileUtils.getFileRootDir();
		File file_cache = new File(fileRootDir);
		if (!file_cache.exists()) {  
			file_cache.mkdirs();  
        }
		
		try {
			String reqHeader = request.getHeader("Content-type");
			//System.out.println(request.getHeader("Content-type"));
			//文件保存, 走httpClient请求方式
			if(!reqHeader.startsWith("multipart")){
				in = request.getInputStream();
				userId = request.getParameter("userId");
				upload_filename = request.getParameter("fileName");
				myuuid = request.getParameter("fileId");
				createType = request.getParameter("createType");
				
				myuuid = myuuid != null ? myuuid:uuid.toString();
				createType = createType != null ? createType:"own";
				
				if(upload_filename != null && !"".equals(upload_filename)){
					int lastIndex = upload_filename.lastIndexOf(".");
					last = upload_filename.substring(lastIndex+1).toLowerCase();//文件后缀
				}
				
				size = in.available();
			}else{//文件上传与保存，走上传文件方式
				//获得磁盘文件条目工厂
				DiskFileItemFactory factory = new DiskFileItemFactory();
				//String realPath = request.getSession().getServletContext().getRealPath("/");
				//获取文件需要上传到的路径
				
				//如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
				//设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
				/**
				 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 
				 * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 
				 * 然后再将其真正写到 对应目录的硬盘上
				 */
				factory.setRepository(file_cache);
				//设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
				factory.setSizeThreshold(1024*1024) ;
				
				//高水平的API文件上传处理
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding(request.getCharacterEncoding());
				//upload.setSizeMax(0);
				System.out.println(request.getParameter("userId"));
				//可以上传多个文件
				@SuppressWarnings("unchecked")
				List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
				for(FileItem item : list){
					//获取表单的属性名字
					//String name = item.getFieldName();
					//如果获取的 表单信息是普通的 文本 信息
					if(item.isFormField()){					
						//获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
						fieldName = item.getFieldName();
						//不含1，2，3，则是一般文件上传情况
						if("userId".equals(fieldName)){
							userId = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						if("fileName".equals(fieldName)){
							upload_filename = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						
						
						//------------------------------1,上传(更新)富文本内容情况
						if("fileStr".equals(fieldName)){//富文本编辑内容，生成txt文件
							fileStr = item.getString(request.getCharacterEncoding()).trim();
							if(upload_filename != null && upload_filename.endsWith(".jm")){
								last = "jm";
							}else{
								fileStr = "<!DOCTYPE html><html><head><meta charset='utf-8' /></head><body>"+fileStr+"</body><html>";
								last = "html";
							}
							byte [] richText_buf = fileStr.getBytes();
							size = richText_buf.length;
						}
						//------------------------------2,上传图片情况，需要裁剪
						if("imageSizes".equals(fieldName)){//裁剪标准
							imageSizes = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						//------------------------------3,文件保存，或者更新
						if("fileId".equals(fieldName)){
							myuuid = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						if("createType".equals(fieldName)){
							createType = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						//------------------------------4,学习过程中上传的文件
						if("opttype".equals(fieldName)){
							opttype = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						if("taskid".equals(fieldName)){
							taskid = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						if("taskdefinekey".equals(fieldName)){
							taskdefinekey = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
						if("processinstanceid".equals(fieldName)){
							processinstanceid = item.getString(request.getCharacterEncoding()).trim();
							fieldName = null;
							continue;
						}
					}else{//对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
						/**
						 * 以下三步，主要获取 上传文件的名字
						 */
						//获取路径名
						String value = item.getName() ;
						int start = value.lastIndexOf(File.separator);
						//截取 上传文件的 字符串名字，加1是 去掉反斜杠
						String tempFilename = value.substring(start+1);
						int lastIndex = tempFilename.lastIndexOf(".");
						last = tempFilename.substring(lastIndex+1).toLowerCase();//文件后缀
						
						if(upload_filename == null || "".equals(upload_filename.trim())){
							upload_filename = tempFilename;
						}else{
							if(!upload_filename.endsWith("."+last) && 
							   !upload_filename.endsWith("."+last.toUpperCase())){
								
								upload_filename = upload_filename + "." + last;
							}
						}
						
						size = item.getSize();
						in = item.getInputStream();
						
						break;
					}
				}
			}

			//检查userId
			if(userId == null){
				errorMsg = "上传请求里缺少用户id【userId】属性，不能上传!";
				//break;
			}
			
			if(errorMsg == null){
				createType = createType != null ? createType : "own";
				
				if(size <= 1024)
					fileSize = size + "B";
				else if(size <= 1024*1024)
					fileSize = size / 1024 + "KB";
				else if(size <= 1024*1024*1024)
					fileSize = size/(1024*1024) + "M";
				else
					fileSize = size/(1024*1024*1024) + "G";
					
				if(myuuid == null){//upload
					relativePath = FileUtils.getFileRelativePath(fileRootDir, last, userId, uuid + "." + last, createType);
					if(relativePath == null){
						errorMsg = "抱歉，系统不允许格式为【"+last+"】的文件上传，请选择上传其他常规的文件类型！";
						//break;
					}
				}else{//update
					if("own".equals(createType.trim().toLowerCase())){
						ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(myuuid);
						if(actOwnFile != null){
							relativePath = actOwnFile.getFilepath();
							
							upload_filename = actOwnFile.getFilename();
							int lastIndex = upload_filename.lastIndexOf(".");
							last = upload_filename.substring(lastIndex+1).toLowerCase();//文件后缀
							
						}
					}else{
						ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(myuuid);
						if(actStudyFile != null){
							relativePath = actStudyFile.getFilepath();
							
							upload_filename = actStudyFile.getFilename();
							int lastIndex = upload_filename.lastIndexOf(".");
							last = upload_filename.substring(lastIndex+1).toLowerCase();//文件后缀
						}
					}
					
					if(upload_filename == null || "".equals(upload_filename.trim())){
						System.out.println("fileName为空，且根据fileId找不到可能的文件，无法判断文件流类型！");
						errorMsg = "fileName为空，且根据fileId找不到可能的文件，无法判断文件流类型！";
					}else{
						//找不到要保存的文件的记录时，认为是在线创建新文件，而不是多次保存
						if(relativePath == null){
							relativePath = FileUtils.getFileRelativePath(fileRootDir, last, userId, myuuid + "." + last, createType);
							if(relativePath == null){
								errorMsg = "抱歉，系统不允许格式为【"+last+"】的文件上传，请选择上传其他常规的文件类型！";
								//break;
							}else{
								online = true;
							}
						}
					}
				}
				
				//临时处理
				if(relativePath.contains("videos")){
					if(!"3pg".equals(last.toLowerCase()) && !"mp4".equals(last.toLowerCase())){
						errorMsg = "抱歉，暂时不允许上传"+last+"格式的视频文件，请将视频文件转换为3pg或mp4格式后再上传！";
					}
				}
			}
			
			
			if(errorMsg == null){
				if(fieldName != null){//富文本在线编辑
					OutputStream out = new FileOutputStream(new File(fileRootDir + File.separator + relativePath));
					OutputStreamWriter write = new OutputStreamWriter(out, "UTF-8");
					
					//在   buf 数组中 取出数据 写到 （输出流）磁盘上
					write.write(fileStr);
					write.flush();
					
					write.close();
				}else{//文件直接上传
					FileUtils.writeFile(in, fileRootDir + File.separator + relativePath);
					
					String fileTypeDir = FileUtils.getFileTypeDir(last);
					if(imageSizes != null){//图片裁剪
						if(fileTypeDir != null && "images".equals(fileTypeDir)){
							String[] imageSizeArr = imageSizes.split("_");
							for(int iSize = 0; iSize < imageSizeArr.length;iSize ++){
								String[] currSizeArr = imageSizeArr[iSize].split("m");
								if(currSizeArr.length != 2){
									errorMsg = "sizeError：裁剪图片的格式错误！";
									break;
								}else{
									int x = Integer.parseInt(currSizeArr[0]);
									int y = Integer.parseInt(currSizeArr[0]);
									
									int cutLastIndex = relativePath.indexOf(fileTypeDir);
									StringBuffer sCutImagePath = new StringBuffer();
									sCutImagePath.append(relativePath.substring(0, cutLastIndex-1)).append(File.separator).append("cutImages");
									FileUtils.makeFileDir(fileRootDir, sCutImagePath.toString());
									
									sCutImagePath.append(File.separator).append("zoom_").append(x).append("m").append(y).append(".").append(last);
									ImageUtils.zoomImage(fileRootDir + File.separator + relativePath, fileRootDir + File.separator + sCutImagePath.toString(), x, y);
									imageZooms.add(sCutImagePath.toString());
								}
							}
						}else{
							errorMsg = "上传成功，但上传的文件格式不是图片，不允许裁剪！";
						}
					}
				}
			}
			
			
		} catch (FileUploadException e) {
			e.printStackTrace();
			errorMsg = "抱歉，上传文件时出现异常，请稍后再试试看！";
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "抱歉，上传文件时出现异常，请稍后再试试看！";
		}
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			if(errorMsg != null){
				JSONObject errResult = new JSONObject();
				errResult.put("errorMsg", errorMsg);
				res.write(errResult.toString());
			}else{
				if(myuuid == null || (myuuid != null && online)){//upload 或  在线编辑
					
					JSONObject uploadResult = new JSONObject();
					uploadResult.put("filePath", relativePath);
					uploadResult.put("fileSize", fileSize);
					uploadResult.put("fileType", last);
					uploadResult.put("fileName", upload_filename);
					
					int saveStatus = 0;
					if("own".equals(createType.trim().toLowerCase())){
						ActOwnFile actOwnFile = new ActOwnFile(null, relativePath, upload_filename, userId, fileSize, null, last);
						uploadResult.put("createTime", actOwnFile.getCreatetime());
						if(myuuid == null){
							uploadResult.put("fileId", uuid);
							actOwnFile.setFileid(uuid.toString());
						}else{
							uploadResult.put("fileId", myuuid);
							actOwnFile.setFileid(myuuid);
						}
						
						if(imageZooms.size() > 0){
							uploadResult.put("imageZooms", imageZooms.toString());
							actOwnFile.setImagezooms(imageZooms.toString());
						}
						
						saveStatus = this.actOwnFileService.insertSelective(actOwnFile);
					 
					}else{
						ActStudyFile actStudyFile = new ActStudyFile(null, relativePath, upload_filename, 
																	userId,  fileSize,  null, 
																	last,  opttype,  taskid,  taskdefinekey, 
														    		 processinstanceid,  null,  null, 1);
						
						uploadResult.put("createTime", actStudyFile.getCreatetime());
						if(myuuid == null){
							uploadResult.put("fileId", uuid);
							actStudyFile.setFileid(uuid.toString());
							actStudyFile.setLastflow(actStudyFile.getFileid());
							actStudyFile.setFlowsource(actStudyFile.getFileid());
						}else{
							uploadResult.put("fileId", myuuid);
							actStudyFile.setFileid(myuuid);
							actStudyFile.setLastflow(myuuid);
							actStudyFile.setFlowsource(myuuid);
						}
						
						if(imageZooms.size() > 0){
							uploadResult.put("imageZooms", imageZooms.toString());
							actStudyFile.setImagezooms(imageZooms.toString());
						}
						
						saveStatus = this.actStudyFileService.insertSelective(actStudyFile);
					}
					if(saveStatus > 0){
						SysLog.info(userId, "", uploadResult);
						res.write(uploadResult.toString());
					}else{
						JSONObject errResult = new JSONObject();
						errResult.put("errorMsg", "抱歉，你上传的文件的记录保存失败，请尝试重新上传！");
						SysLog.error(userId, "", errResult.getString("errorMsg"));
						
						res.write(errResult.toString());
					}
				}else{//update
					int updateStatus = 0;
					JSONObject uploadResult = new JSONObject();
					uploadResult.put("fileId", myuuid);
					
					if("own".equals(createType.trim().toLowerCase())){
						ActOwnFile actOwnFile = new ActOwnFile();//(null, relativePath, upload_filename, userId, fileSize, null, last);
						actOwnFile.setFileid(myuuid);
						actOwnFile.setFilesize(fileSize);
						//actOwnFile.setFilename(upload_filename);
						actOwnFile.setLastupdatetime(actOwnFile.getDateStr(null));
						
						updateStatus = this.actOwnFileService.updateByPrimaryKeySelective(actOwnFile);
					}else{
						ActStudyFile actStudyFile = new ActStudyFile();//(null, relativePath, upload_filename, userId, fileSize, null, last);
						actStudyFile.setFileid(myuuid);
						actStudyFile.setFilesize(fileSize);
						//actStudyFile.setFilename(upload_filename);
						actStudyFile.setLastupdatetime(actStudyFile.getDateStr(null));
						
						updateStatus = this.actStudyFileService.updateByPrimaryKeySelective(actStudyFile);
					}
					
					if(updateStatus > 0){
						uploadResult.put("isSuccess", true);
						uploadResult.put("msg", "保存文件记录成功！");
						SysLog.info(userId, "", uploadResult.toString());
					}else{
						uploadResult.put("isSuccess", false);
						uploadResult.put("msg", "抱歉，你保存的文件的记录保存失败，请尝试重新保存！");
						SysLog.error(userId, "", uploadResult.getString("msg"));
					}
					
					res.write(uploadResult.toString());
				}
				
				last = fileSize = fileStr = myuuid = null;
				upload_filename = createType = fieldName = null;
				relativePath = errorMsg = userId = imageSizes = null;
				online = false;
				opttype = taskid = taskdefinekey = processinstanceid = "";
				imageZooms.clear();
			}
		} catch (Exception e) {
			System.out.println("fileUpload error!");
			SysLog.error(userId, "", "fileUpload error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 
	 * @Title: fileCreate 
	 * @Description: 文件创建， 一般用于课程编辑过程中
	 * @author 张龙龙
	 * @date 2018年4月15日 下午9:57:59
	 * @param @param request
	 * @param @param response     
	 * @return void     
	 * @throws 
	 */
	@RequestMapping(value="/fileCreate")  
    public @ResponseBody void fileCreate(HttpServletRequest request, HttpServletResponse response){ 
		String errorMsg = null, inPath = null, output_filePath = null, output_fileId = UUID.randomUUID().toString();
		
		String userId = request.getParameter("userId");
		String targetName = request.getParameter("targetName");
		String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
		/*
		String inPath = request.getParameter("sourceF");//模板文件
		if(inPath == null || "".equals(inPath)){
			inPath = request.getParameter("filePath");//actionOutput(任务输出)文件
		}
		*/
		
		PrintWriter res = null;
		try {
			String tLast = targetName.substring(targetName.lastIndexOf(".") + 1);
			Map<String, String> file_Map = FileUtils.createFileInstance(userId, inPath, targetName, tLast, createType);
			if(file_Map.containsKey("errorMsg")){
				errorMsg = file_Map.get("errorMsg");
			}else{
				output_filePath = file_Map.get("filePath");
				int insertR = 0;
				//存数据库
				if("own".equals(createType)){
					ActOwnFile newActOwnFile = new ActOwnFile(output_fileId, output_filePath, targetName, 
							userId, null, null, tLast);
					
					insertR = this.actOwnFileService.insertSelective(newActOwnFile);
				}else{
					ActStudyFile newActStudyFile = new ActStudyFile(output_fileId, output_filePath, targetName,
							userId, null, null,
							tLast, null, null, null, null, null, null, 1);
					
					insertR = this.actStudyFileService.insertSelective(newActStudyFile);
				}
				
				if(insertR <= 0){
					errorMsg = "抱歉，保存创建的文件信息失败！";
				}
			}
			
			response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			
			res = response.getWriter();
			if(errorMsg != null){
				SysLog.error(userId, "", errorMsg);
				res.write(errorMsg);
			}else{
				JSONObject uploadResult = new JSONObject();
				uploadResult.put("filePath", output_filePath);
				uploadResult.put("fileId", output_fileId);
				
				output_filePath = null;
				errorMsg = null;
				
				SysLog.info(userId, "", "fileCreate成功，filePath:"+output_filePath);
				res.write(uploadResult.toString());
			}
		} catch (IOException e) {
			SysLog.error(userId, "", "fileCreate error!");
			System.out.println("fileCreate error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 文件复制,现在只出现在学习过程中
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileCopy")  
    public @ResponseBody void fileCopy(HttpServletRequest request, HttpServletResponse response){ 
		String errorMsg = null, inPath = null, output_filePath = null;
		
		String userId = request.getParameter("userId");
		String targetName = request.getParameter("targetName");
		String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
		/*
		String inPath = request.getParameter("sourceF");//模板文件
		if(inPath == null || "".equals(inPath)){
			inPath = request.getParameter("filePath");//actionOutput(任务输出)文件
		}
		*/
		
		PrintWriter res = null;
		try {
			String inLast = "", tLast = targetName.substring(targetName.lastIndexOf(".") + 1);
			inPath = request.getParameter("filePath");//统一用filePath
			if(StringUtils.isEmpty(inPath)){
				String fileid = request.getParameter("fileId");
				if(!StringUtils.isEmpty(fileid)){
					SysLog.info(userId, "{\"userId\":"+userId+", \"targetName\":"+targetName+", \"fileid\":"+fileid+", \"createType\":"+createType+"}", "");
					if("own".equals(createType.toLowerCase())){
						ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
						if(actOwnFile != null){
							inPath = actOwnFile.getFilepath();
							inLast = actOwnFile.getFiletype();
						}
					}else{
						ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
						if(actStudyFile != null){
							inPath = actStudyFile.getFilepath();
							inLast = actStudyFile.getFiletype();
						}
					}
				}
			}else{
				SysLog.info(userId, "{\"userId\":"+userId+", \"targetName\":"+targetName+", \"filePath\":"+inPath+"}", "");
				int inLastIndex = inPath.lastIndexOf(".");
				inLast = inPath.substring(inLastIndex + 1);
			}
			
			if(!"".equals(inLast)){
				int tLastIndex = targetName.lastIndexOf(".");
				tLast = targetName.substring(tLastIndex + 1, 3);
				if(!inLast.startsWith(tLast)){
					errorMsg = "要复制的文件格式与要生成的文件格式不一致，无法复制！";
				}else{
					targetName = targetName.substring(0, tLastIndex - 1) + "." + inLast;
				}
			}else{
				inLast = tLast;
			}
			
			if(errorMsg != null){
				Map<String, String> file_Map = FileUtils.createFileInstance(userId, inPath, targetName, inLast, createType);
				if(file_Map.containsKey("errorMsg")){
					errorMsg = file_Map.get("errorMsg");
				}else{
					output_filePath = file_Map.get("filePath");
					
					int insertR = 0;
					int lastSep = inPath.lastIndexOf(File.separator) + 1;
					int lastIndex = inPath.lastIndexOf(".") - 1;
					String fileid = inPath.substring(lastSep, lastIndex);
					//TODO 存数据库
					if(inPath.indexOf("own") > -1){
						ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
						if(actOwnFile != null){
							ActOwnFile newActOwnFile = new ActOwnFile(fileid, output_filePath, targetName, 
									userId, actOwnFile.getFilesize(), actOwnFile.getImagezooms(), actOwnFile.getFiletype());
							
							insertR = this.actOwnFileService.insertSelective(newActOwnFile);
						}
					}else{
						ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
						if(actStudyFile != null){
							ActStudyFile newActStudyFile = new ActStudyFile(fileid, output_filePath, targetName,
									userId, actStudyFile.getFilesize(), actStudyFile.getImagezooms(),
									actStudyFile.getFiletype(), null, null, null, null, null, null, 1);
							
							insertR = this.actStudyFileService.insertSelective(newActStudyFile);
						}
					}
					
					if(insertR <= 0){
						errorMsg = "抱歉，保存创建的文件信息失败！";
					}
				}
			}
			response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			
			res = response.getWriter();
			if(errorMsg != null){
				SysLog.error(userId, "", errorMsg);
				res.write(errorMsg);
			}else{
				JSONObject uploadResult = new JSONObject();
				uploadResult.put("filePath", output_filePath);
				
				output_filePath = null;
				errorMsg = null;
				
				SysLog.info(userId, "", "fileCopy成功，filePath:"+output_filePath);
				res.write(uploadResult.toString());
			}
		} catch (IOException e) {
			SysLog.error(userId, "", "fileCopy error!");
			System.out.println("fileCopy error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 文件读取
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileRead")  
    public @ResponseBody void fileRead(HttpServletRequest request, HttpServletResponse response){ 
		String errorMsg = null, userId = null, inPath = null;
		userId = request.getParameter("userId");
		
		PrintWriter res = null;
		try {
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			/*
			errorMsg = FileUtils.checkAuth(request, userId);
			if(errorMsg != null){
				res = response.getWriter();
				SysLog.error(userId, "", errorMsg);
				
				res.write(errorMsg);
			}else{*/
				String fileRootDir = FileUtils.getFileRootDir();
				
				inPath = request.getParameter("filePath");
				if(inPath == null || "".equals(inPath)){
					String fileid = request.getParameter("fileId");
					String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
					SysLog.info(userId, "{\"userId\":"+userId+", \"fileid\":"+fileid+", \"createType\":"+createType+"}", "");
					
					if("own".equals(createType.toLowerCase())){
						ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
						if(actOwnFile != null){
							inPath = actOwnFile.getFilepath();
						}
					}else{
						ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
						if(actStudyFile != null){
							inPath = actStudyFile.getFilepath();
						}
					}
				}else{
					SysLog.info(userId, "{\"userId\":"+userId+", \"filePath\":"+inPath+"}", "");
				}
				
				InputStream in = FileUtils.readFile(fileRootDir + File.separator + inPath);
				if(in == null){
					errorMsg = "找不到要读取的源文件!";
				}
				
				if(errorMsg != null){
					res = response.getWriter();
					SysLog.error(userId, "", errorMsg);
					
					res.write(errorMsg);
				}else{
					BufferedInputStream buffer_in = new BufferedInputStream(in);//放到缓冲流里
					if(inPath.contains("videos")){
						response.setContentType("video/x-msvideo");
						System.out.println("读取视频文件："+inPath);
					}
					ServletOutputStream out = response.getOutputStream();
					BufferedOutputStream buffer_out = new BufferedOutputStream(out);
					
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					//开始向网络传输文件流
					while((bytesRead = buffer_in.read(buffer)) != -1){
						buffer_out.write(buffer, 0, bytesRead);
					}
					
					buffer_out.flush();
					in.close();
					buffer_in.close();
					out.close();
					buffer_out.close();
				}
			//}
		} catch (IOException e) {
			System.out.println("fileRead error!");
			SysLog.error(userId, "", "fileRead error!");
			
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 文件内容读取
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileContentRead")  
    public @ResponseBody void fileContentRead(HttpServletRequest request, HttpServletResponse response){ 
		String result = "", userId = null, inPath = null;
		StringBuffer sBuffer = new StringBuffer();
		
		userId = request.getParameter("userId");
        
        PrintWriter res = null;
        InputStream in = null;
        InputStreamReader read = null;
		try {
            response.setHeader("Access-Control-Allow-Origin", "*");
    		response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
    		res = response.getWriter();
    		
            String fileRootDir = FileUtils.getFileRootDir();
            
            inPath = request.getParameter("filePath");
			if(inPath == null || "".equals(inPath)){
				String fileid = request.getParameter("fileId");
				String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
				SysLog.info(userId, "{\"userId\":"+userId+", \"fileid\":"+fileid+", \"createType\":"+createType+"}", "");
				
				if("own".equals(createType.toLowerCase())){
					ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
					if(actOwnFile != null){
						inPath = actOwnFile.getFilepath();
					}
				}else{
					ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
					if(actStudyFile != null){
						inPath = actStudyFile.getFilepath();
					}
				}
			}else{
				SysLog.info(userId, "{\"userId\":"+userId+", \"filePath\":"+inPath+"}", "");
			}
			
			in = FileUtils.readFile(fileRootDir + File.separator + inPath);
			if(in == null){ //判断文件是否存在
				System.out.println("找不到要读取的源文件【"+inPath+"】");
	            result = "找不到要读取的源文件【"+inPath+"】";
	        }else{
	        	String encoding="utf-8";
	        	read = new InputStreamReader(in, encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    //System.out.println(lineTxt);
                    sBuffer.append(lineTxt);
                }
                
                in.close();
                read.close();
	        }
			
			if(!"".equals(result)){
				SysLog.error(userId, "", result);
				
    			res.write(result);
    			result = "";
			}else{
    			res.write(sBuffer.toString());
    			sBuffer.delete(0, sBuffer.length());
    		}
        }catch (IOException e) {
			System.out.println("fileRead error!");
			SysLog.error(userId, "", "fileRead error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
		
    }
	
	/**
	 * 文件下载
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileDownload")  
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response){ 
		String errorMsg = null, userId = null, inPath = null, fileName = null;
		
		userId = request.getParameter("userId");
		fileName = request.getParameter("fileName");
        
		PrintWriter res = null;
		try {
			String fileRootDir = FileUtils.getFileRootDir();
			
			inPath = request.getParameter("filePath");
			if(inPath == null || "".equals(inPath)){
				String fileid = request.getParameter("fileId");
				String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
				SysLog.info(userId, "{\"userId\":"+userId+", \"fileid\":"+fileid+", \"createType\":"+createType+"}", "");
				
				if("own".equals(createType.toLowerCase())){
					ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
					if(actOwnFile != null){
						inPath = actOwnFile.getFilepath();
					}
				}else{
					ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
					if(actStudyFile != null){
						inPath = actStudyFile.getFilepath();
					}
				}
			}else{
				SysLog.info(userId, "{\"userId\":"+userId+", \"filePath\":"+inPath+"}", "");
			}
			
			InputStream in = FileUtils.readFile(fileRootDir + File.separator + inPath);
			if(in == null){
				errorMsg = "找不到要读取的源文件【"+inPath+"】";
			}
	       
	        response.reset();
	        response.setContentType("application/x-msdownload");
	        //response.setHeader("Access-Control-Allow-Origin", "*");
			//response.setHeader("Content-type", "text/plain;charset=UTF-8");
			response.setHeader("Location", fileName);
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			
			if(errorMsg != null){
				res = response.getWriter();
				SysLog.error(userId, "", errorMsg);
				
				res.write(errorMsg);
			}else{
				BufferedInputStream buffer_in = new BufferedInputStream(in);//放到缓冲流里
				
				ServletOutputStream out = response.getOutputStream();
				BufferedOutputStream buffer_out = new BufferedOutputStream(out);
				
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				//开始向网络传输文件流
				while((bytesRead = buffer_in.read(buffer)) != -1){
					buffer_out.write(buffer, 0, bytesRead);
				}
				
				buffer_out.flush();
				in.close();
				buffer_in.close();
				out.close();
				buffer_out.close();
			}
		}catch (IOException e) {
			System.out.println("fileDownload error!");
			SysLog.error(userId, "", "fileDownload error!");
			
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
        
    }
	
	/**
	 * 文件删除
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileDelete")  
    public @ResponseBody void fileDelete(HttpServletRequest request, HttpServletResponse response){ 
		PrintWriter res = null;
		try {
			String fileRootDir = FileUtils.getFileRootDir();
			
			String userId = request.getParameter("userId");
			String fileid = request.getParameter("fileId");
			String createType = request.getParameter("createType");//own:自己上传的文件；study:学习过程中产生的文件
			SysLog.info(userId, "{\"userId\":"+userId+", \"fileid\":"+fileid+", \"createType\":"+createType+"}", "");
			
			int delResult = 0; String inPath = null, errorMsg = null;
			if("own".equals(createType.toLowerCase())){
				ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid);
				if(actOwnFile != null){
					inPath = actOwnFile.getFilepath();
					delResult = this.actOwnFileService.deleteByPrimaryKey(fileid);
				}else{
					errorMsg = "找不到要删除的文件！";
				}
			}else{
				ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid);
				if(actStudyFile != null){
					inPath = actStudyFile.getFilepath();
					delResult = this.actStudyFileService.deleteByPrimaryKey(fileid);
				}else{
					errorMsg = "找不到要删除的文件！";
				}
			}
			if(delResult > 0){
				boolean result = FileUtils.deleteFile(fileRootDir, inPath);
				if(!result){
					errorMsg = "抱歉，删除文件失败，找不到指定的路径的文件！";
				}
			}
	       
			response.setHeader("Access-Control-Allow-Origin", "*");
    		//response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
			
    		res = response.getWriter();
    		JSONObject r = new JSONObject();
			if(errorMsg == null){
				r.put("isSuccess", true);
				r.put("msg", "删除文件成功！");
				SysLog.info(userId, "", "删除文件成功！");
			}else{
				r.put("isSuccess", false);
				r.put("msg", errorMsg);
				SysLog.error(userId, "", errorMsg);
			}
			res.write(r.toString());
		}catch (IOException e) {
			System.out.println("fileDelete error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	@RequestMapping(value="/filesBatchDelete")  
    public @ResponseBody void filesBatchDelete(HttpServletRequest request, HttpServletResponse response){ 
		PrintWriter res = null;
		try {
			String fileRootDir = FileUtils.getFileRootDir();
			
			String files = request.getParameter("files");
			String userId = request.getParameter("userId");
			JSONArray fileArr = new JSONArray(files);
			
			//List<String> ownFileIds = new ArrayList<String>();
			//List<String> studyFileIds = new ArrayList<String>();
			
			int delResult = 0; String inPath = null, errorMsg = "";
			for(int i=0;i<fileArr.length();i++){
				JSONObject tempObj = fileArr.getJSONObject(i);
				if("own".equals(tempObj.getString("createType"))){
					ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(tempObj.getString("fileId"));
					if(actOwnFile != null){
						inPath = actOwnFile.getFilepath();
						delResult = this.actOwnFileService.deleteByPrimaryKey(tempObj.getString("fileId"));
					}else{
						errorMsg += "找不到要删除的文件["+tempObj.getString("fileId")+"];";
					}
					
					//ownFileIds.add(tempObj.getString("fileId"));
				}else{
					ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(tempObj.getString("fileId"));
					if(actStudyFile != null){
						inPath = actStudyFile.getFilepath();
						delResult = this.actStudyFileService.deleteByPrimaryKey(tempObj.getString("fileId"));
					}else{
						errorMsg += "找不到要删除的文件["+tempObj.getString("fileId")+"];";
					}
					
					//studyFileIds.add(tempObj.getString("fileId"));
				}
				
				if(delResult > 0){
					boolean result = FileUtils.deleteFile(fileRootDir, inPath);
					if(!result){
						errorMsg += "找不到指定的路径的文件["+tempObj.getString("fileId")+"];";
					}
				}
			}
			
			/*批量一次性删除
			if(ownFileIds.size() > 0){
				
			}
			*/
	       
			response.setHeader("Access-Control-Allow-Origin", "*");
    		//response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
			
    		res = response.getWriter();
    		JSONObject r = new JSONObject();
			if("".equals(errorMsg)){
				r.put("isSuccess", true);
				r.put("msg", "删除成功！");
				SysLog.info(userId, "", "删除成功！");
			}else{
				r.put("isSuccess", false);
				r.put("msg", errorMsg);
				SysLog.error(userId, "", errorMsg);
			}
			res.write(r.toString());
		}catch (IOException e) {
			System.out.println("fileDelete error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 列出磁盘文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileList")  
    public @ResponseBody void fileList(HttpServletRequest request, HttpServletResponse response){ 
		//String userId = request.getParameter("userId");
		String filePath = request.getParameter("filePath");
		
		PrintWriter res = null;
		try {
			String fileRootDir = FileUtils.getFileRootDir();
			JSONArray fileLists = FileUtils.listFiles(fileRootDir, filePath);
	       
			response.setHeader("Access-Control-Allow-Origin", "*");
    		//response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
			
    		res = response.getWriter();
			if(fileLists == null){
				res.write("抱歉，找不到指定的路径!");
			}else{
				res.write(fileLists.toString());
			}
		}catch (IOException e) {
			System.out.println("fileList error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 获取符合条件的文件记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getOwnFiles")  
    public @ResponseBody void getOwnFiles(HttpServletRequest request, HttpServletResponse response){ 
		String userId = request.getParameter("userId");
		String fileId = request.getParameter("fileId");
		String fileName = request.getParameter("fileName");
		String fileType = request.getParameter("fileType");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		PrintWriter res = null;
		try {
			ActOwnFile actOwnFile = new ActOwnFile(fileId, fileName, fileType, userId, startTime, endTime);
			List<ActOwnFile> ownFileList = this.actOwnFileService.selectOwnFiles(actOwnFile);
			
			JSONArray ownFileArr = new JSONArray();
			for(ActOwnFile tempOwnFile:ownFileList){
				ownFileArr.put(tempOwnFile.toJSON());
			}
	       
			response.setHeader("Access-Control-Allow-Origin", "*");
    		//response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
			
    		res = response.getWriter();
    		res.write(ownFileArr.toString());
		}catch (IOException e) {
			System.out.println("fileList error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
	/**
	 * 更新文件记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/fileUpdate")  
    public @ResponseBody void fileUpdate(HttpServletRequest request, HttpServletResponse response){ 
		//String userId = request.getParameter("userId");
		String fileId = request.getParameter("fileId");
		String fileName = request.getParameter("fileName");
		String createType = request.getParameter("createType");
		
		PrintWriter res = null;
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
    		//response.setHeader("Content-type", "text/html;charset=UTF-8");
    		response.setCharacterEncoding("utf-8");
    		res = response.getWriter();
    		
    		int updateStatus = 0;
			if("own".equals(createType.trim().toLowerCase())){
				ActOwnFile actOwnFile = new ActOwnFile();
				actOwnFile.setFileid(fileId);
				actOwnFile.setFilename(fileName);
				actOwnFile.setLastupdatetime(actOwnFile.getDateStr(null));
				
				updateStatus = this.actOwnFileService.updateByPrimaryKeySelective(actOwnFile);
			}else{
				ActStudyFile actStudyFile = new ActStudyFile();
				actStudyFile.setFileid(fileId);
				actStudyFile.setFilename(fileName);
				actStudyFile.setLastupdatetime(actStudyFile.getDateStr(null));
				
				updateStatus = this.actStudyFileService.updateByPrimaryKeySelective(actStudyFile);
			}
			
			JSONObject r = new JSONObject();
			if(updateStatus > 0){
				r.put("isSuccess", true);
				r.put("msg", "更新文件信息成功！");
			}else{
				r.put("isSuccess", false);
				r.put("msg", "更新文件信息失败！");
			}
			
			res.write(r.toString());
		}catch (IOException e) {
			System.out.println("fileUpdate error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
    }
	
}
