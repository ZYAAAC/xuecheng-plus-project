package com.xuecheng.media.service.impl;

import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Service
public class MediaFileProcessServiceImpl implements MediaFileProcessService {

    @Resource
    MediaFilesMapper mediaFilesMapper;

    @Resource
    MediaProcessMapper mediaProcessMapper;

    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        List<MediaProcess> mediaProcesses = mediaProcessMapper
                .selectListByShardIndex(shardTotal, shardIndex, count);
        return mediaProcesses;
    }

    @Override
    public boolean startTask(long id) {
        int result = mediaProcessMapper.startTask(id);
        return result<=0?false:true;
    }
}
