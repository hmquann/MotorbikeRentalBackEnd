package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.BlogDto;
import com.MotorbikeRental.entity.Blog;
import com.MotorbikeRental.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/createBlog")
    public ResponseEntity<Blog> createBlog(@RequestBody BlogDto blogDto) {
        Blog createdBlog = blogService.createBlog(blogDto);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @PutMapping("/updateBlog/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody BlogDto blogDto) {
        Blog updatedBlog = blogService.updateBlog(id, blogDto);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }

    @GetMapping("/getAllBlogs")
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        List<BlogDto> blogs = blogService.getAllBlog();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBlog/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getBlog/{id}")
    public ResponseEntity<BlogDto> createBlog(@PathVariable Long id) {
        BlogDto blogDto = blogService.getBlogById(id);
        return new ResponseEntity<>(blogDto, HttpStatus.CREATED);
    }


}
