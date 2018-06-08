package com.rain.qiong.common.service;


import com.rain.qiong.common.domain.LogDO;
import com.rain.qiong.common.domain.PageDO;
import com.rain.qiong.common.utils.Query;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
