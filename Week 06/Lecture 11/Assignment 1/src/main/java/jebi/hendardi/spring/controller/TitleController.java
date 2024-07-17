package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;
import jebi.hendardi.spring.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @GetMapping
    public List<Title> getAllTitles() {
        return titleService.getAllTitles();
    }

    @GetMapping("/{empNo}/{title}/{fromDate}")
    public ResponseEntity<Title> getTitleById(@PathVariable Integer empNo, @PathVariable String title, @PathVariable String fromDate) {
        TitleId id = new TitleId(empNo, title, java.sql.Date.valueOf(fromDate));
        return titleService.getTitleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Title createTitle(@RequestBody Title title) {
        return titleService.createOrUpdateTitle(title);
    }

    @PutMapping("/{empNo}/{title}/{fromDate}")
    public Title updateTitle(@PathVariable Integer empNo, @PathVariable String title, @PathVariable String fromDate, @RequestBody Title titleUpdate) {
        TitleId id = new TitleId(empNo, title, java.sql.Date.valueOf(fromDate));
        titleUpdate.setId(id);
        return titleService.createOrUpdateTitle(titleUpdate);
    }

    @DeleteMapping("/{empNo}/{title}/{fromDate}")
    public ResponseEntity<?> deleteTitle(@PathVariable Integer empNo, @PathVariable String title, @PathVariable String fromDate) {
        TitleId id = new TitleId(empNo, title, java.sql.Date.valueOf(fromDate));
        titleService.deleteTitle(id);
        return ResponseEntity.ok().build();
    }
}
