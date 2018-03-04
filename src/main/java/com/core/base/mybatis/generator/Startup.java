package com.core.base.mybatis.generator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 
 * @ClassName: Startup 
 * @Description: 直接run 即可执行MBG, 配置文件读取classpath:mybatis/generator/generatorConfig.xml
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:24:42 
 */
public class Startup {
    public static void main(String[] args) throws URISyntaxException {
    	String errorMsg = null;
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            URL url = Startup.class.getClassLoader().getResource("mybatis/generator/generatorConfig.xml");
            File configFile = new File(url.getFile());
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (SQLException e) {
        	errorMsg = "执行时出现 SQLException !";
            e.printStackTrace();
        } catch (IOException e) {
        	errorMsg = "执行时出现 IOException !";
            e.printStackTrace();
        } catch (InterruptedException e) {
        	errorMsg = "执行时出现 InterruptedException !";
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
        	errorMsg = "执行时出现 InvalidConfigurationException !";
            e.printStackTrace();
        } catch (XMLParserException e) {
        	errorMsg = "执行时出现 XMLParserException !";
            e.printStackTrace();
        }
        
        if(errorMsg != null){
        	System.out.println(errorMsg);
        }else{
        	System.out.println("Mybatis相关文件创建成功!");
        }
    }
}
