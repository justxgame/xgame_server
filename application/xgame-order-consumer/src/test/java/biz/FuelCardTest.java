package biz;

import com.xgame.order.consumer.biz.FuelCardRechargeBiz;
import com.xgame.order.consumer.db.dao.RewardBoxDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.util.MybatisSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class FuelCardTest {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisSqlSessionFactory.getSqlSessionAutoCommit();
        RewardBoxDao rewardBoxDao = sqlSession.getMapper(RewardBoxDao.class);
        List<RewardBoxDto> rewardBoxDtos = rewardBoxDao.getAll();
        FuelCardRechargeBiz biz = new FuelCardRechargeBiz(sqlSession,rewardBoxDtos);

        List<RewardOrderLogDto> rewardOrderLogDtos = new ArrayList<>();
        RewardOrderLogDto rewardOrderLogDto = new RewardOrderLogDto();
        rewardOrderLogDto.setOrder_id(String.valueOf(System.currentTimeMillis()));
        rewardOrderLogDto.setIndate("2017-08-20 16:05:43");
        rewardOrderLogDto.setServer_id("1000");
        rewardOrderLogDto.setItem_id(10201);
        rewardOrderLogDto.setItem_count(1);
        rewardOrderLogDto.setUid("1001");
        rewardOrderLogDtos.add(rewardOrderLogDto);
//        List<RewardOrderInfoDto> result = biz.getProcessedResult(rewardOrderLogDtos);
    }
}
