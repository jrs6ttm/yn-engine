package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

import com.core.interceptor.StartUpServlet;

public class FileUtils {
	//文件的根路径
	static String fileRootDir = null;
	//支持的文件类型
	static Map<String, String> FileTypeAllow = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			//文档类型
	        put("txt" , "file");
	        put("doc" , "file");
	        put("docx" , "file");
	        put("pdf" , "file");
	        put("ppt" , "file");
	        put("pptx" , "file");
	        put("xls" , "file");
	        put("xlsx" , "file");
	        put("xml" , "file");
	        put("chm" , "file");
	        put("rtf" , "file");
	        put("wps" , "file");//WPS文字
	        put("dps" , "file");//WPS演示
	        put("et" , "file");//WPS表格
	        put("jm" , "file");
	        
	        //静态文件类型
	        put("html" , "static");
	        put("htm" , "static");
	        put("dhtml" , "static");
	        put("xhtml" , "static");
	        put("html" , "static");
	        put("shtm" , "static");
	        put("shtml" , "static");
	        put("js" , "static");
	        put("css" , "static");
	        put("json" , "static");
	        put("jsp" , "static");
	        put("jade" , "static");
	        put("ejs" , "static");
	        put("php" , "static");
	        put("asp" , "static");
	        put("aspx" , "static");
	        put("java" , "static");
	        put("sql" , "static");

	        //图片
	        put("jpg" , "images");
	        put("jpeg", "images");
	        put("png" , "images");
	        put("gif" , "images");
	        put("psd" , "images");
	        put("swf" , "images");
	        put("svg" , "images");
	        put("ico" , "images");
	        put("bmp" , "images");

	        //音频
	        put("mp3" , "audios");
	        put("wma" , "audios");
	        put("wav" , "audios");
	        put("ape" , "audios");
	        put("ogg" , "audios");
	        put("flac" , "audios");

	        //视频
	        put("3gp" , "videos");
	        put("mp4" , "videos");
	        put("mpg" , "videos");
	        put("mpeg" , "videos");
	        put("avi" , "videos");
	        put("wmv" , "videos");
	        put("vcd" , "videos");
	        put("dvd" , "videos");
	        put("rmvb" , "videos");
	        put("flv" , "videos");
	        put("flash" , "videos");
	        put("f4v" , "videos");
	        put("mov" , "videos");

