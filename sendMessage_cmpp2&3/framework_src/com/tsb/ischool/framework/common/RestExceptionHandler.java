package com.tsb.ischool.framework.common;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import com.tsb.ischool.framework.bean.comm.ResultBean;
import com.tsb.ischool.framework.exception.ISchoolException;

/**
 * 
 * @author zkl
 * @date 2014-7-17 下午3:48:17
 * @version 1.0
 */
@Controller
@Provider  
public class RestExceptionHandler implements ExceptionMapper<Exception> {  
	private Logger logger = Logger.getLogger(RestExceptionHandler.class);
    @Override  
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response toResponse(Exception e) {          
        //ResultDto ret = ResultBuilder.buildResultStr(ResultBuilder.FAIL_CODE, null, "-1", e.getMessage()); 
    	String operation = "出现restful全局异常";
    	ResultBean ret = null;
    	logger.error(operation, e);
    	try {
			ISchoolException iSchoolException = (ISchoolException)e;
			ret= new ResultBean(0,iSchoolException.getPerrorcode(),"",iSchoolException.getPerrormessage());
		} catch (Exception e1) {
			ret = new ResultBean(0,-10000,"",e.getMessage());
			logger.info(ISchoolConstants.LOGGER_PREFIX_ERROR + operation + "e.getMessage()="+e.getMessage());
			e.printStackTrace();
		}
    	
        return Response.ok(ret, MediaType.APPLICATION_JSON).build();  
    }  
} 