package com.duckbobby.controller;

import com.duckbobby.model.Article;
import com.duckbobby.model.Category;
import com.duckbobby.model.Count;
import com.duckbobby.service.ArticleService;
import com.duckbobby.service.CategoryService;
import com.duckbobby.service.CountService;
import com.duckbobby.utils.DateUtil;
import com.duckbobby.utils.QiNiuUtils;
import jodd.io.FileUtil;
import jodd.io.StreamUtil;
import jodd.util.SystemUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理台controller
 * Created by duckbobby on 2017/11/28.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CountService countService;

    @Autowired
    private CategoryService categoryService;

    static {
        try {
            initSigar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理台首页
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) throws IOException {
        String user = (String) request.getAttribute("user");
        model.addAttribute("user", user);
        Sigar sigar;
        try {
            //当前时间
            model.addAttribute("nowTime", DateUtil.getDateTime());
            //应用服务器
            ServletContext servletContext = request.getSession().getServletContext();
            model.addAttribute("servletServer", servletContext.getServerInfo());
            //JDK版本
            model.addAttribute("javaVersion", SystemUtil.getJavaVersion());
            sigar = new Sigar();
            CpuInfo[] cpuInfoList = sigar.getCpuInfoList();
            //CPU信息
            model.addAttribute("cpu", cpuInfoList[0].getVendor() + "(" + cpuInfoList[0].getModel() + ") " + cpuInfoList[0].getMhz() + "MHz x " + cpuInfoList.length + "核");
            //操作系统
            String osname = System.getProperties().getProperty("os.name");
            model.addAttribute("osname", osname);
            //服务器内存
            Mem freeMemory = sigar.getMem();
            model.addAttribute("mem", String.format("%.2fG", new Object[]{Double.valueOf((double) freeMemory.getTotal() / 1.073741824E9D)}));
            //JVM内存
            model.addAttribute("jvmMaxMem", String.format("%.2fM", new Object[]{Float.valueOf((float) Runtime.getRuntime().maxMemory() / 1048576.0F)}));
            //
            model.addAttribute("memUsedPercent", String.format("%.2f", new Object[]{Double.valueOf(freeMemory.getUsedPercent())}));
            //CPU使用
            model.addAttribute("cpuUsed", String.format("%.2f", new Object[]{Double.valueOf(CpuPerc.format(sigar.getCpuPerc().getCombined()).replace("%", ""))}));
            long pid = sigar.getPid();
            long startTime = sigar.getProcTime(pid).getStartTime();
            //系统运行时间
            model.addAttribute("runningTime", DateUtil.friendDuration(System.currentTimeMillis() - startTime));
            //系统内存使用
            long maxMemory2 = Runtime.getRuntime().maxMemory();
            long freeMemory1 = Runtime.getRuntime().freeMemory();
            //JVM内存使用
            model.addAttribute("useableMemeory", String.format("%.2f", new Object[]{Double.valueOf((double) (maxMemory2 - freeMemory1) / ((double) maxMemory2 * 1.0D) * 100.0D)}));
        } catch (SigarException e) {
            logger.error("使用sigar获取服务器信息错误：", e);
        }
        return "manager/index";
    }

    /**
     * 文章
     */
    @RequestMapping("/article")
    public String homepage(HttpServletRequest request, Model model, String page) {
        String user = (String) request.getSession().getAttribute("user");
        //获取点击量和评论量
        List<Count> countList = countService.getCountList();
        //总数
        int count = articleService.getArticleSize();
        //总页数
        int pageNum = count / 5;
        model.addAttribute("pageNum", pageNum);

        int num = StringUtils.isNotBlank(page) ? Integer.parseInt(page) : 0;
        //展示的文章
        List<Article> list = articleService.getArticleList(num, countList);
        model.addAttribute("list", list);
        if (pageNum > num) {
            //下一页
            model.addAttribute("nextPage", num + 1);
        }
        if (num > 0) {
            //上一页
            model.addAttribute("previous", num - 1);
        }
        model.addAttribute("page", num + 1);
        model.addAttribute("user", user);
        return "manager/article";
    }

    /**
     * 写博客首页
     */
    @RequestMapping("/add-article")
    public String blog(HttpServletRequest request, Model model) {
        String user = (String) request.getAttribute("user");
        model.addAttribute("user", user);

        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);

        return "manager/add-article";
    }

    /**
     * 新增文章
     *
     * @param article 文章实体
     */
    @RequestMapping("/addArticle")
    @ResponseBody
    public String addArticle(HttpServletRequest request, Article article) {
        articleService.createArticle(article);
        return "";
    }

    /**
     * 修改文章页面
     */
    @RequestMapping("/update-article")
    public String updateArticle(Model model, String contentId) {
        try {
            Article article = articleService.findArticleByContentId(contentId);//获取类别
            List<Category> categories = categoryService.getCategoryList();
            model.addAttribute("categories", categories);
            model.addAttribute("article", article);
        } catch (Exception e) {
            return "redirect:/404";
        }
        return "manager/update-article";
    }

    /**
     * 修改文章
     */
    @RequestMapping("/updateArticle")
    @ResponseBody
    public String updateArticle(Article article) {
        articleService.updateArticle(article);
        return "";
    }

    @RequestMapping("/deleteArticle/{contentId}")
    public String deleteArticle(@PathVariable int contentId){
        articleService.deleteArticle(contentId);
        return "manager/article";
    }

    /**
     * 公告
     */
    @RequestMapping("/notice")
    public String notice() {
        return "manager/notice";
    }

    /**
     * 评论
     */
    @RequestMapping("/comment")
    public String comment() {
        return "manager/comment";
    }

    /**
     * 栏目
     */
    @RequestMapping("/category")
    public String category() {
        return "manager/category";
    }

    /**
     * 友情链接
     */
    @RequestMapping("/flink")
    public String flink() {
        return "manager/flink";
    }

    /**
     * 访问记录
     */
    @RequestMapping("/loginlog")
    public String loginlog() {
        return "manager/loginlog";
    }

    /**
     * ueditor 配置
     */
    @RequestMapping("/ueditor")
    @ResponseBody
    public Object ueditor(HttpServletRequest request) throws Exception {
        String action = request.getParameter("action");
        //图片上传
        if ("uploadimage".equals(action)) {
            String contentType = request.getContentType();
            if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
                MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
                //获取上传的文件
                Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
                if (fileMap.size() > 0) {
                    //获取第一个就是
                    for (String key : fileMap.keySet()) {
                        MultipartFile multipartFile = fileMap.get(key);
                        InputStream inputStream = multipartFile.getInputStream();
                        String name = multipartFile.getOriginalFilename();
                        //上传七牛云服务器
                        Map map = QiNiuUtils.updataFile(inputStream, name);
                        return map;
                    }
                }
            }
            return null;
            //所有图片列表
        } else if ("listimage".equals(action)) {
            Map<String, String> map = new HashMap<>();
            map.put("1", "");
            return map;
            //获取配置文件
        } else if ("config".equals(action)) {
            File file1 = ResourceUtils.getFile("classpath:config.json");
            return FileUtils.readFileToString(file1, "UTF-8");
        } else {
            return null;
        }
    }

    //初始化sigar的配置文件
    private static void initSigar() throws IOException {
        SigarLoader loader = new SigarLoader(Sigar.class);
        String lib = null;

        try {
            lib = loader.getLibraryName();
        } catch (ArchNotSupportedException var7) {
            logger.error(var7.getMessage(), var7);
        }
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/sigar/" + lib);
        if (resource.exists()) {
            InputStream is = resource.getInputStream();
            File tempDir = FileUtil.createTempDirectory();
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
            StreamUtil.copy(is, os);
            is.close();
            os.close();
            System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
        }
    }
}
