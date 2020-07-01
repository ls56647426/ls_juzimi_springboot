package com.ls.juzimi.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.Work;

public interface WorkService {

	public Integer addWork(Work work);

	public Work findWorkById(Integer id);

	public Integer updateWork(Work work);

	public void deleteWorkById(Integer id);

	public Page<Work> findWorkAllPageAndSort(Integer page, Integer size, Direction dir, String pro);

	public List<Work> findWorkByUid(Integer uid);

}
