package com.naver.reservation.detail.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.naver.reservation.detail.dto.DisplayInfo;

import static com.naver.reservation.detail.dao.DisplayInfoDaoSqls.*;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

@Repository
public class DisplayInfoDao {
	NamedParameterJdbcTemplate jdbc;
	RowMapper<DisplayInfo> rowMapper = BeanPropertyRowMapper.newInstance(DisplayInfo.class);
	
	public DisplayInfoDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<DisplayInfo> getDisplayInfos(int id){
		List<DisplayInfo> list = jdbc.query(SELECT, Collections.singletonMap("id", id), rowMapper);
		return list;
	}

}
