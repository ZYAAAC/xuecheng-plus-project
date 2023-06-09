package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import com.xuecheng.media.model.dto.BindTeachplanMediaDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Resource
    TeachplanMapper teachplanMapper;

    @Resource
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {

        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id!=null){
            //修改
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else{
            //新增
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);

        }

    }

    /**
     * 向下移动，id是课程id
     * @param id
     */
    @Override
    public void movedownTeachplan(Long id) {
        //先通过id判断这个是父还是子
        Teachplan teachplan = teachplanMapper.selectById(id);
        if (teachplan.getParentid() == 0){
            //如果是父的话找出该courseid下的所有父id
            QueryWrapper<Teachplan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parentid",0).eq("course_id",teachplan.getCourseId());
            List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
            //将两个的orberby切换一下
            try{
                teachplans.stream().forEach(res -> {
                    //如果res的orderby是2，teachplan是1
                    if (res.getOrderby() == teachplan.getOrderby()+1){
                        //res减1
                        res.setOrderby(teachplan.getOrderby());
                        teachplanMapper.updateById(res);
                        //teachplan+1
                        teachplan.setOrderby(teachplan.getOrderby()+1);
                        teachplanMapper.updateById(teachplan);
                        throw new XueChengPlusException("终止foreach");
                    }
                });
            }catch (Exception e){

            }
        }
        //如果是子找出所有父id一样的id
        QueryWrapper<Teachplan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentid",teachplan.getParentid());
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
        //修改orderby
        try{
            teachplans.stream().forEach(res -> {
                if (res.getOrderby().equals(teachplan.getOrderby()+1)){
                    //res减1
                    res.setOrderby(teachplan.getOrderby());
                    teachplanMapper.updateById(res);
                    //teachplan+1
                    teachplan.setOrderby(teachplan.getOrderby()+1);
                    teachplanMapper.updateById(teachplan);
                    throw new XueChengPlusException("终止foreach");
                }

            });
        }catch (Exception e){

        }


    }

    /**
     * 向上移动，id是课程id
     * @param id
     */
    @Override
    public void moveupTeachplan(Long id) {
        //先通过id判断这个是父还是子
        Teachplan teachplan = teachplanMapper.selectById(id);
        if (teachplan.getParentid() == 0){
            //如果是父的话找出该courseid下的所有父id
            QueryWrapper<Teachplan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parentid",0).eq("course_id",teachplan.getCourseId());
            List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
            //将两个的orberby切换一下
            try{
                teachplans.stream().forEach(res -> {
                    //如果res的orderby是2，teachplan是1
                    if (res.getOrderby() == teachplan.getOrderby()-1){
                        //res+1
                        res.setOrderby(teachplan.getOrderby());
                        teachplanMapper.updateById(res);
                        //teachplan-1
                        teachplan.setOrderby(teachplan.getOrderby()-1);
                        teachplanMapper.updateById(teachplan);
                        throw new XueChengPlusException("终止foreach");
                    }
                });
            }catch (Exception e){

            }
        }
        //如果是子找出所有父id一样的id
        QueryWrapper<Teachplan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentid",teachplan.getParentid());
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
        //修改orderby
        try{
            teachplans.stream().forEach(res -> {
                if (res.getOrderby().equals(teachplan.getOrderby()-1)){
                    //res减1
                    res.setOrderby(teachplan.getOrderby());
                    teachplanMapper.updateById(res);
                    //teachplan+1
                    teachplan.setOrderby(teachplan.getOrderby()-1);
                    teachplanMapper.updateById(teachplan);
                    throw new XueChengPlusException("终止foreach");
                }

            });
        }catch (Exception e){

        }



    }

    @Override
    public void deleteTeachplan(Long teachplanId) {
        //先判断他是大章节还是小章节,也就是对应的parentid是否为0
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan.getParentid() == 0){
            //如果是大章节判断是否有小章节
            QueryWrapper<Teachplan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parentid",teachplanId);
            List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
            //如果有小章节则提示无法删除
            if (teachplans != null){
                throw new XueChengPlusException("没有小章节时方可删除");
            }
        }
        teachplanMapper.deleteById(teachplanId);
    }



    /**
     * @description 获取最新的排序号
     * @param courseId  课程id
     * @param parentId  父课程计划id
     * @return int 最新排序号
     * @author Mr.M
     * @date 2022/9/9 13:43
     */
    private int getTeachplanCount(long courseId,long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            XueChengPlusException.cast("教学计划不存在");
        }
        Integer grade = teachplan.getGrade();
        if(grade!=2){
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }
        //课程id
        Long courseId = teachplan.getCourseId();

        //先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));

        //再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

}
