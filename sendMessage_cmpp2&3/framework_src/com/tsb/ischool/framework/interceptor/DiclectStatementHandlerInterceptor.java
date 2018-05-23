package com.tsb.ischool.framework.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

@Intercepts(@Signature(type=StatementHandler.class, method="prepare", args={Connection.class}))
public class DiclectStatementHandlerInterceptor implements Interceptor {
	private static ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static String ORACLE_CLASS = "org.hibernate.dialect.OracleDialect";
	private static String MYSQL_CLASS = "org.hibernate.dialect.MySQL5Dialect";
	private Logger logger = Logger.getLogger(DiclectStatementHandlerInterceptor.class.getName());
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();  
	     MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,  
	     DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);  
	     // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环  
	     // 可以分离出最原始的的目标类)  
	     while (metaStatementHandler.hasGetter("h")) {  
	         Object object = metaStatementHandler.getValue("h");  
	         metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,   
	         DEFAULT_OBJECT_WRAPPER_FACTORY);  
	     }  
	     // 分离最后一个代理对象的目标类  
	     while (metaStatementHandler.hasGetter("target")) {  
	         Object object = metaStatementHandler.getValue("target");  
	         metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,   
	         DEFAULT_OBJECT_WRAPPER_FACTORY);  
	     }  
	     Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");  
	     String dialectStr = configuration.getVariables().getProperty("dialect");  
	     if (null == dialectStr || "".equals(dialectStr)) {  
	           
	         dialectStr = "mysql";  
	     }
	     RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");
	     BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
         String sql = boundSql.getSql();
        if (rowBounds.getLimit() > 0
                && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT)
        {
        	
            System.out.println(sql);
           
            sql = buildPageSqlForMysql(sql, rowBounds.getOffset(), rowBounds.getLimit());
//            System.out.println(sql);
            metaStatementHandler.setValue("delegate.boundSql.sql", sql);
            // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数  
            metaStatementHandler.setValue("delegate.rowBounds.offset",   
            RowBounds.NO_ROW_OFFSET);  
            metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT); 
            
        }
        
        logger.info("THREADID = "+Thread.currentThread().getId()+".|执行sql:"+sql);
        return invocation.proceed();
		
	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的  
	    // 次数  
	    if (target instanceof StatementHandler) {  
	        return Plugin.wrap(target, this);  
	    } else {  
	        return target;  
	    }  
	}

	@Override
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub

	}
	public String buildPageSqlForMysql(String sql, int offset, int size) {  
	    StringBuilder pageSql = new StringBuilder();  
	    
	    pageSql.append(sql);  
	    pageSql.append(" limit " + offset + "," + size);  
	    return pageSql.toString();  
	} 
//	private Dialect buildPageSql(int dialect) {
//		String className = MYSQL_CLASS;
//		switch(dialect) {
//		case 1 :
//			className = MYSQL_CLASS;
//			break;
//		case 2 :
//			className = ORACLE_CLASS;
//			break;
//		}
//		Dialect retDialect = null;
//		try {
//			retDialect = (Dialect)Class.forName(className).newInstance();
//		}catch(Exception e) {
//			e.printStackTrace();
//			
//		}
//		return retDialect;
//	}
//	private Dialect buildPageSql(String dialect) {
//		String className = MYSQL_CLASS;
//		switch(dialect) {
//		case "mysql" :
//			className = MYSQL_CLASS;
//			break;
//		case "oracle" :
//			className = ORACLE_CLASS;
//			break;
//		}
//		Dialect retDialect = null;
//		try {
//			retDialect = (Dialect)Class.forName(className).newInstance();
//		}catch(Exception e) {
//			e.printStackTrace();
//			
//		}
//		return retDialect;
//	}

}
