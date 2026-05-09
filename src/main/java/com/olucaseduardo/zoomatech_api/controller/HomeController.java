package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.home.HomePageResponseDTO;
import com.olucaseduardo.zoomatech_api.services.HomeService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/website")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<ApiResponse<HomePageResponseDTO>> getHomePageData()
    {
        HomePageResponseDTO responseDTO = homeService.findHomePageData();
        return ResponseEntity.ok(ResponseUtil.success("Dados recuperados com sucesso!",responseDTO,null));
    }
}
