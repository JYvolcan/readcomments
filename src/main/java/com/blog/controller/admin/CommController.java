package com.blog.controller.admin;

import com.blog.pojo.Comment;
import com.blog.pojo.Tag;
import com.blog.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * bug描述：当用户在提交一样的评论后会重定向到修改界面，
 * 而修改界面的from表单里面有个三目运算，表示如果comment为空即就按新增评论的方法执行
 * 这就会到，修改不成功，反而新增加了应该评论
 */
@Controller
@RequestMapping("/admin")
public class CommController {

    @Autowired
    private CommentService commentService;

//做中转
/*------------------bug开始--------------------------------------------------------------------*/
    @GetMapping("/comments/input")
    public String toAddComment(Model model,Comment c){
        model.addAttribute("comment", new Comment());   //问题出处这里是一个空的，应该用之前页面传递过来的对象
        return "admin/comments-input";
    }
    @PostMapping("/comments")
    public String addComment(Comment comment, RedirectAttributes attributes){   //新增
        Comment t = commentService.getCommentByContent(comment.getContent());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的标签");
            return "redirect:/admin/comments/input";
        }else {
            attributes.addFlashAttribute("msg", "添加成功");
        }
        commentService.saveComment(comment);
        return "redirect:/admin/tags";   //不能直接跳转到tags页面，否则不会显示tag数据(没经过tags方法)
    }
/*-----------------bug结束-----------------------------------------------------------------------*/

    @GetMapping("/comments")
    public String comments(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 5);
        List<Comment> allComment = commentService.getAllComment();
        //得到分页结果对象
        PageInfo<Comment> pageInfo = new PageInfo<>(allComment);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/comments";
    }
    @GetMapping("/comments/{id}/input")
    public String toEditComment(@PathVariable Long id, Model model){
        model.addAttribute("comment", commentService.getComment(id));
        return "admin/comments-input";
    }

    //修改
    @PostMapping("/comments/{id}")
    public String editComment(@PathVariable Long id, Comment comment, RedirectAttributes attributes){  //修改

        Comment t = commentService.getCommentByContent(comment.getContent());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的评论");
            /*--------------------------产生bug的地方--------------------------------------------*/
            return "redirect:/admin/comments/input";
            /*---------------------------------------------------------------------------------*/
        }else {
            attributes.addFlashAttribute("msg", "修改成功");
        }
       commentService.updateComment(comment);
        return "redirect:/admin/comments";   //不能直接跳转到comments页面，否则不会显示comment数据(没经过comments方法)
    }

    //删除
    @GetMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        commentService.deleteComment(id);
        attributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/comments";
    }
}
