package binarfud.challenge7.utlis;

import java.util.HashMap;
import java.util.Map;
// import java.lang.Math;

import org.springframework.data.domain.Page;

public class Utils {
    public static void addPageResponse(Page pageData, Map<String, Object> response) {
        response.put("page", pageData.getTotalPages() == 0 ? 1: pageData.getNumber()+1);
        response.put("dataPerPage", pageData.getSize());
        response.put("totalPage", pageData.getTotalPages() == 0 ? 1: pageData.getTotalPages());
    }

    public static int handlePage(int totalData, int dataPerPage, int selectedPage){
        int maxPage = (int) Math.ceil((double) totalData / dataPerPage);
        if (maxPage < selectedPage && maxPage != 0) return maxPage;

        return selectedPage;
    }
}
