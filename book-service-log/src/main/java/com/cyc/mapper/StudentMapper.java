package com.cyc.mapper;

import com.cyc.common.po.Student;

import tk.mybatis.mapper.common.Mapper;





public interface StudentMapper extends Mapper<Student>{

  public long insertPo(Student student);
}