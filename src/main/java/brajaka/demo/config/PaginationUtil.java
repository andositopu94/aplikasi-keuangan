package brajaka.demo.config;

import org.springframework.data.domain.Sort;

public class PaginationUtil {
    public static Sort parseSort(String[] sortParams){
        Sort sort = Sort.unsorted();
        for (String param : sortParams) {
            String[] parts = param.split("," , 2);
            if (parts.length == 2) {
                sort = sort.and(Sort.by(parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, parts[0]));
            }
        }
        return sort;
    }
}
