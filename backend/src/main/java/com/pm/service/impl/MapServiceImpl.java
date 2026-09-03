package com.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.entity.PmProject;
import com.pm.mapper.PmProjectMapper;
import com.pm.service.MapService;
import com.pm.vo.map.ProjectMapVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final PmProjectMapper projectMapper;

    @Override
    public List<ProjectMapVO> getProjects() {
        List<PmProject> projects = projectMapper.selectList(
                new LambdaQueryWrapper<PmProject>()
                        .isNotNull(PmProject::getLongitude)
                        .isNotNull(PmProject::getLatitude)
        );
        return projects.stream().map(p -> {
            ProjectMapVO vo = new ProjectMapVO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
