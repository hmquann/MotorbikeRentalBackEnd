package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.BlogDto;
import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.MotorbikeDto;
import com.MotorbikeRental.entity.Blog;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.BlogNotFoundException;
import com.MotorbikeRental.repository.BlogRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.BlogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private  BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public Blog createBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setWordFilePath(blogDto.getWordFilePath());

        Optional<User> userOptional = userRepository.findById(blogDto.getUserId());
        if (userOptional.isPresent()) {
            blog.setUser(userOptional.get());
        }

        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, BlogDto blogDTO) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setTitle(blogDTO.getTitle());
            blog.setContent(blogDTO.getContent());
            blog.setWordFilePath(blogDTO.getWordFilePath());
            return blogRepository.save(blog);
        } else {
            throw new BlogNotFoundException("Blog not found with id: " + id);
        }
    }

    @Override
    public List<BlogDto> getAllBlog() {
        List<Blog> blogList = blogRepository.findAll();
        List<BlogDto> blogDtoList = blogList.stream()
                .map(booking -> mapper.map(blogList, BlogDto.class))
                .collect(Collectors.toList());
        return blogDtoList;
    }

    @Override
    public List<BlogDto> getAllBlogByUserId(Long id) {
        return null;
    }

    @Override
    public BlogDto getBlogById(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        return mapper.map(blog, BlogDto.class);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
