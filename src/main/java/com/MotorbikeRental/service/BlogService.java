package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.BlogDto;
import com.MotorbikeRental.dto.BookingDto;
import com.MotorbikeRental.entity.Blog;

import java.util.List;

public interface BlogService {

    Blog createBlog(BlogDto blogDto);

    Blog updateBlog(Long id, BlogDto blogDTO);
    List<BlogDto> getAllBlog();
    List<BlogDto> getAllBlogByUserId(Long id);

    BlogDto getBlogById(Long id);

    void deleteBlog(Long id);


}
