package net.nonworkspace.demo.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.model.DummyDataVO;
import net.nonworkspace.demo.service.DummyDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
public class DummyDataController {

    private final DummyDataService dummyDataService;

    @GetMapping("/page/{pageNo}")
    public List<DummyDataVO> getPage(@PathVariable int pageNo) throws Exception {
        try {
            return dummyDataService.getDummyDataPage(pageNo, 10);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
