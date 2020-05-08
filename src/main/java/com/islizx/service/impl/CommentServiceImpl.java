package com.islizx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Blog;
import com.islizx.entity.Comment;
import com.islizx.mapper.BlogMapper;
import com.islizx.mapper.CommentMapper;
import com.islizx.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-14 - 13:12
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private BlogMapper blogMapper;


    @Override
    public PageInfo<Comment> pageComment(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> commentList = commentMapper.findAll(criteria);
        for (int i = 0; i < commentList.size(); i++) {
            //封装Blog
            Blog blog = blogMapper.getBlogByPublishedAndId(null,null, commentList.get(i).getBlogId());
            commentList.get(i).setBlog(blog);

            // 封装parentComment
            Integer parentCommentId = commentList.get(i).getParentCommentId();
            if(parentCommentId != null){
                Comment parentComment = commentMapper.getById(null, parentCommentId);
                commentList.get(i).setParentComment(parentComment);
            }
        }
        return new PageInfo<>(commentList, 5);
    }

    @Override
    @CacheEvict(value= "comment", allEntries=true)
    public Comment updateCommentPass(Integer id, Integer pass) {
        //子评论随父评论状态一起改变
        //1.修改该评论状态
        Comment comment = commentMapper.getById(null, id);
        comment.setPass(pass);
        commentMapper.updateCommentPass(id, pass);

        //2.修改该评论的子评论状态
        List<Integer> childIds = commentMapper.selectChildCommentIds(id);
        childIds.forEach(childId -> commentMapper.updateCommentPass(childId, pass));

        //3.修改文章评论数
        blogMapper.updateCommentCount(comment.getBlogId());
        return comment;
    }

    @Override
    @CacheEvict(value= "comment", allEntries=true)
    public Comment insertComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
        blogMapper.updateCommentCount(comment.getBlogId());

        return comment;
    }

    @Override
    @CacheEvict(value= "comment", allEntries=true)
    public void deleteComment(Integer commentId) {
        Comment comment = commentMapper.getById(null, commentId);
        if (comment != null) {
            //1.删除评论
            commentMapper.deleteById(commentId);
            //2.修改文章的评论数量
            blogMapper.updateCommentCount(comment.getBlogId());
        }
    }

    @Override
    @Cacheable(value = "comment", key = "'getListCommentByBlogId_' + #blogPublished + '_' + #commentPublished + '_' + #postType + '_' + #pageIndex + '_' + #pageSize + '_' + #blogId")
    public PageInfo<Comment> getListCommentByBlogId(Integer blogPublished,Integer commentPublished, Integer postType, Integer pageIndex, Integer pageSize,Integer blogId) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> comments = commentMapper.findByBlogIdAndParentCommentNull(blogId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments, 5);
        List<Comment> commentsCopy = new ArrayList<>();
        for(Comment comment:pageInfo.getList()){
            commentsCopy.add(packagingComment(blogPublished, commentPublished, postType, comment));
        }
        /*return eachComment(commentsCopy);*/
        pageInfo.setList(eachComment(commentsCopy));
        return pageInfo;
    }

    @Override
    public Comment getById(Integer blogPublished,Integer commentPublished, Integer postType,Integer id) {
        Comment comment = commentMapper.getById(commentPublished, id);
        if(comment == null){
            return null;
        }
        return packagingComment(blogPublished,commentPublished, postType, comment);
    }

    @Override
    public Integer countCommentByPass(Integer pass) {
        return commentMapper.countComment(pass);
    }

    @Override
    public List<Comment> findByBatchIds(List<Integer> ids) {
        return commentMapper.selectByIds(ids);
    }

    @Override
    public List<Comment> getCommentLimit(Integer limit) {
        return commentMapper.findLatestCommentByLimit(limit);
    }

    /**
     * 封装评论
     * @param comment
     * @return
     */
    public Comment packagingComment(Integer blogPublished, Integer commentPublished, Integer postType,Comment comment){
        // 封装文章
        comment.setBlog(blogMapper.getBlogByPublishedAndId(blogPublished,postType, comment.getBlogId()));
        // 封装父级评论
        if(comment.getParentCommentId() != -1){
            comment.setParentComment(commentMapper.getById(commentPublished, comment.getParentCommentId()));
        }
        // 封装被回复评论
        // 1.获取所有的子评论
        List<Integer> childIds = commentMapper.selectChildCommentIds(comment.getId());
        for(Integer childId:childIds){
            /*comment.getReplyComments().add(commentMapper.getById(childId));*/
            Comment tempComment = this.getById(blogPublished,commentPublished,  postType, childId);
            if(tempComment != null){
                comment.getReplyComments().add(tempComment);
            }
        }

        return comment;
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if(comment == null || comment.getReplyComments() == null){
            return;
        }
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
