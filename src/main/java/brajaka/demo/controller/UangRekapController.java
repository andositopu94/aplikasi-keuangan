package brajaka.demo.controller;

import brajaka.demo.dto.UangKeluarRekapDto;
import brajaka.demo.dto.UangMasukRekapDto;
import brajaka.demo.service.BukuUtamaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rekap")
public class UangRekapController {
    private final BukuUtamaService bukuUtamaService;

    public UangRekapController(BukuUtamaService bukuUtamaService){
        this.bukuUtamaService=bukuUtamaService;
    }

    @GetMapping("/masuk")
    public List<UangMasukRekapDto>getRekapUangMasuk(){
        return bukuUtamaService.getRekapUangMasuk();
    }
    @GetMapping("/keluar")
    public List<UangKeluarRekapDto>getRekapUangKeluar(){
        return bukuUtamaService.getRekapUangKeluar();
    }
}
