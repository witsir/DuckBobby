package com.duckbobby.controller;

import com.duckbobby.common.Frequency;
import com.duckbobby.model.*;
import com.duckbobby.service.*;
import com.duckbobby.utils.DateUtil;
import com.duckbobby.utils.cleanXSS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 门户网站controller
 * Created by duckbobby on 2017/11/29.
 */
@Controller
@Frequency(name = "gateway", limit = 5, time = 1)
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private CountService countService;

    /**
     * 首页
     */
    @RequestMapping(value = {"/", "index"})
    public String gateway(Model model) throws Exception {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        //获取最新发布的文章
        List<Article> articleList = articleService.getArticleList(0, countList);
        model.addAttribute("articles", articleList);

        //总数
        int count = articleService.getArticleSize();
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > 0) {
            model.addAttribute("nextPage", 1);
        }

        //获取热门文章（5篇）
        List<Article> top = articleService.getTop5ArticleList("1", countList);
        model.addAttribute("topList", top);
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        //今日推荐
        List<Article> top2 = articleService.getTopArticleList("1");
        model.addAttribute("now", top2.get(0));
        //获取关键词列表
        List<Key> keys = keyService.getKeyList();
        model.addAttribute("keys", keys);
        return "homepage/index";
    }

    /**
     * 类别
     */
    @RequestMapping("/category/{categoryId}")
    public String category(Model model, @PathVariable int categoryId) {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        List<Article> list = articleService.getArticleByCategoryId(categoryId, 0, countList);
        model.addAttribute("articles", list);
        //总数
        int count = articleService.getArticleSizeByCategoryId(categoryId);
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > 0) {
            model.addAttribute("nextPage", 1);
        }
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //获取类别
        Category cate = categories.stream().filter(category -> category.getId() == categoryId).findFirst().get();
        model.addAttribute("category", cate);
        //获取热门文章
        List<Article> top = articleService.getTop5ArticleList("1", countList);
        model.addAttribute("topList", top);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        return "homepage/category";
    }

    /**
     * 文章
     */
    @RequestMapping("/article/{contentId}")
    public String article(HttpServletRequest request, Model model, @PathVariable String contentId) {
        //从session中拿到有没有这个content浏览记录，若有则time不是null
        LocalDateTime time = (LocalDateTime) request.getSession().getAttribute(contentId);
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        //文章内容
        Article article = null;
        try {
            article = articleService.findArticleByContentId(contentId);
            if (null == article) {
                logger.warn("获取内容ID为：" + contentId + " 失败");
                return "redirect:/404";
            }
        } catch (Exception e) {
            logger.warn("获取内容ID为：" + contentId + " 失败");
            return "redirect:/404";
        }
        for (Count count : countList) {
            if (count.getArticleId() == article.getId()) {
                article.setComment(count.getComment());
                article.setClick(count.getClick());
            }
        }
        if (null == time) {
            //用session来判断点击量，如果不存在，就加1点击量。
            request.getSession().setAttribute(contentId, LocalDateTime.now());
            countService.updateClickByArticleId(article.getId());
        }
        model.addAttribute("article", article);
        //获取留言
        List<Comment> commentList = commentService.findCommentByArticleId(article.getId());
        model.addAttribute("comments", commentList);
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        Optional<Category> found = Optional.empty();
        for (Category category : categories) {
            if (category.getId() == Integer.parseInt(article.getCategoryId())) {
                found = Optional.of(category);
                break;
            }
        }
        Category cate = found.get();
        model.addAttribute("category", cate);
        //相关推荐
        List<Article> list = articleService.getArticleByCategoryId(cate.getId(), 0, countList);
        model.addAttribute("articles", list);
        //获取热门文章
        List<Article> top = articleService.getTop5ArticleList("1", countList);
        model.addAttribute("topList", top);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        return "homepage/article";
    }

    /**
     * 首页分页
     */
    @RequestMapping("/page/{num}")
    public String page(Model model, @PathVariable int num) {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        //获取最新发布的文章
        List<Article> list = articleService.getArticleList(num, countList);
        model.addAttribute("articles", list);
        //总数
        int count = articleService.getArticleSize();
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > num) {
            model.addAttribute("nextPage", num + 1);
        }
        //获取热门文章（5篇）
        List<Article> top = articleService.getTopArticleList("5");
        model.addAttribute("topList", top);
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        //今日推荐
        List<Article> top2 = articleService.getTopArticleList("2");
        model.addAttribute("now", top2.get(0));
        return "homepage/index";
    }

    /**
     * 类别分页
     */
    @RequestMapping("/categoryPage/{categoryId}/{page}")
    public String categoryPage(Model model, @PathVariable int categoryId, @PathVariable int page) {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        List<Article> list = articleService.getArticleByCategoryId(categoryId, page, countList);
        model.addAttribute("articles", list);
        //总数
        int count = articleService.getArticleSizeByCategoryId(categoryId);
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > page) {
            model.addAttribute("nextPage", page + 1);
        }
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //获取类别
        Category cate = categories.stream().filter(category -> category.getId() == categoryId).findAny().get();
        model.addAttribute("category", cate);
        //获取热门文章
        List<Article> top = articleService.getTop5ArticleList("1", countList);
        model.addAttribute("topList", top);
        return "homepage/category";
    }

    /**
     * 通过关键字查询
     */
    @RequestMapping("/keyword")
    public String keyword(Model model, String keyword) {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        List<Article> list = articleService.getArticleByKeyword(keyword, 0, countList);
        model.addAttribute("articles", list);
        //总数
        int count = articleService.getArticleSizeByKeyword(keyword);
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > 0) {
            model.addAttribute("nextPage", 1);
        }
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //获取类别
        Category cate = categories.stream().filter(category -> category.getId() == 1).findAny().get();
        model.addAttribute("category", cate);
        //获取热门文章
        List<Article> top = articleService.getTop5ArticleList("1", countList);
        model.addAttribute("topList", top);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        //关键字
        model.addAttribute("keyword", keyword);
        //
        return "homepage/keyword";
    }

    /**
     * 通过关键字查询
     */
    @RequestMapping("/keywordPage/{keyword}/{page}")
    public String keywordPage(Model model, @PathVariable String keyword, @PathVariable int page) {
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        List<Article> list = articleService.getArticleByKeyword(keyword, page, countList);
        model.addAttribute("articles", list);
        //总数
        int count = articleService.getArticleSizeByKeyword(keyword);
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);
        if (pageNum > page) {
            model.addAttribute("nextPage", page + 1);
        }
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        //获取类别
        Category cate = categories.stream().filter(category -> category.getId() == 1).findAny().get();
        model.addAttribute("category", cate);
        //获取热门文章
        List<Article> top = articleService.getTopArticleList("1");
        model.addAttribute("topList", top);
        //每日一句
        Good good = articleService.findGood();
        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        model.addAttribute("good", good);
        //关键字
        model.addAttribute("keyword", keyword);
        return "homepage/keyword";
    }

    /**
     * 提交留言
     */
    @RequestMapping("/comment")
    @ResponseBody
    public Comment comment(Comment comment) {
        String modifiedContent = cleanXSS.cleanXSS(comment.getContent());
        comment.setContent(modifiedContent);
        commentService.createComment(comment);
        countService.updateCommentByArticleId(comment.getArticleId());
        return comment;
    }


    /**
     *
     */
    @RequestMapping("/tags")
    public String tags(Model model) {
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "homepage/tags";
    }

    /**
     *
     */
    @RequestMapping("readers")
    public String readers(Model model) {
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "homepage/readers";
    }

    /**
     *
     */
    @RequestMapping("links")
    public String links(Model model) {
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "homepage/links";
    }

}
