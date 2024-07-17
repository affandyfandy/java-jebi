package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;
import jebi.hendardi.spring.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final TitleRepository titleRepository;

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public Optional<Title> getTitleById(TitleId id) {
        return titleRepository.findById(id);
    }

    public Title createOrUpdateTitle(Title title) {
        return titleRepository.save(title);
    }

    public void deleteTitle(TitleId id) {
        titleRepository.deleteById(id);
    }
}
