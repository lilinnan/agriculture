package com.lln.agriculture.manage.controller.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.service.item.SpuRecycleBinService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/15 9:41
 */


@RestController
@RequestMapping("/v1/item/spu-recycle-bin")
public class SpuRecycleBinController {

    private final SpuRecycleBinService spuRecycleBinService;

    public SpuRecycleBinController(SpuRecycleBinService spuRecycleBinService) {
        this.spuRecycleBinService = spuRecycleBinService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RECYCLE_BIN_SELECT')")
    public Message selectAllDeleted(PageRequest pageRequest) {
        return spuRecycleBinService.selectAllDeletedSpu(pageRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RECYCLE_BIN_RESTORE')")
    public Message restoreDeletedSpu(@PathVariable int id) {
        return spuRecycleBinService.restoreDeletedSpu(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RECYCLE_BIN_DELETE')")
    public Message readDeleteSpu(@PathVariable int id) {
        return spuRecycleBinService.realDeleteSpu(id);
    }
}
