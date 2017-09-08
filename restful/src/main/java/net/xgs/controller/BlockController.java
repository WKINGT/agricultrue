package net.xgs.controller;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;

import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.model.BaseBlockHistoryData;
import net.xgs.services.BlockDataService;

/**
 * Created by duai on 2017-08-11.
 */
@Controller("/block")
public class BlockController extends BaseController {
    @Inject
    BlockDataService blockDataService;

    @Before(POST.class)
    public Object findInfo(){
        String blockId = getPara("blockId");
        if (StringUtils.isBlank(blockId)){//如果是微信端访问将从url截取数据
            throwException("block.info.block.error");
        }
        BaseBlockHistoryData baseBlockHistoryData = blockDataService.findMaxCreateTimeByBlock(blockId);
        if (baseBlockHistoryData==null){
            throwException("block.info.error");
        }
        baseBlockHistoryData = blockDataService.findInfoByBlockId(baseBlockHistoryData);
        return baseBlockHistoryData;
    }
    @Before(POST.class)
    public Object findInfoByBlockId(){
        String blockId = getPara("blockId");
        BaseBlockHistoryData baseBlockHistoryData  = blockDataService.findMaxCreateTimeByBlock(blockId);
        return baseBlockHistoryData;
    }

}
