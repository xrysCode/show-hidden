package com.datalevel.showhiddencontrol.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.other.dto.MenusTreeDto;
import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xry
 * @since 2021-11-03
 */
public interface IOtherMenusService extends IService<OtherMenusEntity> {

    List<MenusTreeDto> queryMenusTree();

    void shiftMenu(TreeShiftDto treeShiftDto);
}
