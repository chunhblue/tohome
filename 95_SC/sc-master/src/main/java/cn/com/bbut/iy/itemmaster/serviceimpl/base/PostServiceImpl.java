package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.ArrayList;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dao.MA4160Mapper;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import cn.com.bbut.iy.itemmaster.entity.MA4160Example;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyPost;
import cn.com.bbut.iy.itemmaster.entity.base.IyPostExample;
import cn.com.bbut.iy.itemmaster.service.base.PostService;
import cn.shiy.common.baseutil.Container;

/**
 * @author songxz
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MA4160Mapper postMapper;

    @Override
    public List<IyPost> getPostByParam(String param) {
        /*MA4160Example example = new MA4160Example();
        example.or().andJobTypeCdLike("%" + param + "%");
        example.or().andJobCatagoryNameLike("%" + param + "%");
        example.setOrderByClause("job_type_cd");*/
        // List<MA4160> list1 = postMapper.selectByExample(example);
        List<MA4160> list1 = postMapper.selectPostByParam(param);
        List<IyPost> list2 = new ArrayList<>();
        for(MA4160 list:list1){
            IyPost dto = new IyPost();
            dto.setPostCode(list.getJobTypeCd());
            dto.setPostName(list.getJobCatagoryName());
            list2.add(dto);
        }
        return list2;
    }

    @Override
    public List<AutoCompleteDTO> getPostAutoByParam(String param) {
        PostService service = Container.getBean(PostService.class);
        List<IyPost> list = service.getPostByParam(param);
        List<AutoCompleteDTO> resList = new ArrayList<AutoCompleteDTO>();
        if (list != null && list.size() > 0) {
            for (IyPost ls : list) {
                AutoCompleteDTO t = new AutoCompleteDTO();
                t.setK(ls.getPostCode());
                t.setV(ls.getPostCode() + " " + ls.getPostName().trim());
                resList.add(t);
            }
        }
        return resList;
    }

    @Override
    public LabelDTO getName(String postCode) {
        MA4160Example example = new MA4160Example();
        example.or().andJobTypeCdEqualTo(postCode);
        List<MA4160> list = postMapper.selectByExample(example);
        LabelDTO rest = new LabelDTO();
        if (list != null && list.size() > 0) {
            MA4160 p = list.get(0);
            rest.setCode(p.getJobTypeCd());
            rest.setName(p.getJobCatagoryName());
            rest.setCodeName(p.getJobTypeCd() + " " + p.getJobCatagoryName().trim());
        }
        return rest;
    }

}
