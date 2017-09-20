package biz;

import com.xgame.order.consumer.biz.PhoneReChargeBiz;
import com.xgame.order.consumer.db.dao.RewardBoxDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.util.MybatisSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PhoneBizTest {
    public static void main(String[] args) throws URISyntaxException {
        SqlSession sqlSession = MybatisSqlSessionFactory.getSqlSessionAutoCommit();
        RewardBoxDao rewardBoxDao = sqlSession.getMapper(RewardBoxDao.class);
        List<RewardBoxDto> rewardBoxDtos = rewardBoxDao.getAll();
        PhoneReChargeBiz biz = new PhoneReChargeBiz(sqlSession,rewardBoxDtos);
        List<RewardOrderLogDto> rewardOrderLogDtos = new ArrayList<>();
        RewardOrderLogDto rewardOrderLogDto = new RewardOrderLogDto();
        rewardOrderLogDto.setOrder_id(String.valueOf(System.currentTimeMillis()));
        rewardOrderLogDto.setIndate("2017-09-10 16:05:43");
        rewardOrderLogDto.setItem_id(10101);
        rewardOrderLogDto.setItem_type(100);

        rewardOrderLogDtos.add(rewardOrderLogDto);
        List<RewardOrderInfoDto> result = biz.getProcessedResult(rewardOrderLogDtos);
    }
}
