package com.blog.service;

import com.blog.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {
    //根据创建时间倒序来排
    List<Comment> getCommentByBlogId(Long blogId);
    //添加一个评论
    int saveComment(Comment comment);
    //查询父级对象
    Comment findByParentCommentId(@Param("parentCommentId")Long parentcommentid);

    //以下为后台管理


    List<Comment> getAllComment();

    Comment getCommentByContent(String content);
    int updateComment(Comment comment);

    int deleteComment(Long id);

   Comment   getComment(Long id);
}
