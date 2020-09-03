package com.github.tuding.blindbox.infrastructure.util;

import com.github.tuding.blindbox.domain.order.OrderStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StreamingCsvResultSetExt implements ResultSetExtractor<Void> {
    final private static char DELIMITER = ',';

    private final OutputStream outputStream;

    public StreamingCsvResultSetExt(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    private String remapStatus(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equals(status)) {
                return orderStatus.getDescription();
            }
        }
        return "Unknown";
    }

    @Override
    public Void extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            writeHeader(printWriter);
            while (resultSet.next()) {
                String orderId = resultSet.getString("orderId");
                String productName = resultSet.getString("productName");
                String createTime = resultSet.getString("createTime");
                String receiver = resultSet.getString("receiver");
                String mobile = resultSet.getString("mobile");
                String area = resultSet.getString("area");
                String detailAddress = resultSet.getString("detailAddress");
                String shippingCompany = resultSet.getString("shippingCompany");
                String shippingTicket = resultSet.getString("shippingTicket");
                String status = resultSet.getString("status");

                printWriter.write(orderId != null ? orderId : "");
                printWriter.append(DELIMITER);
                printWriter.write(productName != null ? productName : "");
                printWriter.append(DELIMITER);
                printWriter.write(createTime != null ? createTime : "");
                printWriter.append(DELIMITER);
                printWriter.write(receiver != null ? receiver : "");
                printWriter.append(DELIMITER);
                printWriter.write(mobile != null ? mobile : "");
                printWriter.append(DELIMITER);
                printWriter.write(mobile != null ? area : "");
                printWriter.append(DELIMITER);
                printWriter.write(detailAddress != null ? detailAddress : "");
                printWriter.append(DELIMITER);
                printWriter.write(shippingCompany != null ? shippingCompany : "");
                printWriter.append(DELIMITER);
                printWriter.write(shippingTicket != null ? shippingTicket : "");
                printWriter.append(DELIMITER);
                printWriter.write(status != null ? remapStatus(status) : "");
                printWriter.println();
            }
            printWriter.flush();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;

    }


    private static void writeHeader(final PrintWriter printWriter) throws SQLException {
        printWriter.write("订单号");
        printWriter.append(DELIMITER);
        printWriter.write("产品名称");
        printWriter.append(DELIMITER);
        printWriter.write("创建时间");
        printWriter.append(DELIMITER);
        printWriter.write("收件人");
        printWriter.append(DELIMITER);
        printWriter.write("收件人电话");
        printWriter.append(DELIMITER);
        printWriter.write("收件人地址");
        printWriter.append(DELIMITER);
        printWriter.write("快递公司");
        printWriter.append(DELIMITER);
        printWriter.write("快递号");
        printWriter.append(DELIMITER);
        printWriter.write("状态");
        printWriter.println();
    }
}
