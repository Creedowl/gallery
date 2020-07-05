package com.creedowl.gallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.creedowl.gallery.model.UploadFile;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFIleMapper extends BaseMapper<UploadFile> {
    @Update("UPDATE upload_file SET deleted=#{deleted} WHERE user_id = #{userId}")
    void updateByUserId(Long userId, Boolean deleted);
}
