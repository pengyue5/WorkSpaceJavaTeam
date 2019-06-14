package com.bbjh.common.utils;

import com.github.pagehelper.PageInfo;
import com.bbjh.common.api.Page;

import java.util.List;

/**
 * @author fwb
 * @Description
 * @date 2019/5/23
 */
public class PageConvertUtil {

    /**
     * PageHelper的PageInfo转成自定义的Page对象
     * @param values
     * @return
     */
    public static Page convert(List<?> values)  {
        PageInfo<?> pageInfo = new PageInfo<>(values);
        return new Page()
                .setPageNum(pageInfo.getPageNum()).
                setPageSize(pageInfo.getPageSize())
                .setTotal(Integer.valueOf(pageInfo.getTotal()+""));
    }
}
