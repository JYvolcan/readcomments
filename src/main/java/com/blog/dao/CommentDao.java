package com.blog.dao;

import com.blog.pojo.Comment;
import com.blog.pojo.Comment;
import com.blog.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {

    //根据创建时间倒序来排
    List<Comment> findByBlogIdAndParentCommentNull(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //查询父级对象
    Comment findByParentCommentId(@Param("parentCommentId")Long parentcommentid);

    //添加一个评论
    int saveComment(Comment comment);

    Comment   getComment(Long id);
    Comment getCommentByContent(String content);

//以下为后台管理
    List<Comment> getAllComment();


    int updateComment(Comment Comment);

    int deleteComment(Long id);
}
