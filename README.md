
# 基于mysql的mybatis分页插件和mapping插件

添加了基类BaseModel、BaseMapper、BasePKMapper、BaseExample，分页类Page
，插件类PagePlugin,批量插入插件类BatchPlugin。

目前只是在mybatis的mapper.xml中添加了一个selectPageByExample方法，用于分页查询。

这个项目可以扩展为通用的mapper，及其他数据库分页支持。

在基于spring springmvc mybatis的框架中使用。

插件使用配置：

	1.下载源码，使用maven打包生成jar。

	2.maven插件包配置pom.xml：

          <!--mybatis-plugin插件依赖-->
          <dependencies>
                 <dependency>
                      <groupId>com.yt</groupId>
                      <artifactId>mybatis-plugin</artifactId>
                      <version>1.0-SNAPSHOT</version>
                 </dependency>
                 <dependency>
                     <groupId>javax.persistence</groupId>
                     <artifactId>persistence-api</artifactId>
                     <version>1.0.2</version>
                 </dependency>
          </dependencies>
          ......

          <!--添加插件使用到Maven -->
          <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.3</version>
                <configuration>
                    <configurationFile>src/main/resources/config/generatorConfig.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
                <dependencies>
                 <!--mybatis生成配置 -->
                    <dependency>
                        <groupId>org.mybatis</groupId>
                        <artifactId>mybatis</artifactId>
                        <version>3.4.0</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.29</version>
                    </dependency>
                    <!--mybatis-plugin插件使用配置 -->
                    <dependency>
                        <groupId>com.yt</groupId>
                        <artifactId>mybatis-plugin</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
            </plugin>
          </plugins>
          
	2.generatorConfig.xml配置：
		<generatorConfiguration>
			......
          
			<!--添加插件使用到MBG -->
			<plugin type="com.yt.mybatis.generator.PagePlugin"></plugin>
            <plugin type="com.yt.mybatis.generator.BatchPlugin"></plugin>

            <commentGenerator type="com.yt.mybatis.generator.FieldCommentGenerator">
            </commentGenerator>
           <javaModelGenerator targetPackage="${package-path}.model" targetProject="${java-file-path}">            
				<!--其他配置省略；-->
			</javaModelGenerator>
        
			<javaClientGenerator type="XMLMAPPER" targetPackage="${package-path}.mapper" targetProject="${java-file-path}">
				 <!--其他配置省略；-->
			</javaClientGenerator>
        ......
	</generatorConfiguration>
	
	3.运行mybatis-generator-maven-plugin生成代码。
		分页查询使用说明:
		
			Page page=new Page();
			page.setPageSize(20);
			
			UsersExample example=new UsersExample();
			UsersExample.Criteria criteria= example.createCriteria();
			criteria.andAgeBetween(20,65) ;
			<!--当把page分页信息设置到example时才会生效；如果page分页信息在设置到example后又被修改了，需要重新在设置才会生效，只有调用selectPageByExample方法才会执行分页查询-->
			Page page=new Page();
            example.setCurrentPage(page.getCurrentPage());
            example.setPageSize(page.getPageSize());
			List<? extends BaseModel> list= usersMapper.selectPageByExample(example) ;
		
		
		
		
