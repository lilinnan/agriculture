package com.lln.agriculture.user.controller.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.user.service.item.SpuService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/17 13:24
 */


@RestController
@RequestMapping("/v1/item/spu")
public class SpuController {

    private final SpuService spuService;

    public SpuController(SpuService spuService) {
        this.spuService = spuService;
    }


    @GetMapping("/latest/{num}")
    public Message getLatestSpu(@DefaultValue(value = "5") @PathVariable int num) {
        return spuService.latestSimpleSpu(num);
    }

    @GetMapping("/search")
    public Message getLatestSpu(PageRequest pageRequest) {
        return spuService.search(pageRequest);
    }

    @GetMapping("/info/{spuId}")
    public Message getSpu(@PathVariable int spuId) {
        return spuService.selectSpuById(spuId);
    }


    @GetMapping("/buy-info/{skuId}")
    public Message getBuyInfoBySkuId(@PathVariable int skuId) {
        return spuService.selectBuyInfoBySkuId(skuId);
    }
}
