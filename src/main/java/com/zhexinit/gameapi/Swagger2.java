package com.zhexinit.gameapi;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Swagger2配置类
 * * 在与spring boot集成时，放在与Application.java同级的目录下。
 * * 通过@Configuration注解，让Spring来加载该类配置。
 * * 再通过@EnableSwagger2注解来启用Swagger2。
 */

@Configuration
@EnableSwagger2
//是否开启swagger，正式环境一般是需要关闭的（避免不必要的漏洞暴露！），可根据springboot的多环境配置进行设置
@ConditionalOnProperty(name="swagger.enable", havingValue="true")
public class Swagger2 {
    private static final String VERSION = "v1";
    private static final String DESC = "文档中可以查询及测试接口调用参数和结果";


    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     */
    /**
     * 用户模块API
     */
    @Bean
    public Docket loginModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.user"))
                .build()
                .groupName("用户模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("用户模块"));
    }

    /**
     * 关卡模块API
     */
    @Bean
    public Docket levelModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.level"))
                .build()
                .groupName("关卡模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("关卡模块"));
    }

    /**
     * 拼图模块API
     */
    @Bean
    public Docket jigsawModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.jigsaw"))
                .build()
                .groupName("拼图模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("拼图模块"));
    }

    /**
     * 任务模块API
     */
    @Bean
    public Docket missionModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.mission"))
                .build()
                .groupName("任务模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("任务模块"));
    }

    /**
     * 商场模块API
     */
    @Bean
    public Docket itemModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.item"))
                .build()
                .groupName("商场模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("商场模块"));
    }

    /**
     * 公共模块
     */
    @Bean
    public Docket commonModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.common"))
                .build()
                .groupName("公共模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("公共模块"));
    }

    /**
     * order
     */
    @Bean
    public Docket orderModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.order"))
                .build()
                .groupName("订单模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("订单模块"));
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return
     */
    /**
     * 用户模块API
     */
    @Bean
    public Docket energyModuleApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zhexinit.planet.controller.energy"))
                .build()
                .groupName("精力模块文档")
                .pathMapping("/")
                .apiInfo(apiInfo("精力模块"));
    }

    private ApiInfo apiInfo(String name) {
        return new ApiInfoBuilder()
        		// 页面标题
                .title(name)
                // 描述
                .description(DESC)
                // 版本号
                .version(VERSION)
                .contact(new Contact("wuqi", "", "wuqi@zhexinit.com"))
                .build();
    }
}
