import org.apache.log4j.Logger;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

/**
 * Created by Tian Jiayuan on 2015/10/28.
 */
public class StartUp {
    private static Logger logger = Logger.getLogger(StartUp.class);

    public static void main(String[] args){
        String[] configLocation = {
                "classpath:/conf/spring-config.xml"
        };
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);
        logger.info("服务启动完成");
        String[] names = ctx.getBeanDefinitionNames();

        for (String string : names)
            logger.debug("服务名称:" + string + ",");
    }

}
