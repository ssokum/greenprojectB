package com.example.greenprojectB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

  @GetMapping({"/"})
  public String homeGet() {
    return "main";
  }

  @GetMapping("/error/accessDenied")
  public String accessDeniedGet() {
    return "error/accessDenied";
  }

}
