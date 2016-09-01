package com.yt.mybatis.generator;


import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseMapper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * PagePlugin
 * 分页插件
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/18 15:12
 */
public class PagePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /*在生成的XML添加一个分页查询的方法*/
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        String tableName=  introspectedTable.getTableConfiguration().getTableName();

        XmlElement selectPageByExampleElement=new XmlElement("select");
        selectPageByExampleElement.addAttribute(new Attribute("id","selectPageByExample"));
        selectPageByExampleElement.addAttribute(new Attribute("parameterType",introspectedTable.getExampleType()));
        selectPageByExampleElement.addAttribute(new Attribute("resultMap","BaseResultMap"));
        selectPageByExampleElement.addElement(new TextElement("select"));

        XmlElement ifDistinctElement=new XmlElement("if");
        ifDistinctElement.addAttribute(new Attribute("test","distinct"));
        ifDistinctElement.addElement(new TextElement("distinct"));
        selectPageByExampleElement.addElement(ifDistinctElement);

        XmlElement includeColumnElement=new XmlElement("include");
        includeColumnElement.addAttribute(new Attribute("refid","Base_Column_List"));
        selectPageByExampleElement.addElement(includeColumnElement);

        selectPageByExampleElement.addElement(new TextElement(" from "+tableName));

        XmlElement ifParameterElement=new XmlElement("if");
        ifParameterElement.addAttribute(new Attribute("test","_parameter != null"));
        XmlElement includeWhereElement=new XmlElement("include");
        includeWhereElement.addAttribute(new Attribute("refid","Example_Where_Clause"));
        ifParameterElement.addElement(includeWhereElement);
        selectPageByExampleElement.addElement(ifParameterElement);

        XmlElement ifOrderByElement=new XmlElement("if");
        ifOrderByElement.addAttribute(new Attribute("test","orderByClause != null"));
        ifOrderByElement.addElement(new TextElement(" order by ${orderByClause} "));
        selectPageByExampleElement.addElement(ifOrderByElement);

        XmlElement ifPageByElement=new XmlElement("if");
        ifPageByElement.addAttribute(new Attribute("test","page !=null"));
        ifPageByElement.addElement(new TextElement("<![CDATA[LIMIT #{limit} OFFSET #{offset}]]>"));
        selectPageByExampleElement.addElement(ifPageByElement);

        document.getRootElement().addElement(selectPageByExampleElement);

        return super.sqlMapDocumentGenerated(document,introspectedTable);
    }

    /*优化统计数据总条数的查询方法*/
    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String tableName=  introspectedTable.getTableConfiguration().getTableName();

        TextElement selectElement=new TextElement("select count(1) from ");

        XmlElement ifElement=new XmlElement("if");
        ifElement.addAttribute(new Attribute("test","distinct"));
        ifElement.addElement(new TextElement("(select  distinct  \n" +
                "      <include refid=\"Base_Column_List\" />\n" +
                "          from  " +tableName+ "\n"+
                "          <if test=\"_parameter != null\">\n" +
                "              <include refid=\"Example_Where_Clause\" />\n" +
                "          </if>) AS c"));

        XmlElement ifDistinctElement=new XmlElement("if");
        ifDistinctElement.addAttribute(new Attribute("test","!distinct"));
        ifDistinctElement.addElement(new TextElement("        " +tableName+ "\n"+
                "        <if test=\"_parameter != null\">\n" +
                "          <include refid=\"Example_Where_Clause\" />\n" +
                "        </if>"));

        element.getElements().clear();
        element.addElement(selectElement);
        element.addElement(ifElement);
        element.addElement(ifDistinctElement);

        return true;
    }

    /*让所有生成的modelExample类文件继承BaseExample类*/
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.setSuperClass(BaseExample.class.getName());
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.yt.mybatis.model.BaseExample"));
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("javax.persistence.*");
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        topLevelClass.addAnnotation("@Entity");
        topLevelClass.addAnnotation("@Table(name = \"" + tableName + "\")");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if(introspectedColumn.getActualColumnName()!=null &&introspectedColumn.getActualColumnName().length()>0) {
            field.addAnnotation("@Column(name=\""+introspectedColumn.getActualColumnName()+"\")");
        }

        Iterator<IntrospectedColumn> iterator=introspectedTable.getPrimaryKeyColumns().iterator();
        IntrospectedColumn primaryKeyColumn=null;
        while (iterator.hasNext()){
            primaryKeyColumn= iterator.next();
        }
        if(null!=primaryKeyColumn && introspectedColumn.getActualColumnName().equals(primaryKeyColumn.getActualColumnName()))  {
            field.addAnnotation("@Id");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> set=interfaze.getSuperInterfaceTypes();
        Iterator<FullyQualifiedJavaType> iterator=set.iterator();

        while (iterator.hasNext()){
            FullyQualifiedJavaType fullyQualifiedJavaType=iterator.next();
             if(fullyQualifiedJavaType.getFullyQualifiedName().equals(BaseMapper.class.getName())){
                 if(introspectedTable.getPrimaryKeyColumns().size()!=1){
                       throw new RuntimeException("请确保数据表中只有一个主键字段，不支持联合主键") ;
                 }
                 fullyQualifiedJavaType.addTypeArgument((introspectedTable.getPrimaryKeyColumns().get(0)).getFullyQualifiedJavaType());
                 fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
                 fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExampleType()));
             }
        }
        return true;
    }

}
