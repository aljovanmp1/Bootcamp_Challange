package challange_5.binarfud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import challange_5.binarfud.model.dto.InvoiceDto;

@Service
public class InvoiceService {
    public String getHtmlStr(InvoiceDto data) {
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
                        <h1>Invoice</h1>
                        <span>Order Id&nbsp;""" + data.getOrderId() + """
                        </span>
                </div>

                <div class="order">
                    <h3 class="bold">Bill To</h3>
                    <div class="order-info">
                        <div class="item">
                            <span class="bold">Name</span>
                            <span> """ + data.getUserName() + """
                        </span>
                </div>
                <div class="item">
                    <span class="bold">Order Time</span>
                    <span>""" + data.getOrderTime() + """
                        </span>
                </div>
                <div class="item">
                    <span class="bold">Destination </span>
                    <span>""" + data.getDest() + """
                                </span>
                        </div>
                    </div>

                </div>""" + getOrderDetails(data.getOrders()) + """

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
                <span>Price&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp:&nbsp;""" + data.getTotalPrice() + """
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </body>

                </html>""";
    }

    private String getOrderDetails(List<InvoiceDto.OrderPerMerchant> datas) {
        String resp = "";

        for (InvoiceDto.OrderPerMerchant data : datas) {
            resp += """
                    <div class="order-detail">
                    <span>Merchant:&nbsp;""" + data.getMerchantName() + """
                            </span>

                    <table>
                        <thead>
                            <tr>
                                <td>No</td>
                                <td>Product Name</td>
                                <td>Price</td>
                                <td>Quantity</td>
                                <td>Total</td>
                            </tr>
                        </thead> """;
            int ind = 1;
            for (InvoiceDto.OrderPerMerchant.OrderDetailWithProduct orderDetail : data.getOrderData()) {
                resp += """
                        <tr>
                            <td>""" + ind + """
                            </td>
                        <td>""" + orderDetail.getProductName() + """
                            </td>
                        <td>""" + orderDetail.getPrice() + """
                            </td>
                        <td>""" + orderDetail.getQty() + """
                            </td>
                        <td>""" + orderDetail.getTotal() + """
                                </td>
                        </tr>""";
                ind++;
            }

            resp += """
                        </table>
                    </div>
                    """;

        }

        return resp;
    }
}
