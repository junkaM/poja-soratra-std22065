package hei.school.soratra.endpoint.rest.controller.soratra;

import hei.school.soratra.service.soratra.SoratraService;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SoratraController {

  @Autowired private SoratraService soratraService;

  @RequestMapping(method = RequestMethod.PUT, path = "/soratra/{id}")
  public String uploadFile(@PathVariable String id, @RequestBody File original) throws IOException {
    return soratraService.uploadFile(id, original);
  }

  @GetMapping("/soratra/{id}")
  public Map<String, String> getSoratraUrl(@PathVariable String id) {

    String originalUrl = soratraService.getOriginalSoratraUrl(id);
    String transformedUrl = soratraService.getTransformedSoratraUrl(id);

    Map<String, String> response = new HashMap<>();
    response.put("original_url", originalUrl);
    response.put("transformed_url", transformedUrl);
    return response;
  }
}
