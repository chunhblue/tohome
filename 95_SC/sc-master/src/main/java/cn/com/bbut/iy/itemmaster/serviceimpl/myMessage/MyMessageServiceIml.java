package cn.com.bbut.iy.itemmaster.serviceimpl.myMessage;

import cn.com.bbut.iy.itemmaster.dao.MyMessageMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageGridDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageParamDTO;
import cn.com.bbut.iy.itemmaster.service.my_message.MyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MyMessageServiceIml")
public class MyMessageServiceIml implements MyMessageService {

    @Autowired
    private MyMessageMapper myMessageMapper;
    @Override
    public GridDataDTO<MyMessageGridDTO> getMessageList(MyMessageParamDTO messParam) {
        List<MyMessageGridDTO> list = myMessageMapper.getShortcutList(messParam);
        int count = myMessageMapper.countMessage(messParam);

        return new GridDataDTO<>(list,messParam.getPage(),messParam.getRows(),count);
    }
}
