package cn.com.bbut.iy.itemmaster.service.inform;


import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4311;
import java.util.Collection;
import java.util.List;

public interface Ma4311Service {

    /**
     * 条件获取全部Store权限<br/>
     * @param ma4311List
     */
    Collection<String> getAllStore(List<MA4311> ma4311List);

    /**
     * 根据通报id查询已添加店铺权限
     * @param informCd
     */
    List<RoleStoreDTO> selectListByInformCd(String informCd);
}
