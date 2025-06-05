package studio.aroundhub.opensourceproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.aroundhub.opensourceproject.dto.cafeteriaDto.CafeteriaResponse;
import studio.aroundhub.opensourceproject.service.CafeteriaService;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class  CafeteriaController {
    private final CafeteriaService cafeteriaService;

    //식단표 이미지 가져오기
    @GetMapping("/cafeteria")
    public ResponseEntity<CafeteriaResponse> getLatestCafeteriaImages() {
        try {
            Map<String, String> images = cafeteriaService.getCafeteria();
            if (images.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            String natural = images.get("natural");
            String student = images.get("student");
            CafeteriaResponse response = new CafeteriaResponse(natural, student); 
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("식단표 이미지 가져오기 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }




}

