package com.niuzhendong.frame;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 测试后台的restful形式的接口
 * @author niuzhendong
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    // 定义分隔符,配置Swagger多包
    private static final String splitor = ";";

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径，指定当前包路径，这里就添加了两个包，注意方法变成了basePackage，中间加上成员变量splitor
                .apis(RequestHandlerSelectors.basePackage("com.niuzhendong.frame.modules.sys.controller"))
//                .apis(basePackage("com.jstv.lizhi.modules.controller"+splitor+"com.jstv.lizhi.modules.applicationpackage.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 添加摘要信息，构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("荔枝工厂_接口文档")
                //描述
                .description("描述：所有接口的测试环境")
                //创建人
//                .contact(new Contact("Niu Zhendong", "http://www.niuzhendong.com:8080", "niuzd@vip.jsbc.com"))
                .contact(new Contact("Swagger常用注解","http://www.niuzhendong.com:8888/blog/archives/269","niuzd@vip.jsbc.com"))
                //版本号
                .version("版本号:1.0")
                .build();
    }

    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     * @author  teavamc
     * @date 2019/1/26
     * @param basePackage
     * @return com.google.common.base.Predicate<springfox.documentation.RequestHandler>
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
