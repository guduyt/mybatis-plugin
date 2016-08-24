
# 基于mysql的mybatis分页插件和mapping插件

添加了基类BaseModel、BaseMapper、BaseExample，分页类Page，插件类PagePlugin。

目前只是在mybatis的mapper.xml中添加了一个selectPageByExample方法，用于分页查询。

这个项目可以扩展为通用的mapper，及其他数据库分页支持。

在基于spring springmvc mybatis的框架中使用。

插件使用配置：
	1.下载源码，使用maven打包生成jar。

	2.maven插件包配置pom.xml：
          
          ......
          
          <!--添加插件使用到Maven -->
          <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>${mybatis-generator-version}</version>
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
          
           <javaModelGenerator targetPackage="${package-path}.model" targetProject="${java-file-path}">            
				<!--其他配置省略；-->
				
				<!-- 设置根对象为com.yt.mybatis.model.BaseModel，那么生成的keyClass或者recordClass会继承这个类；；-->
				  <property name="rootClass" value="com.yt.mybatis.model.BaseModel"></property>
			</javaModelGenerator>
        
			<javaClientGenerator type="XMLMAPPER" targetPackage="${package-path}.mapper" targetProject="${java-file-path}">
				 <!--其他配置省略；-->
				 
				<!-- 设置父接口为com.yt.mybatis.model.BaseMapper，实现自定义的操作数据库的方法-->
				<property name="rootInterface" value="com.yt.mybatis.model.BaseMapper"/>
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
			<!--当把page对象设置到example时才会生效；如果page在设置到example后又被修改了，需要重新在设置才会生效-->
			example.setPage(page);
			List<? extends BaseModel> list= usersMapper.selectByExample(example) ;
		
		
		
		