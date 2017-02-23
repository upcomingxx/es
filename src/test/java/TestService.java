import com.sanhai.service.BHTopicService;
import com.sanhai.service.VideoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 作者：boys
 * 创建时间：2017-02-23 17:37
 * 类描述：
 * 修改人：
 * 修改时间：
 */

@ContextConfiguration(locations = {
        "classpath*:/conf/spring-config.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {


    @Resource
    private VideoService videoService = null;

    @Resource
    private BHTopicService bhTopicService = null;

    @Test
    public void testBHTopic(){
        List<Map<String, Object>> list = this.bhTopicService.loadTopics(null, 1, 10);
        System.out.println(list.size());
    }

    @Test
    public void testVideo(){
        List<Map<String, Object>> list = this.videoService.selectVideos();
        System.out.println(list.size());
    }
}
