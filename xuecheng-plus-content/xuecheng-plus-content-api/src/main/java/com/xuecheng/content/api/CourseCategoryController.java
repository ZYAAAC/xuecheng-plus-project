package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
/**
 * <p>
 * 数据字典 前端控制器
 * </p
 */

@RestController
@CrossOrigin
@Slf4j
@Api(value = "课程新增管理接口",tags = "课程新增管理接口")
public class CourseCategoryController {

    @Resource
    CourseCategoryService courseCategoryService;

    @ApiOperation("课程分类接口")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        return courseCategoryService.queryTreeNodes("1");
    }

}
