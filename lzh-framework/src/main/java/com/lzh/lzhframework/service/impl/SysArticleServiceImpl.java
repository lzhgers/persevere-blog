package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.*;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.domain.vo.SysArticleVo;
import com.lzh.lzhframework.domain.vo.SysUpdateArticleVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;
import com.lzh.lzhframework.service.*;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Service
public class SysArticleServiceImpl implements SysArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private CommentService commentService;

    @Override
    public ResponseResult pageList(QueryArticleForm form) {

        String title = form.getTitle();
        String author = form.getAuthor();
        Long categoryId = form.getCategoryId();
        Long tagId = form.getTagId();

        Page<Article> page = new Page<>(form.getPageNum(), form.getPageSize());

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //标题
        queryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);

        //作者
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(author), User::getUserName, author);
        List<User> userList = userService.list(wrapper);
        List<Long> userIds = userList.stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
        if (userIds.size() > 0) {
            queryWrapper.in(Article::getCreateBy, userIds);
        } else {
            queryWrapper.eq(Article::getId, -1);
        }

        //分类
        queryWrapper.eq(!Objects.isNull(categoryId), Article::getCategoryId, categoryId);

        //标签
        if (!Objects.isNull(tagId)) {
            List<Long> articleIds = articleTagService.getArticleIdByTagId(tagId);
            if (articleIds.size() <= 0) {
                queryWrapper.eq(Article::getId, -1L);
            } else {
                queryWrapper.in(Article::getId, articleIds);
            }
        }

        articleMapper.selectPage(page, queryWrapper);

        List<Article> articleList = page.getRecords();


        List<SysArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, SysArticleVo.class);
        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    //设置标签
                    List<Tag> tags = tagService.getTagsByArticleId(articleVo.getId());
                    articleVo.setTags(tags);
                    //设置分类
                    Category category = categoryService.getCategoryByArticleId(articleVo.getId());
                    articleVo.setCategory(category);
                    //设置作者
                    String createBy = userService.getUserNameByUserId(articleVo.getCreateBy());
                    articleVo.setAuthor(createBy);
                    return articleVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.success(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult deleteArticleById(Long articleId) {
        //删除文章
        articleMapper.deleteById(articleId);

        //删除文章标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagService.remove(articleTagLambdaQueryWrapper);

        //删除文章评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        commentService.remove(commentLambdaQueryWrapper);

        return ResponseResult.success();
    }

    @Override
    public int saveUploadArticle(String markdown, String html, String mdName) {
        Long userId = SecurityUtils.getUserId();
        Article article = new Article().setTitle(mdName)
                .setContent(markdown)
                .setHtml(html)
                .setSummary(mdName)
                .setStatus("2")
                .setCreateBy(userId)
                .setUpdateBy(userId);
        articleMapper.insert(article);
        return 0;
    }

    @Transactional
    @Override
    public ResponseResult saveArticle(SysSaveArticleForm sysSaveArticleForm) {
        Long userId = SecurityUtils.getUserId();
        String isTop = sysSaveArticleForm.getIsTop();
        String status = sysSaveArticleForm.getStatus();
        String isComment = sysSaveArticleForm.getIsComment();

        //保存文章
        Article article = BeanCopyUtils.copyBean(sysSaveArticleForm, Article.class);
        article.setIsTop("true".equals(isTop) ? "1" : "0");
        article.setStatus("true".equals(status) ? "0" : "2");
        article.setIsComment("true".equals(isComment) ? "1" : "0");
        article.setCategoryId(sysSaveArticleForm.getCategory());
        article.setCreateBy(userId);

        articleMapper.insert(article);
        //保存标签
        List<Long> tagIds = sysSaveArticleForm.getTag();
        if (Objects.isNull(tagIds) || tagIds.size() == 0) {
            return ResponseResult.success();
        }
        List<ArticleTag> articleTagList = tagIds.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTagList);

        return ResponseResult.success();
    }

    @Override
    public ResponseResult queryUpdateArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        SysUpdateArticleVo vo = BeanCopyUtils.copyBean(article, SysUpdateArticleVo.class);

        Category category = categoryService.getById(article.getCategoryId());
        if (!Objects.isNull(category)) {
            vo.setCategoryId(category.getId());
        }

        List<Tag> tags = tagService.getTagsByArticleId(article.getId());
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());
        vo.setTagIds(tagIds);

        return ResponseResult.success(vo);
    }

    @Transactional
    @Override
    public ResponseResult updateArticle(SysUpdateArticleVo vo) {

        //删除原有标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, vo.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);

        //添加修改后的标签
        List<Long> tagIds = vo.getTagIds();
        List<ArticleTag> articleTagList = tagIds.stream()
                .map(tagId -> new ArticleTag(vo.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);

        //修改文章
        Article article = BeanCopyUtils.copyBean(vo, Article.class);
        if (!StringUtils.hasText(article.getThumbnail())) {
            article.setThumbnail(null);
        }
        articleMapper.updateById(article);

        return ResponseResult.success();
    }

    @Override
    public ResponseResult downLoadFile(Long articleId, HttpServletResponse response) {
        if (Objects.isNull(articleId)) {
            throw new SystemException(AppHttpCodeEnum.NOT_SELECT_FILE);
        }
        //获取选择的文章
        Article article = articleMapper.selectById(articleId);

        ServletOutputStream sos = null;
        BufferedOutputStream bos = null;
        try {
            //设置响应
            response.setContentType("text/markdown");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    genAttachmentFileName(article.getTitle(), "JSON_FOR_UCC_") + ".md");
            sos = response.getOutputStream();
            bos = new BufferedOutputStream(sos);
            bos.write(article.getContent().getBytes(StandardCharsets.UTF_8));
            bos.flush();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (sos != null) {
                    sos.flush();
                    sos.close();
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        return ResponseResult.success();
    }

    @Transactional
    @Override
    public ResponseResult deleteBatch(List<Long> articleIds) {
        //删除文章
        articleMapper.deleteBatchIds(articleIds);

        //删除文章标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleTag::getArticleId, articleIds);
        articleTagService.remove(queryWrapper);

        return ResponseResult.success();
    }

    /**
     * 解决文件中文名乱码问题
     *
     * @param cnName
     * @param defaultName
     * @return
     */
    public String genAttachmentFileName(String cnName, String defaultName) {
        try {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }


    String content = "# 发送请求\n" +
            "\n" +
            "##  1.HTTP请求报文\n" +
            "\n" +
            "1. 请求行\n" +
            "\n" +
            "   格式：method url\n" +
            "\n" +
            "   例如：GET/product_detail?id=2 或 POST/login\n" +
            "\n" +
            "2. 请求头（一般有多个请求头）\n" +
            "\n" +
            "   Host:www.baidu.com\n" +
            "\n" +
            "   Cookie:BAIDUID=AD3B0FA706E;BIDUPSID=AD3B0FA706;\n" +
            "\n" +
            "   Content-Type:application/x-www-form-urlencoded 或者 application/json\n" +
            "\n" +
            "3. 请求体\n" +
            "\n" +
            "   username=tom&pwd=123\n" +
            "\n" +
            "   {\"username\":\"tom\",\"pwd\":123}\n" +
            "\n" +
            "## 2.请求方式\n" +
            "\n" +
            "- GET：从服务器端读取数据\n" +
            "- POST：向服务器端添加新数据\n" +
            "- PUT：更新服务器端已存在的数据\n" +
            "- DELETE：删除服务器端数据\n" +
            "\n" +
            "## 3. 请求参数\n" +
            "\n" +
            "- query参数\n" +
            "\n" +
            "  1. 参数包含在请求地址中，格式为：`/xxx?name=tom&age=18`\n" +
            "  2. 敏感数据不要用query参数，因为参数是地址的一部分，比较危险\n" +
            "  3. 备注：query参数又称查询字符串参数，编码方式为 urlencoded\n" +
            "\n" +
            "- params参数\n" +
            "\n" +
            "  1. 参数包含在请求地址中，格式为：`/xxx/tom/18`\n" +
            "\n" +
            "- 请求体参数\n" +
            "\n" +
            "  1. 参数包含在请求体中，可通过浏览器开发工具或抓包工具查看\n" +
            "\n" +
            "  2. 有三种格式：\n" +
            "\n" +
            "     - 格式一：urlencoded格式\n" +
            "\n" +
            "       例如：name=tom&age=18\n" +
            "\n" +
            "       对应请求头：Content-Type:application/x-www-form-urlencoded\n" +
            "\n" +
            "     - 格式二：json格式\n" +
            "\n" +
            "       例如：{\"name\":\"tom\",\"age\":12}\n" +
            "\n" +
            "       对应请求头：Content-Type:application/json\n" +
            "\n" +
            "     - 格式三：form-data格式（用于文件上传请求）\n" +
            "\n" +
            "       对应请求头：Content-Type:multipart/form-data\n" +
            "\n" +
            "## 4.请求传参可以使用的方式\n" +
            "\n" +
            "- GET\n" +
            "  - query参数\n" +
            "  - params参数\n" +
            "- POST\n" +
            "  - query参数\n" +
            "  - params参数\n" +
            "  - 请求体参数\n" +
            "- PUT\n" +
            "  - query参数\n" +
            "  - params参数\n" +
            "  - 请求体参数\n" +
            "- DELETE\n" +
            "  - query参数\n" +
            "  - params参数\n" +
            "  - 请求体参数";
}
