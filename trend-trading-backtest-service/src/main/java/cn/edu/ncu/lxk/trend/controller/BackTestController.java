package cn.edu.ncu.lxk.trend.controller;

import cn.edu.ncu.lxk.trend.pojo.AnnualProfit;
import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.edu.ncu.lxk.trend.pojo.Profit;
import cn.edu.ncu.lxk.trend.pojo.Trade;
import cn.edu.ncu.lxk.trend.service.BackTestService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BackTestController
{
    @Autowired
    private BackTestService backTestService;

    @CrossOrigin
    @GetMapping("/simulate/{code}/{ma}/{buyThreshold}/{sellThreshold}/{serviceCharge}/{startDate}/{endDate}")
    public Map<String,Object> backTest(@PathVariable("code") String code,
                                    @PathVariable("ma") int ma,
                                    @PathVariable("buyThreshold") float buyThreshold,
                                    @PathVariable("sellThreshold") float sellThreshold,
                                    @PathVariable("serviceCharge") float serviceCharge,
                                    @PathVariable("startDate")String startDate,
                                    @PathVariable("endDate") String endDate)
    {
        List<IndexData> indexData = backTestService.listIndexData(code);
        indexData = this.getIndexDataByDate(indexData,startDate,endDate);

        //计算指数投资收益
        Map<String,?> simulateResult = backTestService.simulate(ma,sellThreshold,buyThreshold,serviceCharge,indexData);
        List<Profit> profits = (List<Profit>) simulateResult.get("profits");

        //计算交易情况
        List<Trade> trades = (List<Trade>) simulateResult.get("trades");

        //收益分布对比图
        List<AnnualProfit> annualProfits = (List<AnnualProfit>) simulateResult.get("annualProfits");

        //获取交易赚和亏的笔数
        int winCount = (Integer) simulateResult.get("winCount");
        int lossCount = (Integer) simulateResult.get("lossCount");
        float avgWinRate = (Float) simulateResult.get("avgWinRate");
        float avgLossRate = (Float) simulateResult.get("avgLossRate");

        //计算指数的收益和趋势投资的收益，年化收益
        float years = backTestService.dataYear(indexData);
        float indexIncomeTotal = (indexData.get(indexData.size()-1).getClosePoint() - indexData.get(0).getClosePoint()) / indexData.get(0).getClosePoint();
        float indexIncomeAnnual = (float) Math.pow(1+indexIncomeTotal, 1/years) - 1;
        float trendIncomeTotal = (profits.get(profits.size()-1).getValue() - profits.get(0).getValue()) / profits.get(0).getValue();
        float trendIncomeAnnual = (float) Math.pow(1+trendIncomeTotal, 1/years) - 1;

        Map<String,Object> map = new HashMap<>();
        map.put("indexDatas",indexData);
        map.put("indexStartDate",startDate);
        map.put("indexEndDate",endDate);
        map.put("profits",profits);
        map.put("trades",trades);
        map.put("years", years);
        map.put("indexIncomeTotal", indexIncomeTotal);
        map.put("indexIncomeAnnual", indexIncomeAnnual);
        map.put("trendIncomeTotal", trendIncomeTotal);
        map.put("trendIncomeAnnual", trendIncomeAnnual);
        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("avgWinRate", avgWinRate);
        map.put("avgLossRate", avgLossRate);
        map.put("annualProfits", annualProfits);
        return map;
    }


    private List<IndexData> getIndexDataByDate(List<IndexData> allIndexData, String strStartDate, String strEndDate) {
        if(StrUtil.isBlankOrUndefined(strStartDate) || StrUtil.isBlankOrUndefined(strEndDate) )
            return allIndexData;

        List<IndexData> result = new ArrayList<>();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);

        for (IndexData indexData : allIndexData) {
            Date date =DateUtil.parse(indexData.getDate());
            if(
                    date.getTime()>=startDate.getTime() &&
                            date.getTime()<=endDate.getTime()
            ) {
                result.add(indexData);
            }
        }
        return result;
    }
}
