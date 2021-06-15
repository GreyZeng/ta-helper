第一步：
将
/src/main/resources/config.sample
另存一份，存的名字为：
/src/main/resources/config.properties

/src/main/resources/config.properties 这个文件我已经加入ignore，不会被提交。

第二步：
登录博客园，拿到cookie，如何拿cookie，请自行百度。

第三步：
把cookie复制到/src/main/resources/config.properties下的cookie项中。

第四步：
修改`OUTPUT_JSONS`，`OUTPUT_COMMENTS`和`OUTPUT_REPORTS`为本地文件夹路径，分别用于存储程序输出的json，评论和报告。

第五步：
根据情况设置`FIRST`；首次使用设为`true`并在`initReportByNew`填充数据；后续使用设为`false`。

第六步：
执行App.java的main方法