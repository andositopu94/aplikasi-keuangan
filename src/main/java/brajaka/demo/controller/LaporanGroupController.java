package brajaka.demo.controller;

import brajaka.demo.dto.LaporanGroupDto;
import brajaka.demo.service.BukuUtamaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/laporan/group")
public class LaporanGroupController {
    private final BukuUtamaService bukuUtamaService;

    public LaporanGroupController(BukuUtamaService bukuUtamaService){
        this.bukuUtamaService=bukuUtamaService;
    }
    @GetMapping("/akun")
    public List<LaporanGroupDto>getGroupByAkun(){
        return bukuUtamaService.getLaporanGroupByAkun();
    }
    @GetMapping("/kegiatan")
    public List<LaporanGroupDto>getGroupByKegiatan(){
        return bukuUtamaService.getLaporanGroupByKegiatan();
    }
}
