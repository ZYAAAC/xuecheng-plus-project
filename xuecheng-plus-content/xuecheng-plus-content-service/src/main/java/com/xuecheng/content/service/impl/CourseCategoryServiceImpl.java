package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Resource
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //调用mapper递归查询出分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtos =
                courseCategoryMapper.selectTreeNodes(id);
        //找到每个节点的子节点，最终封装成List<CourseCategoryTreeDto>
        //先将List转成map，key就是节点id，value就是CourseCategoryTreeDto对象，目的是为了方便从map获取节点
        //filter(item -> !id.equals(item.getId())) 把根节点排除
        //tomap(key,value,当key有冲突选择哪一个)
        //key -> key.geetId() key的值为集合的id
        //value -> value value的值为原集合
        //(key1, key2) -> key2 当key冲突时选择后面的key
//        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream()
//                .filter(item -> !id.equals(item.getId()))
//                .collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
//        //定义一个List作为最终返回的list
//        ArrayList<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();
//        //从头遍历 List<CourseCategoryTreeDto>,一边遍历一边找子节点放在父节点的childrenTreeNodes
//        courseCategoryTreeDtos.stream().
//                filter(item -> !id.equals(item.getId())).
//                forEach(item -> {
//                    //如果item的父节点为传进来的id(id为1)，那么这个时候item的id为1-1，1-2这些，就存进去集合
//                    if (item.getParentid().equals(id)){
//                        courseCategoryList.add(item);
//                    }
//                    //找到当前节点的父节点
//                    CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
//                    if(courseCategoryTreeDto!=null){
//                        if(courseCategoryTreeDto.getChildrenTreeNodes() ==null){
//                            courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
//                        }
//                        //下边开始往ChildrenTreeNodes属性中放子节点
//                        courseCategoryTreeDto.getChildrenTreeNodes().add(item);
//                    }
//
//                });

        return courseCategoryTreeDtos;
    }
}
