package cn.com.bbut.iy.itemmaster.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dao.Ma1000Mapper;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.entity.Resource;
import cn.shiy.common.pmgr.entity.ResourceGroup;

/**
 * @author songxz
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private Ma1000Mapper storeMapper;
    @Autowired
    private IyRoleService iyRoleService;

    @Override
    public List<Ma1000> getAllStore() {
        Ma1000Example example = new Ma1000Example();
        List<Ma1000> list = storeMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getStoreAutoByParam(String param) {
        Ma1000Example example = new Ma1000Example();
        example.or().andStoreCdLike("%" + param + "%");
        example.or().andStoreNameLike("%" + param + "%");
        example.setOrderByClause("store_cd");
        List<Ma1000> list = storeMapper.selectByExample(example);
        List<AutoCompleteDTO> rest = new ArrayList<AutoCompleteDTO>();
        if (list != null && list.size() > 0) {
            for (Ma1000 ls : list) {
                AutoCompleteDTO t = new AutoCompleteDTO();
                t.setK(ls.getStoreCd());
                t.setV(ls.getStoreCd() + " " + ls.getStoreName().trim());
                rest.add(t);
            }
        }
        return rest;
    }

    @Override
    public LabelDTO getStoreName(String store) {
        LabelDTO rest = new LabelDTO();
        if (store != null && store.equals(ConstantsDB.ALL_STORE)) {
            rest.setCode(ConstantsDB.ALL_STORE);
            rest.setName(ConstantsDB.ALL_STORE_NAME);
            rest.setCodeName(ConstantsDB.ALL_STORE + " " + ConstantsDB.ALL_STORE_NAME);
            return rest;
        }
        Ma1000Example example = new Ma1000Example();
        example.or().andStoreCdEqualTo(store);
        List<Ma1000> list = storeMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            Ma1000 s = list.get(0);
            rest.setCode(s.getStoreCd());
            rest.setName(s.getStoreName().trim());
            rest.setCodeName(s.getStoreCd() + " " + s.getStoreName().trim());
        }
        return rest;
    }

    @Override
    public List<AutoCompleteDTO> getStoreByRoleAndPcode(Collection<Integer> roleIds, User u,
            String pcode) {
        Collection<ResourceGroup> resouces = iyRoleService.getResourcesByRoleAndPCode(
                (List<Integer>) roleIds, pcode);
        if (resouces == null || resouces.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> stores = null;
        // 是否存在全部店铺资源
        boolean isAllStroe = false;
        if (resouces != null && resouces.size() > 0) {
            // 拿到店铺资源
            stores = new ArrayList<>();
            for (ResourceGroup rGroup : resouces) {
                for (Resource rs : rGroup.getGroup()) {
                    switch (rs.getType()) {
                    case ConstantsDB.COMMON_FOUR:
                        String st = rs.getId();
                        if (!isAllStroe && ConstantsDB.ALL_STORE.equals(st)) {
                            isAllStroe = true;
                        }
//                        if (ConstantsDB.SELF_STORE.equals(st)) {
//                            // 如果是自店铺
//                            st = u.getStore();
//                        }
                        stores.add(st);
                        break;
                    }
                }
            }
        }
        if (stores == null || stores.size() == 0) {
            return null;
        }
        StoreService thisService = Container.getBean(StoreService.class);

        List<AutoCompleteDTO> allStroe = new ArrayList<>();
        if (isAllStroe) {
            // 资源中可能会存在 自店铺和全部店铺和其他各个店铺数据，所以这里需要进行解析
            // .andStoreOpenDateLessThanOrEqualTo(storeTime);
            // 得到所有标准店铺
            List<Ma1000> allStores = thisService.getAllStore();
            for (Ma1000 s : allStores) {
                AutoCompleteDTO dto = new AutoCompleteDTO();
                dto.setK(s.getStoreCd());
                dto.setV(s.getStoreName().trim());
                dto.setHidek(s.getStoreCd());
                allStroe.add(dto);
                // 去掉已经存在的店铺编号
                stores.remove(s.getStoreCd());
            }
        }
        List<AutoCompleteDTO> rest = new ArrayList<>();
        stores.sort(Comparator.reverseOrder());
        for (String s : stores) {
            AutoCompleteDTO dto = new AutoCompleteDTO();
            LabelDTO label = thisService.getStoreName(s);
            dto.setK(label.getCode());
            dto.setV(label.getName());
            dto.setHidek(label.getCode());
            // }
            rest.add(dto);
        }

        rest.addAll(allStroe);
        return rest;
    }

}
