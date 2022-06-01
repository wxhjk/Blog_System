# Blog_System
- 开发技术：Spring＋SpringMVC＋MyBatis＋MySQL＋jQuery 该项目由用户管理，博客列表，博客详情，文章发布，修改博客和删除博客六个模块组成
- 通过 Spring，SpringMVC，Mybatis 为主框架来实现业务功能，采用 jQuery.Ajax 等前端技术共同完成
- 项目的结构分为三层（controller，service，mapper），在控制层对请求进行了校验和处理，服务层发挥了解耦作用并增加了项目的拓展性，Mybatis 作为持久层提供了更灵活的 SQL 映射
- 引用 AOP 的设计思想，配置了自定义拦截器对登录页面以外的页面进行拦截，并且统一了异常的处理以及数据的返回格式


项目展示链接: http://175.178.212.251:8090/login.html