	        //压缩文件
	        put("zip" , "zips");
	        put("rar" , "zips");
	        put("7z" , "zips");
	        put("jar" , "zips");
		}
	};
	
	/**
	 * 获取日期字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String getDateStr(Date date, String dateFormat){
		if(date == null){
			date = new Date();
		}
		if(dateFormat == null){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		
		return sf.format(date);
	}
	
	/**
	 * 获取允许上传的文件类型对应的文件夹名字
	 * @return
	 */
	public static String getFileTypeDir(String last){
		String fileTypeDir = null;
		if(FileUtils.FileTypeAllow.containsKey(last.toLowerCase())){
			fileTypeDir =  FileUtils.FileTypeAllow.get(last);
		}
		
		return fileTypeDir;
	}
	
	/**
	 * 获取文件存储的根路径
	 * @return
	 */
	public static String getFileRootDir(){
		if(FileUtils.fileRootDir != null){
			
			return FileUtils.fileRootDir;
			
		}else{
			String os = System.getProperty("os.name");
			System.out.println("os env: "+os);
			String fileRootDir = os.toLowerCase().startsWith("win") ? "win_fileRootDir" : "unix_fileRootDir";
			if(StartUpServlet.APPLICATION_SERVLET != null){
				FileUtils.fileRootDir = StartUpServlet.APPLICATION_SERVLET.getInitParameter(fileRootDir);
			}else{
				FileUtils.fileRootDir = System.getProperty(fileRootDir);
			}
			System.out.println("fileRootDir: " + FileUtils.fileRootDir);
			
			return FileUtils.fileRootDir;
		} 
	}
	
	/**
	 * 生成指定路径的目录结构
	 * @param fileRootDir
	 * @param relativePath
	 */
	public static void makeFileDir(String fileRootDir, String relativePath){
		
		File dir = new File(fileRootDir + File.separator + relativePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}
	
	/**
	 * 组装文件的相对路径
	 * 文件相对路径：{userId}\{type}\{年月}\{FileTypeAllow.get(last)}\{filename}
	 * eg: zhangll\own\2017-2\html\123456789.html
	 * @param fileRootDir
	 * @param last
	 * @param userId
	 * @param fileName
	 * @return
	 */
	public static String getFileRelativePath(String fileRootDir, String last, String userId, String fileName, String type){
		String relativePath = null;
		
		String fileTypeDir = FileUtils.getFileTypeDir(last);
		if(fileTypeDir == null){
			//relativePath = "抱歉，系统不允许格式为【"+last+"】的文件上传，请选择上传其他常规的文件类型！";
			return relativePath;
		}
		
		relativePath = userId + File.separator + type + File.separator +FileUtils.getDateStr(null, "yyyy-MM") 
						+ File.separator + fileTypeDir;
		FileUtils.makeFileDir(fileRootDir, relativePath);
		
		return relativePath + File.separator + fileName;
	}
	
	public static boolean isCanAccess(){
		Boolean canAccess = false;
		
		return canAccess;
	}
	
	public static String checkAuth(HttpServletRequest request, String userId){
		String errorMsg = null;
		
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie:cookies){
			System.out.println("COOKIE 【" + cookie.getDomain() + " " + cookie.getPath() + "】" + cookie.getName() + " : " + cookie.getValue());
		}
		
		HttpSession httpSession = request.getSession(false);
		if(httpSession != null){
			System.out.println("SESSION 【"+httpSession.getId()+"】:");
			JSONObject userData = (JSONObject)httpSession.getAttribute("userData");
			Enumeration<String> sessionAttrs = httpSession.getAttributeNames();
			while(sessionAttrs.hasMoreElements()){
				String attrEle = sessionAttrs.nextElement();
				System.out.println(attrEle + " : " + httpSession.getAttribute(attrEle).toString());
			}
			
			if(userData == null || !userData.has("id") || !userId.equals(userData.get("id"))){
				errorMsg = "抱歉，你尚未登录，不能操作文件！";
			}
			
			
			//<key, value> == <seesionId, userData> ??
			//redisService.get(httpSession.getId());
		}
		
		return errorMsg;
	}
	
	/**
	 * 读取文件流
	 * @param inPath
	 * @return
	 * @throws IOException
	 */
	public static FileInputStream readFile(String inPath) throws IOException{
		FileInputStream in = null;
		
		File in_file = new File(inPath);
		if(in_file.exists() && in_file.isFile()){
			in = new FileInputStream(in_file);
		}
		
		return in;
	}
	
	/**
	 * 写文件到指定路径
	 * @param in
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeFile(InputStream in, String filePath) throws IOException{
		OutputStream out = new FileOutputStream(new File(filePath));
		int length = 0 ;
		byte [] file_buf = new byte[1024] ;
		// in.read(buf) 每次读到的数据存放在   buf 数组中
		while( (length = in.read(file_buf) ) != -1)
		{
			//在   buf 数组中 取出数据 写到 （输出流）磁盘上
			out.write(file_buf, 0, length);
			out.flush();
		}
		
		in.close();
		out.close();
	}
	
	/**
	 * 创建一个新的空文件
	 * @param fileRootDir
	 * @param userId
	 * @param targetName
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> createFile(String fileRootDir, String userId, String targetName, String last, String createType) throws IOException{
		Map<String, String> rMap = new HashMap<String, String>();
		
		int create_lastIndex = targetName.lastIndexOf(".");
		String output_filePath = FileUtils.getFileRelativePath(fileRootDir, targetName.substring(create_lastIndex+1), userId, targetName, createType);
		if(output_filePath == null){
			rMap.put("errorMsg", "文件创建失败，系统禁止对格式为【"+targetName.substring(create_lastIndex+1)+"】的文件进行操作！");
		}else{
			String matchLast = "doc,docx,ppt,pptx,xls,xlsx,jm";
			if(matchLast.indexOf(last) > -1){
				String tPath = StartUpServlet.APPLICATION_URL + "template"+ File.separator + last + "." + last;
				System.out.println("新建文件，根据 " + tPath);
				//InputStream in = servletContext.getResourceAsStream(tPath);
				InputStream in = new FileInputStream(new File(tPath));
				
				rMap = FileUtils.copyFile(in, userId, targetName, last, createType);
			}else{
				File create_file = new File(fileRootDir + File.separator + output_filePath);
				if(!create_file.createNewFile()){
					String errorMsg = "创建文件【"+output_filePath+"】失败！";
					rMap.put("errorMag", errorMsg);
				}else{
					rMap.put("filePath", output_filePath);
				}
			}
		}
		
		return rMap;
	}
	
	/**
	 * 拷贝文件
	 * @param fileRootDir
	 * @param inPath
	 * @param userId
	 * @param targetName
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> copyFile(String fileRootDir, String inPath, String userId, String targetName, String last, String createType) throws IOException{
		Map<String, String> rMap = new HashMap<String, String>();
		
		InputStream in = FileUtils.readFile(fileRootDir + File.separator + inPath);
		if(in == null){
			String errorMsg = "找不到要拷贝的源文件【"+inPath+"】";
			rMap.put("errorMsg", errorMsg);
		}else{
			rMap = FileUtils.copyFile(in, userId, targetName, last, createType);
			/*
			int lastIndex = inPath.lastIndexOf(".");
			String output_filePath = FileUtils.getFileRelativePath(fileRootDir, inPath.substring(lastIndex+1), userId, targetName, "study");
			if(output_filePath == null){
				rMap.put("errorMsg", "文件复制失败，系统禁止对格式为【"+inPath.substring(lastIndex+1)+"】的文件进行操作！");
			}else{
				FileUtils.writeFile(in, fileRootDir + File.separator + output_filePath);
				
				rMap.put("filePath", output_filePath);
			}
			*/
		}
		
		return rMap;
	}
	
	public static Map<String, String> copyFile(InputStream in, String userId, String targetName, String last, String createType) throws IOException{
		Map<String, String> rMap = new HashMap<String, String>();
		
		if(in == null){
			String errorMsg = "复制的文件流为null！";
			rMap.put("errorMsg", errorMsg);
		}else{
			String output_filePath = FileUtils.getFileRelativePath(fileRootDir, last, userId, targetName, createType);
			if(output_filePath == null){
				rMap.put("errorMsg", "文件复制失败，系统禁止对格式为【"+last+"】的文件进行操作！");
			}else{
				FileUtils.writeFile(in, fileRootDir + File.separator + output_filePath);
				
				rMap.put("filePath", output_filePath);
			}
		}
		
		return rMap;
	}
	
	/**
	 * 生成文件filePath的实例文件
	 * @param servletContext
	 * @param userId
	 * @param filePath
	 * @param targetName
	 * @return
	 */
	public static Map<String, String> createFileInstance(String userId, String filePath, String targetName, String last, String createType){
		Map<String, String> file_Map = new HashMap<String, String>(); 
		
		try {
			String fileRootDir = FileUtils.getFileRootDir();
			if(filePath == null || "".equals(filePath)){//create new file
				file_Map = FileUtils.createFile(fileRootDir, userId, targetName, last, createType);
			}else{//copy 
				file_Map = FileUtils.copyFile(fileRootDir, filePath, userId, targetName, last, createType);
			}
		} catch (IOException e) {
			file_Map.put("errorMsg", "生成文件实例时出现异常！");
			e.printStackTrace();
		}
		
		return file_Map;
	}
	
	/**
	 * 删除指定的文件或目录
	 * @param fileRootDir
	 * @param inPath
	 * @return
	 */
	public static boolean deleteFile(String fileRootDir, String inPath){
		boolean result = false;
		
		File f = new File(fileRootDir + File.separator + inPath);
		if(f.exists()){
			f.delete();
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 列出指定目录中的所有文件和目录（列出的只是名字）
	 * @param fileRootDir
	 * @param inPath
	 * @return
	 */
	public static JSONArray listFiles(String fileRootDir, String inPath){
		JSONArray fileList = new JSONArray();
		File f = new File(fileRootDir + File.separator + inPath);
		if(f.exists() && f.isDirectory()){
			String[] files = f.list();
			if(files.length > 0){
				for(String fileName:files){
					JSONObject tempJ = new JSONObject();
					tempJ.put("name", fileName);
					
					File tempFile = new File(fileRootDir + File.separator + inPath + File.separator + fileName);
					if(tempFile.isDirectory()){
						tempJ.put("type", "directory");
					}else{
						tempJ.put("type", "file");
					}
					
					fileList.put(tempJ);
				}
			}
		}
		
		return fileList;
	}
}
