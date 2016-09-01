package com.yt.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.Iterator;
import java.util.List;

/**
 * BatchPlugin
 * 实现批量操作数据
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/30 15:45
 */
public class BatchPlugin extends PluginAdapter {

    private String item="item";

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        String tableName=  introspectedTable.getTableConfiguration().getTableName();

        XmlElement insertBatchElement=new XmlElement("insert");
        insertBatchElement.addAttribute(new Attribute("id","insertBatch"));
        insertBatchElement.addAttribute(new Attribute("parameterType","java.util.List"));

        StringBuilder insertColumn = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        Iterator<IntrospectedColumn> iterator=introspectedTable.getAllColumns().iterator();
        while (iterator.hasNext()){

            IntrospectedColumn introspectedColumn=iterator.next();
            if(introspectedColumn.isIdentity())
                continue;

            insertColumn.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, item + "."));
            if(iterator.hasNext()){
                insertColumn.append(",");
                valuesClause.append(",");
            }

        }

        TextElement insertBatchTextElement=new TextElement("insert into "+tableName+" ("+insertColumn+") \n" +
                "    values \n    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" > \n" +
                "        ("+valuesClause+") \n   </foreach>");

        insertBatchElement.addElement(insertBatchTextElement);

        document.getRootElement().addElement(insertBatchElement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
}
