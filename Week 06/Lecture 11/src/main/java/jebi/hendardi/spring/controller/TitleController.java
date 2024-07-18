package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/titles")
public class TitleController {

    @Autowired
    private TitleService titleService;

    @PostMapping
    public Title addTitle(@RequestBody Title title) {
        return titleService.addTitle(title);
    }

    @PutMapping("/{empNo}")
    public Title updateTitle(@PathVariable int empNo, @RequestBody Title title) {
        return titleService.updateTitle(empNo, title);
    }
}
