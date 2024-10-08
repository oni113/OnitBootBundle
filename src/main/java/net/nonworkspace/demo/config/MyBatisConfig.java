package net.nonworkspace.demo.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan("net.nonworkspace.demo.mapper")
@RequiredArgsConstructor
@Slf4j
public class MyBatisConfig {

    private final DataSource dataSource;

    private final Environment environment;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // Mapper Locations 설정
        String mapperLocationProperty = environment.getProperty("mybatis.mapper-locations");
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations = pathResolver.getResources(mapperLocationProperty);
        factoryBean.setMapperLocations(mapperLocations);

        // Type Aliases Package 설정
        String typeAliasesPackageProperty = environment.getProperty("mybatis.type-aliases-package");
        factoryBean.setTypeAliasesPackage(typeAliasesPackageProperty);

        // undescore to camel case 설정
        String underToCamel = environment.getProperty(
            "mybatis.configuration.map-underscore-to-camel-case");
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(Boolean.parseBoolean(underToCamel));

        // cache enabled 설정
        String cacheEnabled = environment.getProperty("mybatis.configuration.cache-enabled");
        configuration.setCacheEnabled(Boolean.parseBoolean(cacheEnabled));
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }
}
