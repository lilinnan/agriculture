package com.lln.agriculture.manage.controller.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Spu;
import com.lln.agriculture.manage.service.item.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/12 9:51
 */

@RestController
@RequestMapping("/v1/item/spu")
public class SpuController {
    private final SpuService spuService;

    @Autowired
    public SpuController(SpuService spuService) {
        this.spuService = spuService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SPU_INSERT')")
    public Message insertSpu(@RequestBody Spu spu) {
        return spuService.insertSpu(spu);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SPU_SELECT')")
    public Message selectAllSpuSimple(PageRequest pageRequest) {
        return spuService.selectAllSpuSimple(pageRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SPU_SELECT')")
    public Message selectSpu(@PathVariable int id) {
        return spuService.selectOneSpu(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SPU_DELETE')")
    public Message deleteSpu(@PathVariable int id) {
        return spuService.deleteSpu(id);
    }


    @PutMapping
    @PreAuthorize("hasAuthority('SPU_UPDATE')")
    public Message updateSpu(@RequestBody Spu spu) {
        return spuService.updateSpu(spu);
    }

    @PutMapping("/saleable")
    @PreAuthorize("hasAuthority('SPU_UPDATE')")
    public Message saleable(@RequestBody Spu spu) {
        return spuService.updateSaleable(spu);
    }

}
