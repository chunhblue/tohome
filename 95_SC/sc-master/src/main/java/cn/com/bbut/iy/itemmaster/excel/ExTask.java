/**
 * ClassName  ExTask
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-7-5
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.File;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shiy
 */
@Slf4j
public class ExTask implements Callable<ExStatus> {

    private ExService service;

    private BaseExcelParam param;

    public ExTask(ExService service, BaseExcelParam param) {
        this.service = service;
        this.param = param;

    }

    /**
     * @see Callable#call()
     */
    @Override
    public ExStatus call() throws Exception {
        log.debug("starting...{}", service);
        ExtendExService extendExService = null;
        if (service instanceof ExtendExService) {
            extendExService = (ExtendExService) service;
        }
        if (extendExService != null) {
            extendExService.preExcelExcel(param);
        }
        File file = this.service.getExcel(param);
        if (file.isFile()) {
            if (extendExService != null) {
                extendExService.postExcel(param);
            }
            return new ExStatus(ExStatus.STATUS_FINISHED, file.getName(), param.getExFileName());
        } else {
            return new ExStatus(ExStatus.STATUS_FAILED, null, param.getExFileName());
        }
    }
}
