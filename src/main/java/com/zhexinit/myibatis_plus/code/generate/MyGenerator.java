package com.zhexinit.myibatis_plus.code.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class MyGenerator {
	public static void main(String[] args) {
		generateCode(new String[] {"battle_round_step_attack_target_record"});
	}
	
	public static void generateCode(String[] tableNames) {
		AutoGenerator mpg = new AutoGenerator();
		 // 选择 freemarker 模板引擎，默认 Veloctiy，如果用的是Veloctiy模板引擎就不需要这句代码
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		
		//全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setAuthor("wuqi");
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setSwagger2(true);
		gc.setOpen(false);
		
		gc.setFileOverride(false); //设置同名文件是否覆盖
		gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
	    gc.setEnableCache(false);// XML 二级缓存
	    gc.setBaseResultMap(true);// XML ResultMap
	    gc.setBaseColumnList(true);// XML columList
		
	    // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
	    
		mpg.setGlobalConfig(gc);
		
		
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setTypeConvert(new MySqlTypeConvert() {

			@Override
			public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
				 System.out.println("转换类型：" + fieldType);
	                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回
				return super.processTypeConvert(globalConfig, fieldType);
			}
			
		});
		
		dsc.setUrl("jdbc:mysql://127.0.0.1:3306/game?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("tryme");
		
		mpg.setDataSource(dsc);
		
		StrategyConfig strategy = new StrategyConfig();
//		strategy.setTablePrefix(new String[] {""});// 此处可以修改为您的表前缀
		strategy.setNaming(NamingStrategy.underline_to_camel);//表名生成策略
//		strategy.setNaming(NamingStrategy.no_change);
//		strategy.setColumnNaming(NamingStrategy.no_change);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);//字段的生成策略
		strategy.setEntityLombokModel(true);
		strategy.setInclude(tableNames);
		
		mpg.setStrategy(strategy);
		
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.zhexinit.myibatis_plus.code.generate");
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setMapper("mapper");
		pc.setService("service");
		pc.setServiceImpl("service.impl");
		mpg.setPackageInfo(pc);
		 
        // 执行生成
        mpg.execute();

		
	}
}
