package placeholder.organisation.unicms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class FilterAndSorterOfEntities {

    private final int pageSize;

    public FilterAndSorterOfEntities(@Value("${table.page_size:10}") int pageSize) {
        this.pageSize = pageSize;
    }

    public <T> Page<T> getFilteredAndSortedEntities(String sortField, String sortDir,
                                                    JpaSpecificationExecutor<T> repo,
                                                    Specification<T> specification,
                                                    int pageNo) {

        Sort sort;
        if ("none".equalsIgnoreCase(sortDir)) {
            sort = Sort.unsorted();
        } else {
            if ("asc".equals(sortDir)) {
                sort = Sort.by(sortField).ascending();
            } else {
                sort = Sort.by(sortField).descending();
            }
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return repo.findAll(specification, pageable);
    }
}