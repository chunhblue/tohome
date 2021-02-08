/**
 * ClassName  SystemRuntimeException
 *
 * History
 * Create User: Shiy
 * Create Date: 2014年2月25日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 系统运行时错误
 * 
 * @author Shiy
 */
@ControllerAdvice
public class SystemRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public SystemRuntimeException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SystemRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public SystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public SystemRuntimeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SystemRuntimeException(Throwable cause) {
        super(cause);
    }

}
