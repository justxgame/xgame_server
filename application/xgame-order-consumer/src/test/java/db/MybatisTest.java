package db;

import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.util.MybatisSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class MybatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisSqlSessionFactory.getSqlSessionAutoCommit();
        RewardOrderLogDao rewardOrderLogDao = sqlSession.getMapper(RewardOrderLogDao.class);
        List<RewardOrderLogDto> rewardOrderLogDtos = rewardOrderLogDao.getAll();

        for (RewardOrderLogDto rewardOrderLogDto:rewardOrderLogDtos){
            System.out.println(rewardOrderLogDto.getOrder_id());
        }

        RewardOrderInfoDao rewardOrderInfoDao = sqlSession.getMapper(RewardOrderInfoDao.class);
        RewardOrderInfoDto rewardOrderInfoDto = new RewardOrderInfoDto();
        rewardOrderInfoDto.setOrderId(String.valueOf(System.currentTimeMillis()));


        List<RewardOrderInfoDto> rewardOrderInfoDtos = new ArrayList<>();
        RewardOrderInfoDto rewardOrderInfoDto2 = new RewardOrderInfoDto();
        rewardOrderInfoDto2.setOrderId(String.valueOf(System.currentTimeMillis()+1));
        rewardOrderInfoDtos.add(rewardOrderInfoDto);
        rewardOrderInfoDtos.add(rewardOrderInfoDto2);

        rewardOrderInfoDao.saveObjects(rewardOrderInfoDtos);

    }
}
