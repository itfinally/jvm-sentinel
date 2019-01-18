package io.github.itfinally.jvm.repository.mapper;

import io.github.itfinally.jvm.entity.JvmThreadInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import io.github.itfinally.mybatis.jpa.mapper.BasicCrudMapper;

@Mapper
public interface JvmThreadInfoMapper extends BasicCrudMapper<JvmThreadInfoEntity> {
}