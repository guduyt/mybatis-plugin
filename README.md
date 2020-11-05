
# 基于mysql的mybatis分页插件和mapping插件

添加了基类BaseModel、BaseMapper、BasePKMapper、BaseExample，分页类Page
，插件类PagePlugin,批量插入插件类BatchPlugin。

目前只是在mybatis的mapper.xml中添加了一个selectPageByExample方法，用于分页查询。

这个项目可以扩展为通用的mapper，及其他数据库分页支持。

在基于spring springmvc mybatis的框架中使用。

插件使用配置：

	1.下载源码，使用maven打包生成jar。

	2.maven插件包配置pom.xml：

           <!--包路径全局设置-->
           <properties>
                <package-path>com.entity.auto</package-path>
                <java-file-path>${basedir}/src/main/java</java-file-path>
                <resources-path>${basedir}/src/main/resources</resources-path>
           </properties>

          <!--mybatis-plugin插件依赖-->
          <dependencies>
                 <dependency>
                      <groupId>com.yt</groupId>
                      <artifactId>mybatis-plugin</artifactId>
                      <version>1.0-SNAPSHOT</version>
                 </dependency>                
          </dependencies>
          ......

          <!--添加插件使用到Maven -->
          <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
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
                        <version>3.4.6</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.19</version>
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
		<!-- 生产dao实体，不设置时不生成dao-->
		<property name="daoPackage" value="${package-path}.dao"/>

		<!--添加插件使用到MBG -->

		<!--覆盖生成XML文件 -->
		<plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin"></plugin>
		<plugin type="com.yt.mybatis.generator.PagePlugin"></plugin>
		<plugin type="com.yt.mybatis.generator.BatchPlugin"></plugin>

		<!--生成实体添加注释 -->
		<commentGenerator type="com.yt.mybatis.generator.FieldCommentGenerator">
		</commentGenerator>
		<javaModelGenerator targetPackage="${package-path}.model" targetProject="${java-file-path}">
		<!--其他配置省略；-->
		</javaModelGenerator>

		<!--对应的mapper.xml文件  -->
		<sqlMapGenerator targetPackage="${package-path}.mapper" targetProject="${resources-path}">
		<!--其他配置省略；-->
		</sqlMapGenerator>

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
		int size=usersMapper.countByExample(example);
		<!--当把page分页信息设置到example时才会生效；如果page分页信息在设置到example后又被修改了，需要重新在设置才会生效，只有调用selectPageByExample方法才会执行分页查询-->
		
		page.setTotalRow(size);
		example.setCurrentPage(page.getCurrentPage());
		example.setPageSize(page.getPageSize());
		List<SysUser> data= usersMapper.selectPageByExample(example);
		page.setData(data);
		
		
		
		
