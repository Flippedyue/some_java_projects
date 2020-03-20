package com.kuang.service;

import com.kuang.pojo.Scene;

import java.util.List;

public interface SceneService {

    // 查询一种类型的情境
    List<Scene> selectByType(String type);

    // 查询所有情境
    List<Scene> selectAll();
}
