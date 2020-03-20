package com.kuang.service;

import com.kuang.dao.SceneMapper;
import com.kuang.pojo.Scene;

import java.util.List;

public class SceneServiceImpl implements SceneService{

    private SceneMapper sceneMapper;

    public void setSceneMapper(SceneMapper sceneMapper) {
        this.sceneMapper = sceneMapper;
    }

    public List<Scene> selectByType(String type) {
        return sceneMapper.selectByType(type);
    }

    public List<Scene> selectAll() {
        return sceneMapper.selectAll();
    }
}
