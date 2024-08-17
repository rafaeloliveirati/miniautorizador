package com.vr.mini.autorizador.miniautorizador.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceCheckController {

  @GetMapping("/resource-check")
  public ResponseEntity.BodyBuilder checkResource() {
    return ResponseEntity.ok();
  }
}
