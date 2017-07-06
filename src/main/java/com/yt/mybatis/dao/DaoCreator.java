package com.yt.mybatis.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yt on 2017-7-6.
 */
public class DaoCreator {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String daoSuffixProperty="daoSuffix";

	private Context context;
	private String daoPackage;
	private String daoSuffix="Dao";

	private String modelBasePath;
	private String modelPackage;
	private String modelName;

	private String mapperPackage;

	private boolean hasPK = false;

	private String pkTypeShortName;
	private String pkType;
	private boolean hasDeleteColumn = false;
	private IntrospectedTable introspectedTable;

	public DaoCreator() {
	}

	public DaoCreator(Context context,String daoPackage) {
		this.context = context;
		this.daoPackage=daoPackage;
		init();
	}

	private void init() {
		modelPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
		modelBasePath=context.getJavaModelGeneratorConfiguration().getTargetProject();
		mapperPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();

		daoSuffix = context.getProperty(daoSuffixProperty) == null ? daoSuffix : context.getProperty(daoSuffixProperty);
		if (daoPackage == null) {
			String[] ss = mapperPackage.split("\\.");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < ss.length - 1; i++) {
				sb.append(".").append(ss[i]);
			}
			sb.append("dao");
			daoPackage = sb.toString();
		}
	}

	public void exec() {
		String path = getDaoPath();
		if (!Paths.get(path).toFile().exists()) {
			execDao();
		} else {
			logger.info("{} is exists", path);
		}
		if (!Paths.get(getDaoImplPath()).toFile().exists()) {
			execImpl();
		} else {
			logger.info("{} is exists", path);
		}
	}

	public void execDao() {
		String baseDao = BaseDao.class.getSimpleName();
		if (hasPK) {
			baseDao = BasePKDao.class.getSimpleName();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(daoPackage).append(";\r\n\r\n");

		sb.append("import com.yt.mybatis.dao.").append(baseDao).append(";\r\n");
		sb.append("import ").append(modelPackage).append(".").append(modelName).append(";\r\n");
		sb.append("import ").append(modelPackage).append(".").append(modelName).append("Example;\r\n");
		if (pkType != null) {
			sb.append("import ").append(pkType).append(";\r\n");
		}
		sb.append("\r\n");

		sb.append("public interface ").append(modelName).append("Dao extends ").append(baseDao).append("<");
		if (hasPK) {
			sb.append(pkTypeShortName).append(", ");
		}
		sb.append(modelName).append(", ").append(modelName).append("Example> {\r\n\r\n");
		if (hasDeleteColumn) {
			sb.append("\tpublic int deleteLogicByExample(").append(modelName).append("Example example);\r\n\r\n");
			if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
				sb.append("\tpublic int deleteLogicByPrimaryKey(").append(pkTypeShortName).append(" key);\r\n\r\n");
			}
		}
		sb.append("}");
		writeToFile(getDaoPath(), sb.toString());
	}

	public void execImpl() {

		String baseDaoPath= BaseDaoAdapter.class.getName();
		if (hasPK) {
			baseDaoPath= BasePKDaoAdapter.class.getName();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(daoPackage).append(".impl").append(";\r\n\r\n");
		sb.append("import org.springframework.stereotype.Repository;\r\n");
		sb.append("\r\n");
		sb.append("import ").append(daoPackage).append(".").append(modelName).append("Dao;\r\n");
		sb.append("import ").append(mapperPackage).append(".").append(modelName).append("Mapper;\r\n");
		sb.append("import ").append(modelPackage).append(".").append(modelName).append(";\r\n");
		sb.append("import ").append(modelPackage).append(".").append(modelName).append("Example;\r\n");
		if (pkType != null) {
			sb.append("import ").append(pkType).append(";\r\n");
		}

		sb.append("import ").append(baseDaoPath).append(";\r\n\r\n");
		sb.append("@Repository\r\n");

		String className = "public class #modelName#DaoImpl extends BaseDaoAdapter<#modelName#, #modelName#Example, #modelName#Mapper> implements #modelName#Dao {";
		if (hasPK) {
			className = "public class #modelName#DaoImpl extends BasePKDaoAdapter<#pk#, #modelName#, #modelName#Example, #modelName#Mapper> implements #modelName#Dao {";
		}
		sb.append(className).append("\r\n");
		if (hasDeleteColumn) {
			sb.append("\tpublic int deleteLogicByExample(").append(modelName).append("Example example) {\r\n");
			sb.append("\t\t").append(modelName).append(" record = new ").append(modelName).append("();\r\n");
			sb.append("\t\t").append("record.setIsDeleted(1);\r\n");
			sb.append("\t\t").append("return updateByExample(record, example);\r\n");
			sb.append("\t}\r\n");
			if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
				sb.append("\tpublic int deleteLogicByPrimaryKey(").append(pkTypeShortName).append(" key) {\r\n");
				sb.append("\t\t").append(modelName).append(" record = new ").append(modelName).append("();\r\n");
				sb.append("\t\t").append("record.setIsDeleted(1);\r\n");
				IntrospectedColumn key = introspectedTable.getPrimaryKeyColumns().get(0);
				String keyName = key.getJavaProperty();
				sb.append("\t\t").append("record.");
				sb.append("set");
				sb.append(keyName.toUpperCase().charAt(0));
				sb.append(keyName.substring(1));
				sb.append("(key);\r\n");
				sb.append("\t\t").append("return updateByPrimaryKey(record);\r\n");
				sb.append("\t}\r\n");
			}
		}
		sb.append("}");
		String result = sb.toString();
		result = result.replace("#modelName#", modelName);
		if (hasPK)
			result = result.replace("#pk#", pkTypeShortName);
		writeToFile(getDaoImplPath(), result);
	}

	private void writeToFile(String file, String str) {
		try {
			Path path = Paths.get(file);
			if (!path.toFile().exists()) {
				path.getParent().toFile().mkdirs();
				Files.write(path, str.getBytes("UTF-8"));
				logger.info("{} is create", path.getFileName());
			} else {
				logger.info("{} is exists", path.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	public void setTable(IntrospectedTable introspectedTable) {
		this.introspectedTable = introspectedTable;
		IntrospectedColumn deletedColumn = introspectedTable.getColumn("is_deleted");
		if (deletedColumn != null) {
			hasDeleteColumn = true;
		}
		if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
			setPK(new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()));
		} else if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
			IntrospectedColumn column = introspectedTable.getPrimaryKeyColumns().get(0);
			setPK(column.getFullyQualifiedJavaType());
		}
		setModel(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
	}

	private void setModel(FullyQualifiedJavaType modelType) {
		modelName = modelType.getShortNameWithoutTypeArguments();
		modelPackage = modelType.getPackageName();
	}

	private void setPK(FullyQualifiedJavaType PK) {
		hasPK = true;
		String pkClassName = PK.getFullyQualifiedNameWithoutTypeParameters();

		if (pkClassName.matches("java\\.lang\\.[a-zA-Z0-9]+")) {
			pkTypeShortName = PK.getShortName();
		} else {
			pkType = pkClassName;
			pkTypeShortName = PK.getShortName();
		}
	}

	private String getDaoPath() {
		return getBasePath() +File.separatorChar + getPackagePath() + File.separatorChar + modelName + daoSuffix + ".java";
	}

	private String getDaoImplPath() {
		return getBasePath() +File.separatorChar + getPackagePath() + File.separatorChar + "impl" + File.separatorChar + modelName + daoSuffix + "Impl.java";
	}

	private String getPackagePath() {
		return daoPackage.replace('.', File.separatorChar);
	}

	private String getBasePath() {
		return modelBasePath;
	}

}
