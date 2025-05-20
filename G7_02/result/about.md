# Important
本次作業遇到GlassFish4.1.2Bug&java.lang.NoClassDefFoundError: Could not initialize class org.eclipse.persistence.jaxb.BeanValidationHelper
借助神之力爆改專案結構才正常執行
# 爆改內容
1. 新建.\web\WEB-INF\lib放入多個jar
2. 引入多個jar，都在jar全家餐裡
3. 爆改ApplicationConfig.java, 因為遇到驗證問題