package com.example.springnews.controller;

import com.example.springnews.model.News;
import com.example.springnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class NewsController {
    @Autowired
    NewsRepository newsRepository;

    @GetMapping(value="/newsmain")
    public ModelAndView newsMain(){
        List<News> list = newsRepository.findAllByOrderByIdDesc();
        ModelAndView mav = new ModelAndView();
        if(list.size() != 0){
            mav.addObject("list",list);
        }else{
            mav.addObject("msg","작성된 글이 없습니다.");
        }
        mav.setViewName("newsmain");
        return mav;
    }

    @GetMapping(value="/one/{newsid}")
    public ModelAndView newsDetail(@PathVariable("newsid") Integer id){
        Optional<News> list = newsRepository.findById(id);
        Integer cntRaw = list.get().getCnt() ;
        Integer cntUp = cntRaw + 1;
//        System.out.println(cntRaw);
//        System.out.println(cntUp);
        list.get().setCnt(cntUp);
        newsRepository.save(list.get());
        ModelAndView mav = new ModelAndView();
        mav.addObject("list",list);
        mav.setViewName("newsDetail");
        return mav;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(@RequestParam("keyword") String keyword){
        List<News> list = newsRepository.findByTitleContainsOrderByIdDesc(keyword);
        ModelAndView mav = new ModelAndView();
        if(list.size() != 0){
            mav.addObject("list",list);
        }else{
            mav.addObject("msg","검색된 결과가 없습니다.");
        }
        mav.setViewName("newsmain");
        return mav;
    }

    @PostMapping(value="/insert")
    public String newsInsert(News news){
        newsRepository.save(news);
        return "redirect:/newsmain";
    }

    @GetMapping(value="/delete/{newsid}")
    public String newsDelete(@PathVariable("newsid") Integer id){
        newsRepository.deleteById(id);
        return "redirect:/newsmain";
    }
    @PostMapping(value="/update/{newsid}")
    public ModelAndView newsUpdate(@PathVariable("newsid") Integer id,News news){
        ModelAndView mav = new ModelAndView();
        News target = newsRepository.findById(id).get();
        target.setTitle(news.getTitle());
        target.setContent(news.getContent());
        newsRepository.save(target);
        mav.setViewName("redirect:/newsmain");
        return mav;
    }

}
