package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;
import jebi.hendardi.spring.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    public Title addTitle(Title title) {
        return titleRepository.save(title);
    }

    public Title updateTitle(int empNo, Title title) {
        TitleId titleId = new TitleId();
        titleId.setEmployee(empNo);
        titleId.setTitle(title.getTitle());
        titleId.setFromDate(title.getFromDate());

        if (titleRepository.existsById(titleId)) {
            return titleRepository.save(title);
        }
        return null;
    }
}
