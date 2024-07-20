package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.BlogDto;
import com.MotorbikeRental.entity.Blog;
import com.MotorbikeRental.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/create")
    public ResponseEntity<Blog> createBlog(
            @RequestParam("title") String title,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        String filePath = null;

        if (file != null && !file.isEmpty()) {
            try {
                if (!Files.exists(Paths.get(UPLOAD_DIR))) {
                    Files.createDirectories(Paths.get(UPLOAD_DIR));
                }
                filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename()).toString();
                Files.write(Paths.get(filePath), file.getBytes());
                // Read file content and set it to content
                content = new String(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        BlogDto blogDto = new BlogDto();
        blogDto.setTitle(title);
        blogDto.setUserId(userId);
        blogDto.setContent(content);
        blogDto.setWordFilePath(filePath);

        Blog createdBlog = blogService.createBlog(blogDto);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody BlogDto blogDto) {
        Blog updatedBlog = blogService.updateBlog(id, blogDto);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }

    @GetMapping("/getAllBlogs")
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        List<BlogDto> blogs = blogService.getAllBlog();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
