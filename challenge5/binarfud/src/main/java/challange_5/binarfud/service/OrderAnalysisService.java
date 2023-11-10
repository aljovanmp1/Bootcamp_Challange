package challange_5.binarfud.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challange_5.binarfud.model.Merchant;
import challange_5.binarfud.model.dto.OrderAnalysisDto;
import challange_5.binarfud.model.dto.OrderAnalysisMonthly;
import challange_5.binarfud.model.dto.OrderAnalysisWeekly;

@Service
public class OrderAnalysisService {
    @Autowired MerchantService merchantService;

    final String[] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public OrderAnalysisDto retrieveAnalysisData(String period, Merchant merchant) {
        OrderAnalysisDto orderDto = new OrderAnalysisDto();
        orderDto.setMerchantId(merchant.getId());
        orderDto.setMerchantLocation(merchant.getMerchantLocation());
        orderDto.setMerchantName(merchant.getMerchantName());

        Integer totalIncome = 0;
        Integer totalQty = 0;
        if (period.equals("month")) {
            List<OrderAnalysisMonthly> monthlyData = merchantService
                    .getOrderAnalysisMonthly(merchant.getId());
            List<OrderAnalysisDto.Data> analysisData = new ArrayList<>();
            for (OrderAnalysisMonthly monthly : monthlyData) {
                OrderAnalysisDto.Data newData = new OrderAnalysisDto.Data();
                newData.setIncome(monthly.getPrice());
                newData.setPeriod(month[monthly.getMonthCount() - 1]);
                newData.setQty(monthly.getQty());
                analysisData.add(newData);

                totalIncome += monthly.getPrice();
                totalQty += monthly.getQty();
            }
            orderDto.setTotalPrice(totalIncome.toString());
            orderDto.setTotalQty(totalQty);
            orderDto.setData(analysisData);
        }

        else if (period.equals("week")) {
            List<OrderAnalysisWeekly> weeklyData = merchantService
                    .getOrderAnalysisWeekly(merchant.getId());
            List<OrderAnalysisDto.Data> analysisData = new ArrayList<>();
            for (OrderAnalysisWeekly weekly : weeklyData) {
                OrderAnalysisDto.Data newData = new OrderAnalysisDto.Data();
                newData.setIncome(weekly.getPrice());
                newData.setPeriod("W" + weekly.getWeekMonth() + " " + month[weekly.getMonthCount() - 1]);
                newData.setQty(weekly.getQty());
                analysisData.add(newData);

                totalIncome += weekly.getPrice();
                totalQty += weekly.getQty();
            }
            orderDto.setTotalPrice(totalIncome.toString());
            orderDto.setTotalQty(totalQty);
            orderDto.setData(analysisData);
        }

        return orderDto;
    }

    public String getHtmlStr(OrderAnalysisDto data) {
        return """
                            <!DOCTYPE html>
                <html>

                <head>
                    <style>
                        body {
                            margin: 50px;
                        }

                        .header {
                            display: flex;
                            flex-direction: column;
                            margin-bottom: 30px;
                        }

                        .header h1 {
                            border-bottom: 2px black solid;
                            margin-bottom: 10px;
                        }

                        .bold {
                            font-weight: bold;
                        }

                        .order-info {
                            display: flex;
                            flex-direction: row;
                            justify-content: flex-start;
                        }

                        .order {
                            display: flex;
                            flex-direction: column;
                            gap: 20px;
                            margin-bottom: 40px;
                        }

                        .order h3 {
                            margin: 0;
                        }

                        .item {
                            display: flex;
                            flex-direction: column;
                            margin-right: 50px;
                        }

                        .order-detail{
                            margin-top: 40px;
                        }

                        .order-detail span{
                            font-weight: bold;
                        }

                        table {
                            border-collapse: collapse;
                            margin-top: 10px;
                        }

                        table td {
                            padding: 15px;
                            max-width: 100px;
                            width: 100px;
                            overflow: auto;
                        }

                        table td:nth-child(1) {
                            max-width: 20px;
                            width: 20px;
                            padding: 5px;
                        }

                        table td:nth-child(2) {
                            max-width: 200px;
                            width: 200px;
                            padding: 15px;
                        }

                        table thead td {
                            background-color: #c7cace;
                            color: #3e3333;
                            font-weight: bold;
                            font-size: 13px;
                            border: 1px solid #b6b9bd;
                            text-align: center;
                        }

                        table tbody td {
                            color: #636363;
                            border: 1px solid #dddfe1;
                            text-align: center;
                        }

                        .total table tbody td:nth-last-child(2) {
                            max-width: 80px;
                            width: 80px;
                        }
                        .total table tbody td:last-child {
                            max-width: 150px;
                            width: 150px;
                        }

                        .total table tbody td {
                            color: #fff;
                            border: none;
                            max-width: 200px;
                            text-align: start;
                        }

                        table tbody tr {
                            background-color: #f9fafb;
                        }

                        table tbody tr:nth-child(odd) {
                            background-color: #ffffff;
                        }

                        .grand-total{
                            display: flex;
                            flex-direction: column;
                            gap: 5px;
                            background-color: #808080;
                            padding: 10px;
                            border-radius: 4px;
                        }

                        /* .total {
                            display: flex;
                            justify-content: flex-end;
                        } */
                    </style>
                </head>

                <body>

                    <!-- Used to display heading content -->
                    <div class="header">
                        <h1>Order Analysis</h1>
                        <span>Merchant id&nbsp;""" + data.getMerchantId() + """
                        </span>
                </div>

                <div class="order">
                    <h3 class="bold">Merchant Infromation</h3>
                    <div class="order-info">
                        <div class="item">
                            <span class="bold">Name</span>
                            <span> """ + data.getMerchantName() + """
                        </span>
                </div>
                <div class="item">
                    <span class="bold">Location</span>
                    <span>""" + data.getMerchantLocation() + """
                                </span>
                        </div>
                    </div>

                </div>""" + getAnalysisData(data.getData()) + """

                <div class="total">
                    <table>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <div class="grand-total">
                                    <span class="bold">Total</span>
                                    <span>Quantity&nbsp:&nbsp;""" + data.getTotalQty() + """
                </span>
                <span>Income&nbsp&nbsp&nbsp:&nbsp;""" + data.getTotalPrice() + """
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </body>

                </html>""";
    }

    private String getAnalysisData(List<OrderAnalysisDto.Data> data) {
        String resp = "";

        resp += """
                <div class="order-detail">
                <table>
                    <thead>
                        <tr>
                            <td>No</td>
                            <td>Period</td>
                            <td>Product Quantity</td>
                            <td>Income</td>
                        </tr>
                    </thead> """;
        int ind = 1;
        for (OrderAnalysisDto.Data dataDetail : data) {
            resp += """
                    <tr>
                        <td>""" + ind + """
                        </td>
                    <td>""" + dataDetail.getPeriod() + """
                        </td>
                    <td>""" + dataDetail.getQty() + """
                        </td>
                    <td>""" + dataDetail.getIncome() + """
                        </td>
                    </tr>""";
            ind++;
        }

        resp += """
                    </table>
                </div>
                """;

        return resp;
    }
}
