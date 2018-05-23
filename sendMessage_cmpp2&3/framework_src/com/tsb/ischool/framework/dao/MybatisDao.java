package com.tsb.ischool.framework.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;

import com.tsb.ischool.framework.bean.comm.PageBean;



/**
 * 类描述：
 * @author 
 * @date   2014-3-14 下午5:32:38
 * @version 1.0
 */

public abstract class MybatisDao {
	
	@Resource
	protected SqlSessionTemplate sqlSession;
	
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSession) {
		
		this.sqlSession = sqlSession;
	}
	
	
	

	public Object selectOne(String statement){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectOne(statement);
	}
	
	public Object selectOne(String statement , Object parameter){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectOne(statement, parameter);
	}
	
	public List selectList(String statement){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectList(statement);
	}	
	
	public List selectList(String statement , Object parameter){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectList(statement,parameter);
	}
	
	public List selectList(String statement , Object parameter ,RowBounds rowBounds){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectList(statement,parameter ,rowBounds);
	}
	
	public Map<Object, Object> selectMap(String statement , String mapKey){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectMap(statement, mapKey);
	}
		
	public Map<Object, Object> selectMap(String statement ,Object parameter ,String mapKey){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectMap(statement, parameter, mapKey);
	}
	
	public Map<Object, Object> selectMap(String statement ,Object parameter ,String mapKey ,RowBounds rowBounds){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		return sqlSession.selectMap(statement, parameter, mapKey, rowBounds);
	}
		
	public void select(String statement ,ResultHandler handler){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		sqlSession.select(statement ,handler);
	}
		
	public void select(String statement, Object parameter, ResultHandler handler){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		sqlSession.select(statement, parameter, handler);
	}
	
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_READ);
		sqlSession.select(statement, parameter, rowBounds, handler);
	}
	
	
	public int update(String statement){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.update(statement);
	}
	
	public int update(String statement, Object parameter){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.update(statement, parameter);
	}
	
	
	public int insert(String statement){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.insert(statement);
	}
	
	public int insert(String statement, Object parameter){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.insert(statement, parameter);
	}
	
	
	public int delete(String statement){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.delete(statement);
	}
	
	public int delete(String statement, Object parameter){
		//DataSourceContextHolder.setDbType(DataSourceContextHolder.TYPE_WRITE);
		return sqlSession.delete(statement, parameter);
	}
	
	
	public <T> PageBean<T> selectPage(String statement,Object parameter,int currentPage ,int pageSize){		
		
		String statement_count = statement + "_count";
		
		int totalRecord = (Integer) selectOne(statement_count,parameter);
		
//		int offset = (currentPage - 1) * pageSize + 1;
		int offset = (currentPage - 1) * pageSize;
		int limit = pageSize;
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<T> _list = selectList(statement, parameter, rowBounds);
		
		PageBean<T> pageBean = PageBean.newPageBean(pageSize, currentPage, totalRecord);
		pageBean.setDataList(_list);
		
		return pageBean;
		
	}
}
