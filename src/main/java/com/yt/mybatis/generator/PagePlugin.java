package com.yt.mybatis.generator;


import com.yt.mybatis.dao.DaoCreator;
import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseMapper;
import com.yt.mybatis.model.BaseModel;
import com.yt.mybatis.model.BasePKMapper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
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
    private static final String daoPackageProperty="daoPackage";

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

        selectPageByExampleElement.addElement(new TextElement("<![CDATA[LIMIT #{limit} , #{offset}]]>"));

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
        topLevelClass.addImportedType(new FullyQualifiedJavaType(BaseExample.class.getName()));
        topLevelClass.setSuperClass(BaseExample.class.getName());
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(BaseModel.class.getName()));
        topLevelClass.setSuperClass(BaseModel.class.getName());
        /*topLevelClass.addImportedType("javax.persistence.*");
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        topLevelClass.addAnnotation("@Entity");
        topLevelClass.addAnnotation("@Table(name = \"" + tableName + "\")");*/
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        /*if(introspectedColumn.getActualColumnName()!=null &&introspectedColumn.getActualColumnName().length()>0) {
            field.addAnnotation("@Column(name=\""+introspectedColumn.getActualColumnName()+"\")");
        }

        if(introspectedTable.hasPrimaryKeyColumns() && introspectedTable.getPrimaryKeyColumns().size()==1) {
            Iterator<IntrospectedColumn> iterator = introspectedTable.getPrimaryKeyColumns().iterator();
            IntrospectedColumn primaryKeyColumn = null;
            while (iterator.hasNext()) {
                primaryKeyColumn = iterator.next();
            }
            if (null != primaryKeyColumn && introspectedColumn.getActualColumnName().equals(primaryKeyColumn.getActualColumnName())) {
                field.addAnnotation("@Id");
            }
        }*/
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (introspectedTable.getAllColumns().size() == introspectedTable.getPrimaryKeyColumns().size()) {
            throw new RuntimeException("不支持仅有主键的表自动生成代码");
        }
        String baseMapper=BaseMapper.class.getName();
        if(introspectedTable.hasPrimaryKeyColumns())
            baseMapper= BasePKMapper.class.getName();

        interfaze.addImportedType(new FullyQualifiedJavaType(baseMapper)); ;
        interfaze.addSuperInterface(new FullyQualifiedJavaType(baseMapper));
        Set<FullyQualifiedJavaType> set=interfaze.getSuperInterfaceTypes();
        Iterator<FullyQualifiedJavaType> iterator=set.iterator();
        while (iterator.hasNext()){
            FullyQualifiedJavaType fullyQualifiedJavaType=iterator.next();

            if(fullyQualifiedJavaType.getFullyQualifiedName().equals(baseMapper)){

                FullyQualifiedJavaType type;
                if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
                    IntrospectedColumn column = introspectedTable.getPrimaryKeyColumns().get(0);
                    type = new FullyQualifiedJavaType(column.getFullyQualifiedJavaType().getFullyQualifiedName());
                    interfaze.addImportedType(type);
                    fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(type.getShortName()));
                } else if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                    type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
                    interfaze.addImportedType(type);
                    fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(type.getShortName()));
                }

                type=new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
                interfaze.addImportedType(type);
                fullyQualifiedJavaType.addTypeArgument(type);

                type=new FullyQualifiedJavaType(introspectedTable.getExampleType());
                interfaze.addImportedType(type);
                fullyQualifiedJavaType.addTypeArgument(type);
            }
        }

        String daoPackage=context.getProperty(daoPackageProperty);
        if(null!=daoPackage){
            DaoCreator creator = new DaoCreator(context,daoPackage);
            creator.setTable(introspectedTable);
            creator.exec();
        }
        return true;
    }
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }


    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

}
