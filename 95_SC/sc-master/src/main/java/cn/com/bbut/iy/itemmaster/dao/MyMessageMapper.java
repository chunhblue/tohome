package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageGridDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageParamDTO;

import java.util.List;

public interface MyMessageMapper {

    List<MyMessageGridDTO> getShortcutList(MyMessageParamDTO messParam);

    int countMessage(MyMessageParamDTO messParam);
}