package com.example.tuanfei.controller;



import cn.afterturn.easypoi.word.WordExportUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.tuanfei.Param.FeeParam;
import com.example.tuanfei.VO.ActivityFeeVO;
import com.example.tuanfei.VO.ActivityVO;
import com.example.tuanfei.entity.Activity;
import com.example.tuanfei.entity.ActivityFee;
import com.example.tuanfei.service.ActivityFeeService;
import com.example.tuanfei.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/activity-fee")
@Api(tags = "团费缴纳")
public class ActivityFeeController {

    @Resource
    private ActivityFeeService activityFeeService;
    @Resource
    private ActivityService activityService;



    @PostMapping("/payment")
    @ApiOperation(value = "缴纳费用")
    public R aaPayment(@RequestBody FeeParam feeParam){
        ActivityFee activityFee = new ActivityFee();
        BeanUtils.copyProperties(feeParam,activityFee);
      activityFee=feeParam.getIsFee()==1?activityFee.setAaFee(feeParam.getFee()):activityFee.setActivityFee(feeParam.getFee());
        //查询是否之前在这个活动上缴纳过
        ActivityFee one = activityFeeService.getOne(new QueryWrapper<ActivityFee>().eq("member_id", feeParam.getMemberId())
                .eq("activity_id", feeParam.getActivityId()));
        if (Objects.nonNull(one)){
            //追加即可
            if (Objects.nonNull(activityFee.getAaFee())) {
                if (Objects.isNull(one.getAaFee())) {
                    one.setAaFee(activityFee.getAaFee());
                } else {
                    one.setAaFee(one.getAaFee().add(activityFee.getAaFee()));
                }
            }
            if (Objects.nonNull(activityFee.getActivityFee())) {
                if (Objects.isNull(one.getActivityFee())) {
                    one.setActivityFee(activityFee.getActivityFee());
                } else {
                    one.setActivityFee(one.getActivityFee().add(activityFee.getActivityFee()));
                }
            }
        activityFeeService.updateById(one);
        }else {
            activityFeeService.save(activityFee);
        }
      return R.ok("缴纳成功");
    }

    @PostMapping("/paymentPage")
    @ApiOperation(value = "根据不同角色分页查询活动费用")
    public R activityPage(@RequestBody FeeParam feeParam){
        feeParam.setPage((feeParam.getPage()-1)*feeParam.getPage());
        List<ActivityFee> activityFees = activityFeeService.selectListPage(feeParam);
        for (ActivityFee activityFee : activityFees) {
            Activity byId = activityService.getById(activityFee.getActivityId());
            activityFee.setActivityName(byId.getActivityName());
            int id = activityService.count(new QueryWrapper<Activity>().eq("id", activityFee.getActivityId()));

          if (Objects.nonNull(activityFee.getAaFee())){
              activityFee.setRealTimeAaFee(activityFee.getAaFee().divide(BigDecimal.valueOf(id), 2, RoundingMode.HALF_UP));
          }else {
              activityFee.setRealTimeAaFee(new BigDecimal(BigInteger.ZERO));
          }
          if (Objects.nonNull(activityFee.getActivityFee())){
              activityFee.setRealTimeCostFee(activityFee.getActivityFee().add(activityFee.getRealTimeAaFee()));
          }else {
              activityFee.setRealTimeCostFee(activityFee.getRealTimeAaFee());
          }
        }
        long count=activityFeeService.selectListCount(feeParam);
        HashMap<String, Object> map = new HashMap<>();
        map.put("data",activityFees);
        map.put("total",count);
        return R.ok(map);
    }

    @PostMapping("/export")
    @ApiOperation(value = "团费导出")
    public void getExportExcel(HttpServletResponse response) throws IOException {
        List<ActivityFee> list = activityFeeService.list(null);
        ArrayList<ActivityVO> activityVOS = new ArrayList<>();
        for (ActivityFee activityFee : list) {
            Activity byId = activityService.getById(activityFee.getActivityId());
            activityFee.setActivityName(byId.getActivityName());
            int id = activityService.count(new QueryWrapper<Activity>().eq("id", activityFee.getActivityId()));
            if (Objects.nonNull(activityFee.getAaFee())){
                activityFee.setRealTimeAaFee(activityFee.getAaFee().divide(BigDecimal.valueOf(id), 2, RoundingMode.HALF_UP));
            }else {
                activityFee.setRealTimeAaFee(new BigDecimal(BigInteger.ZERO));
            }
            if (Objects.nonNull(activityFee.getActivityFee())){
                activityFee.setRealTimeCostFee(activityFee.getActivityFee().add(activityFee.getRealTimeAaFee()));
            }else {
                activityFee.setRealTimeCostFee(activityFee.getRealTimeAaFee());
            }
            ActivityVO activityVO = new ActivityVO();
            BeanUtils.copyProperties(activityFee, activityVO);
            activityVOS.add(activityVO);
        }

        // 创建Excel文件
        String fileName = "团费.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
        excelWriter.write(activityVOS, writeSheet);
        excelWriter.finish();

        // 下载Excel文件
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }
    }

@PostMapping("/wordExport/{id}")
@ApiOperation(value = "word导出")
@ResponseBody
public void getWordExport(HttpServletResponse response, @PathVariable(name = "id") Integer id) throws Exception {
            ActivityFee fee = activityFeeService.getById(id);
    Activity activity = activityService.getById(fee.getActivityId());
        fee.setActivityName(activity.getActivityName());
    if (Objects.nonNull(fee.getAaFee())){
        fee.setRealTimeAaFee(fee.getAaFee().divide(BigDecimal.valueOf(id), 2, RoundingMode.HALF_UP));
    }else {
        fee.setRealTimeAaFee(new BigDecimal(BigInteger.ZERO));
    }
    if (Objects.nonNull(fee.getActivityFee())){
        fee.setRealTimeCostFee(fee.getActivityFee().add(fee.getRealTimeAaFee()));
    }else {
        fee.setRealTimeCostFee(fee.getRealTimeAaFee());
    }
    ActivityFeeVO activityFeeVO = new ActivityFeeVO();
        BeanUtils.copyProperties(fee, activityFeeVO);
        activityFeeVO.setMainContent(activity.getMainContent());
        // 导出word并指定word导出模板
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityVO", activityFeeVO);
        XWPFDocument doc = WordExportUtil.exportWord07("https://xiaoyangjun.oss-cn-shenzhen.aliyuncs.com/moban.docx", map);

        // 设置编码格式
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 设置内容类型
        response.setContentType("application/octet-stream");

        // 设置头及文件命名
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("团费报表.docx", StandardCharsets.UTF_8.name()));

    OutputStream outputStream = response.getOutputStream();

        // 写入
        doc.write(outputStream);
    outputStream.close();
    doc.close();
}


}

