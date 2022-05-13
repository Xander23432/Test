package com.realnet.comm.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import springfox.documentation.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity.SingleSerise;
import com.realnet.fnd.response.OperationResponse.ResponseStatusEnum;
import com.realnet.fnd.response.SingleDataSeriseResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Order"})
public class OrderStatsController {

    @Autowired private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "Order Stats", response = SingleDataSeriseResponse.class)
    @RequestMapping(value = "/order-stats/{type}", method = RequestMethod.GET)
    public SingleDataSeriseResponse getOrderStats(@PathVariable("type") String type ) {
        String fieldName = "";
        if (type.equalsIgnoreCase("status") || type.equalsIgnoreCase("order_status")){
            fieldName = " order_status ";
        }
        else if (type.equalsIgnoreCase("paytype") || type.equalsIgnoreCase("payment_type")){
            fieldName = " payment_type ";
        }
        else if (type.equalsIgnoreCase("country") || type.equalsIgnoreCase("ship_country")){
            fieldName = " ship_country ";
        }
        else{
            fieldName = " order_status ";
        }

        String sql = "select count(*) as value, " + fieldName + " as name from orders group by " + fieldName;
        String countType = new String();
        long count;
        SingleSerise singleSerise;
        SingleDataSeriseResponse resp = new SingleDataSeriseResponse();
        ArrayList<SingleSerise> dataItemList = new ArrayList<SingleSerise>();


        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> row : list) {
            //singleSerise = new SingleSerise((String)row.get("name"), new BigDecimal((long)row.get("value")) );
        	BigDecimal value=(BigDecimal) row.get("value");
        	singleSerise = new SingleSerise((String)row.get("name"), value);
            dataItemList.add(singleSerise);
        }
        resp.setItems(dataItemList);
        resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
        resp.setOperationMessage("Orders by " + fieldName);
        //resp.setItems(singleSerise);
        return resp;
    }


}
