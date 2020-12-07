package cn.edu.ncu.lxk.trend.service;

import cn.edu.ncu.lxk.trend.client.IndexDataClient;
import cn.edu.ncu.lxk.trend.pojo.AnnualProfit;
import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.edu.ncu.lxk.trend.pojo.Profit;
import cn.edu.ncu.lxk.trend.pojo.Trade;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BackTestService
{
    @Autowired
    private IndexDataClient indexDataClient;

    public List<IndexData> listIndexData(String code)
    {
        //调用feign获取了对应的数据
        List<IndexData> indexData = indexDataClient.getIndexData(code);
        //因为从缓存中读取的数据是最新的在前，老的在后面，但是在前端页面上横轴按时间先后顺序，所以倒置一下
        Collections.reverse(indexData);
        return indexData;
    }

    public Map<String,Object> simulate(int ma, float sellRate, float buyRate, float serviceCharge, List<IndexData> indexData)
    {
        List<Profit> profits = new ArrayList<>();
        List<Trade> trades = new ArrayList<>();
        float initCash = 1000;
        float cash = initCash;
        float share = 0;
        float value = 0;
        float init = 0;

        int winCount = 0;
        float totalWinRate = 0;
        float avgWinRate = 0;
        float totalLossRate = 0;
        int lossCount = 0;
        float avgLossRate = 0;

        if (!indexData.isEmpty())
            init = indexData.get(0).getClosePoint();

        for (int i = 0; i < indexData.size(); i++)
        {
            IndexData data = indexData.get(i);
            float closePoint = data.getClosePoint();
            float max = getMax(i,ma,indexData);
            float avg = getMA(i,ma,indexData);

            float increaseRate = closePoint / avg;
            float decreaseRate = closePoint / max;
            if (avg != 0)
            {
                if (increaseRate > buyRate)
                {
                    if (share == 0)
                    {
                        share += cash / closePoint;
                        cash = 0;

                        Trade trade = new Trade();
                        trade.setBuyDate(data.getDate());
                        trade.setBuyClosePoint(data.getClosePoint());
                        trade.setSellDate("n/a");
                        trade.setSellClosePoint(0);
                        trades.add(trade);
                    }
                }
                else if (decreaseRate > sellRate)
                {
                    if (share != 0)
                    {
                        cash += share * closePoint * (1 - serviceCharge);
                        share = 0;

                        Trade trade = trades.get(trades.size() - 1);
                        trade.setSellDate(data.getDate());
                        trade.setSellClosePoint(data.getClosePoint());
                        float rate = cash / initCash;
                        trade.setRate(rate);

                        float earn = trade.getSellClosePoint() - trade.getBuyClosePoint();
                        if (earn > 0)
                        {
                            totalWinRate += earn / trade.getBuyClosePoint();
                            winCount++;
                        }
                        else
                        {
                            totalLossRate += earn / trade.getBuyClosePoint();
                            lossCount++;
                        }
                    }
                }
            }

            value = share * closePoint + cash;
            float rate = value / initCash;
            Profit profit = new Profit();
            profit.setDate(data.getDate());
            profit.setValue(init * rate);
            profits.add(profit);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("profits",profits);
        map.put("trades",trades);

        avgLossRate = totalLossRate / lossCount;
        avgWinRate = totalWinRate / winCount;
        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("avgWinRate", avgWinRate);
        map.put("avgLossRate", avgLossRate);

        List<AnnualProfit> annualProfits = calculateAnnualProfits(indexData,profits);
        map.put("annualProfits",annualProfits);

        return map;
    }

    private List<AnnualProfit> calculateAnnualProfits(List<IndexData> indexData, List<Profit> profits)
    {
        List<AnnualProfit> result = new ArrayList<>();
        String startDate = indexData.get(0).getDate();
        String endDate = indexData.get(indexData.size() - 1).getDate();
        int startYear = getYearOfDate(startDate);
        int endYear = getYearOfDate(endDate);
        for (int year = startYear; year <= endYear; year++)
        {
            AnnualProfit annualProfit = new AnnualProfit();
            annualProfit.setYear(year);
            float indexIncome = getIndexIncome(year,indexData);
            float trendIncome = getTrendIncome(year, profits);
            annualProfit.setIndexIncome(indexIncome);
            annualProfit.setTrendIncome(trendIncome);
            result.add(annualProfit);
        }
        return result;
    }

    //获取一年的指数收益
    private float getIndexIncome(int year, List<IndexData> indexData)
    {
        IndexData first = null;
        IndexData last = null;
        for (IndexData data : indexData)
        {
            String date = data.getDate();
            int curYear = getYearOfDate(date);
            if (year == curYear)
            {
                if (first == null)
                    first = data;
                last = data;
            }
        }
        return (last.getClosePoint() - first.getClosePoint()) / first.getClosePoint();
    }

    //获取一年趋势投资的收益
    private float getTrendIncome(int year, List<Profit> profits)
    {
        Profit first=null;
        Profit last=null;
        for (Profit profit : profits) {
            String strDate = profit.getDate();
            int currentYear = getYearOfDate(strDate);
            if(currentYear == year) {
                if(null==first)
                    first = profit;
                last = profit;
            }
            if(currentYear > year)
                break;
        }
        return (last.getValue() - first.getValue()) / first.getValue();
    }

    private static float getMA(int i, int ma, List<IndexData> list)
    {
        int start = i - 1- ma;
        if (start < 0)
            return 0;
        int now = i - 1;

        float sum = 0;
        float avg = 0;
        for (int j = start; j < now; j++)
            sum += list.get(j).getClosePoint();
        avg = sum / ma;
        return avg;
    }

    private static float getMax(int i, int ma, List<IndexData> list )
    {
        int start = i - 1- ma;
        if (start < 0)
            return 0;
        int now = i - 1;
        float max = 0;
        for (int j = start; j < now; j++)
            max = Math.max(max,list.get(j).getClosePoint());
        return max;
    }

    public float dataYear(List<IndexData> indexData)
    {
        if (indexData == null || indexData.size() == 0)
            return 0f;
        String start = indexData.get(0).getDate();
        String end = indexData.get(indexData.size() - 1).getDate();
        Date startDate = DateUtil.parseDate(start);
        Date endDate = DateUtil.parseDate(end);
        long days = DateUtil.between(startDate,endDate, DateUnit.DAY);
        return days / 365f;
    }

    private int getYearOfDate(String date)
    {
        String year = StrUtil.subBefore(date,"-",false);
        return Convert.toInt(year);
    }
}
