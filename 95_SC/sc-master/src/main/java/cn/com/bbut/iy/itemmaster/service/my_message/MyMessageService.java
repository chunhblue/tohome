package cn.com.bbut.iy.itemmaster.service.my_message;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageGridDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageParamDTO;

public interface MyMessageService {

    GridDataDTO<MyMessageGridDTO> getMessageList(MyMessageParamDTO messParam);
}
