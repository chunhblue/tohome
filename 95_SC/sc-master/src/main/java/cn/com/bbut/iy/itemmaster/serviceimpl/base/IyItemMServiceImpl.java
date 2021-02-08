package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dao.IyItemMMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ItemStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemM;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemMExample;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemMExample.Criteria;
import cn.com.bbut.iy.itemmaster.service.base.IyItemMService;

/**
 * @author songxz
 */
@Slf4j
@Service
public class IyItemMServiceImpl implements IyItemMService {

    @Autowired
    IyItemMMapper itemMapper;

    @Override
    public List<AutoCompleteDTO> getItemLikeNameCode(String v, String... notv) {
        List<AutoCompleteDTO> rest = new ArrayList<>();
        IyItemMExample example = new IyItemMExample();
        Criteria c1 = example.or().andItem1Like("%" + v + "%");
        Criteria c2 = example.or().andItemNameLike("%" + v + "%");
        if (notv != null && notv.length > 0) {
            // 不可以以某内容开头的
            for (String n : notv) {
                c1.andItem1NotLike(n + "%");
                c2.andItem1NotLike(n + "%");
            }
        }
        example.setOrderByClause("item1");
        List<IyItemM> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            for (IyItemM ls : list) {
                AutoCompleteDTO d = new AutoCompleteDTO();
                d.setK(ls.getItem1());
                d.setV(ls.getItem1() + " " + ls.getItemName().trim());
                d.setHidek(ls.getItemSystem() + "&" + ls.getItemName().trim());
                rest.add(d);
            }
        }
        return rest;
    }

    @Override
    public boolean isItemExist(String item1) {
        IyItemMExample example = new IyItemMExample();
        example.or().andItem1EqualTo(item1);
        long count = itemMapper.countByExample(example);
        return count > 0 ? true : false;
    }

    @Override
    public ItemStoreDTO getItemCInfo(String itemSystem, String store) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("itemSystem", itemSystem);
        map.put("store", store);
        return itemMapper.selectItemInfoFromC(map);
    }

    @Override
    public IyItemM getItemByCode(String item1) {
        IyItemMExample example = new IyItemMExample();
        item1 = StringUtils.rightPad(item1, 13);
        example.or().andItem1EqualTo(item1);
        List<IyItemM> m = itemMapper.selectByExample(example);
        return m != null && m.size() > 0 ? m.get(0) : null;
    }

    @Override
    public IyItemM getItemInfoBySystem(String itemSystem) {
        IyItemMExample example = new IyItemMExample();
        example.or().andItemSystemEqualTo(itemSystem);
        List<IyItemM> m = itemMapper.selectByExample(example);
        return m != null && m.size() > 0 ? m.get(0) : null;
    }
}
