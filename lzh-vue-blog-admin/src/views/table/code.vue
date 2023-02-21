<template>
  <div class="code-container">
    <pre v-highlightjs><code class="language-java">System.out.println("hello world")</code></pre>
  </div>
</template>

<script>
export default {
  name: 'code',
  data() {
    return {
      article: "<h1>SpringBoot整合邮箱服务实现登录验证功能</h1>\n" +
        "<blockquote>\n" +
        "<p>参考网址：https://blog.csdn.net/a648119398/article/details/125290931?spm=1001.2014.3001.5501</p>\n" +
        "</blockquote>\n" +
        "<h1>SpringBoot整合邮箱服务实现注册功能</h1>\n" +
        "<h2>1.导入依赖</h2>\n" +
        "<pre><code class=\"language-xml\">&lt;!-- 邮件服务 --&gt;\n" +
        "&lt;dependency&gt;\n" +
        "    &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;\n" +
        "    &lt;artifactId&gt;spring-boot-starter-mail&lt;/artifactId&gt;\n" +
        "&lt;/dependency&gt;\n" +
        "&lt;!-- Thymeleaf 模版，用于发送模版邮件 --&gt;\n" +
        "&lt;dependency&gt;\n" +
        "    &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;\n" +
        "    &lt;artifactId&gt;spring-boot-starter-thymeleaf&lt;/artifactId&gt;\n" +
        "&lt;/dependency&gt;\n" +
        "</code></pre>\n" +
        "<h2>2.配置application.yml</h2>\n" +
        "<pre><code class=\"language-yaml\">server:\n" +
        "  port: 8082\n" +
        "spring:\n" +
        "  datasource:\n" +
        "    driver-class-name: com.mysql.cj.jdbc.Driver\n" +
        "    url: jdbc:mysql://127.0.0.1:3307/test?useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true&amp;allowMultiQueries=true&amp;useSSL=false\n" +
        "    username: root\n" +
        "    password: abc123\n" +
        "  #邮箱配置\n" +
        "  mail:\n" +
        "    # 发送邮件的服务器地址\n" +
        "    host: smtp.qq.com\n" +
        "    #填写自己的qq邮箱账号 # 开启 IMAP/SMTP服务 的qq邮箱的账号\n" +
        "    username: 1203464373@qq.com\n" +
        "    #填写自己的授权码  # 开启 IMAP/SMTP服务 获得的授权码,而不是qq邮箱的登录密码\n" +
        "    password: wkfoakhphgsvhieh\n" +
        "    default-encoding: UTF-8\n" +
        "  redis:\n" +
        "    host: 1.117.218.230\n" +
        "    port: 7001\n" +
        "    database: 0\n" +
        "    password: qfedu123\n" +
        "</code></pre>\n" +
        "<h2>3.使用MybatisX生成mapper\\service\\entity</h2>\n" +
        "<h2>4.controller</h2>\n" +
        "<pre><code class=\"language-java\">@RestController\n" +
        "@RequestMapping(&quot;/user&quot;)\n" +
        "public class UserController {\n" +
        "\n" +
        "    @Resource\n" +
        "    private UserService userService;\n" +
        "\n" +
        "    /**\n" +
        "     * 注册\n" +
        "     * @param userRegistForm\n" +
        "     * @return\n" +
        "     */\n" +
        "    @PostMapping(&quot;/email/regist&quot;)\n" +
        "    public ResponseResult regist(@RequestBody UserRegistForm userRegistForm) {\n" +
        "        return userService.regist(userRegistForm);\n" +
        "    }\n" +
        "\n" +
        "    /**\n" +
        "     * 获取验证码\n" +
        "     * @param email\n" +
        "     * @return\n" +
        "     */\n" +
        "    @PostMapping(&quot;/email/getCode/{email}&quot;)\n" +
        "    public ResponseResult getCode(@PathVariable String email) {\n" +
        "        return userService.getEmailCode(email);\n" +
        "    }\n" +
        "}\n" +
        "</code></pre>\n" +
        "<h2>5.service</h2>\n" +
        "<pre><code class=\"language-java\">package com.lzh.email.service.impl;\n" +
        "\n" +
        "import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\n" +
        "import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n" +
        "import com.lzh.email.domain.ResponseResult;\n" +
        "import com.lzh.email.domain.User;\n" +
        "import com.lzh.email.pojo.UserRegistForm;\n" +
        "import com.lzh.email.service.UserService;\n" +
        "import com.lzh.email.mapper.UserMapper;\n" +
        "import com.lzh.email.utils.JwtUtil;\n" +
        "import com.lzh.email.utils.PwdUtil;\n" +
        "import org.apache.tomcat.util.security.MD5Encoder;\n" +
        "import org.springframework.beans.factory.annotation.Autowired;\n" +
        "import org.springframework.beans.factory.annotation.Value;\n" +
        "import org.springframework.data.redis.core.StringRedisTemplate;\n" +
        "import org.springframework.http.ResponseEntity;\n" +
        "import org.springframework.mail.SimpleMailMessage;\n" +
        "import org.springframework.mail.javamail.JavaMailSender;\n" +
        "import org.springframework.stereotype.Service;\n" +
        "import org.springframework.util.StringUtils;\n" +
        "import sun.security.provider.MD5;\n" +
        "\n" +
        "import javax.annotation.Resource;\n" +
        "import java.security.MessageDigest;\n" +
        "import java.util.Objects;\n" +
        "import java.util.Random;\n" +
        "import java.util.concurrent.TimeUnit;\n" +
        "\n" +
        "/**\n" +
        " * @author ASUS\n" +
        " * @description 针对表【my_user】的数据库操作Service实现\n" +
        " * @createDate 2022-07-28 21:13:51\n" +
        " */\n" +
        "@Service\n" +
        "public class UserServiceImpl extends ServiceImpl&lt;UserMapper, User&gt; implements UserService {\n" +
        "\n" +
        "    @Value(&quot;${spring.mail.username}&quot;)\n" +
        "    private String from;\n" +
        "\n" +
        "    @Resource\n" +
        "    private JavaMailSender javaMailSender;\n" +
        "\n" +
        "    @Resource\n" +
        "    private StringRedisTemplate stringRedisTemplate;\n" +
        "\n" +
        "    @Override\n" +
        "    public ResponseResult regist(UserRegistForm userRegistForm) {\n" +
        "\n" +
        "        if (Objects.isNull(userRegistForm)) {\n" +
        "            return ResponseResult.errorResult(303, &quot;注册失败&quot;);\n" +
        "        }\n" +
        "        if (!StringUtils.hasText(userRegistForm.getEmail())) {\n" +
        "            return ResponseResult.errorResult(305, &quot;邮箱不能为空&quot;);\n" +
        "        }\n" +
        "        if (!StringUtils.hasText(userRegistForm.getPassword())) {\n" +
        "            return ResponseResult.errorResult(306, &quot;密码不能为空&quot;);\n" +
        "        }\n" +
        "        if (!StringUtils.hasText(userRegistForm.getInputCode())) {\n" +
        "            return ResponseResult.errorResult(307, &quot;验证码不能为空&quot;);\n" +
        "        }\n" +
        "\n" +
        "        //判断该邮箱是否已经注册\n" +
        "        if (isRegisted(userRegistForm.getEmail())) {\n" +
        "            return ResponseResult.errorResult(308, &quot;该邮箱已经注册&quot;);\n" +
        "        }\n" +
        "\n" +
        "        //从redis中获取，判断是否过期\n" +
        "        String value = stringRedisTemplate.opsForValue().get(&quot;email:&quot; + userRegistForm.getEmail());\n" +
        "        if (!StringUtils.hasText(value)) {\n" +
        "            //验证码不存在\n" +
        "            return ResponseResult.errorResult(303, &quot;验证码过期，请重新发送&quot;);\n" +
        "        }\n" +
        "\n" +
        "        //验证码存在\n" +
        "        //判断输入的验证码是否正确\n" +
        "        if (!userRegistForm.getInputCode().equals(value)) {\n" +
        "            return ResponseResult.errorResult(304, &quot;验证码输入错误&quot;);\n" +
        "        }\n" +
        "\n" +
        "        //存入数据库\n" +
        "        User user = new User();\n" +
        "        user.setEmail(userRegistForm.getEmail());\n" +
        "        String password = userRegistForm.getPassword();\n" +
        "        String encode = PwdUtil.encode(password);\n" +
        "        user.setPassword(encode);\n" +
        "        this.save(user);\n" +
        "\n" +
        "        return ResponseResult.okResult(200, &quot;注册成功&quot;);\n" +
        "    }\n" +
        "\n" +
        "    private boolean isRegisted(String email) {\n" +
        "        //判断该邮箱是否已经注册\n" +
        "        LambdaQueryWrapper&lt;User&gt; queryWrapper = new LambdaQueryWrapper&lt;&gt;();\n" +
        "        queryWrapper.eq(User::getEmail, email);\n" +
        "        User user = this.getOne(queryWrapper);\n" +
        "        if (!Objects.isNull(user)) {\n" +
        "            return true;\n" +
        "        }\n" +
        "        return false;\n" +
        "    }\n" +
        "\n" +
        "    @Override\n" +
        "    public ResponseResult getEmailCode(String email) {\n" +
        "\n" +
        "        //判断该邮箱是否已经注册\n" +
        "        if (isRegisted(email)) {\n" +
        "            return ResponseResult.errorResult(308, &quot;该邮箱已经注册&quot;);\n" +
        "        }\n" +
        "\n" +
        "        String redisKey = &quot;email:&quot; + email;\n" +
        "        //判断redis是否存在key\n" +
        "        String value = stringRedisTemplate.opsForValue().get(redisKey);\n" +
        "        if (StringUtils.hasText(value)) {\n" +
        "            return ResponseResult.errorResult(303, &quot;验证码已发送&quot;);\n" +
        "        }\n" +
        "\n" +
        "        //发送验证码\n" +
        "        SimpleMailMessage mailMessage = new SimpleMailMessage();\n" +
        "        mailMessage.setFrom(from);\n" +
        "        mailMessage.setTo(email);\n" +
        "        mailMessage.setSubject(&quot;注册验证码&quot;);\n" +
        "        int emailCode = (int) (Math.random() * ((9999 - 1000 + 1) + 1000));\n" +
        "        String context = &quot;注册验证码为: &quot; + emailCode + &quot;,二分钟内有效，请妥善保管!&quot;;\n" +
        "        mailMessage.setText(context);\n" +
        "        javaMailSender.send(mailMessage);\n" +
        "\n" +
        "        //存入redis\n" +
        "        stringRedisTemplate.opsForValue().set(redisKey, emailCode + &quot;&quot;, 2, TimeUnit.MINUTES);\n" +
        "\n" +
        "        return ResponseResult.okResult(200, &quot;验证码发送成功&quot;);\n" +
        "    }\n" +
        "}\n" +
        "</code></pre>\n"
    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
