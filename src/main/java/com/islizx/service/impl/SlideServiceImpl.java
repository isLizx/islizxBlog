package com.islizx.service.impl;

import com.islizx.entity.Slide;
import com.islizx.mapper.SlideMapper;
import com.islizx.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-15 - 23:38
 */
@Service
public class SlideServiceImpl implements SlideService {

    @Autowired(required = false)
    SlideMapper slideMapper;

    @Override
    public List<Slide> findAll() {
        return slideMapper.findAll();
    }

    @Override
    public Slide insertOrUpdate(Slide slide) {
        if(slide.getId() == null || slide.getId() == 0){
            slideMapper.insert(slide);
        }else{
            slideMapper.update(slide);
        }
        return slide;
    }

    @Override
    public Slide get(Integer id) {
        return slideMapper.getSlideById(id);
    }

    @Override
    public Integer delete(Integer id) {
        return slideMapper.deleteSlide(id);
    }
}
