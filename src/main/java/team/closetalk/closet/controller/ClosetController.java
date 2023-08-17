package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet")
public class ClosetController {
    private final ClosetService closetService;

    // 옷장 목록
    @PostMapping
    public List<ClosetDto> closetList() {
        return closetService.findCloset();
    }

    // 옷장 생성
    @PostMapping("/create")
    public void closetCreate(@RequestParam(name = "closetName", required = false)
                                 String closetName,
                             @RequestParam(name = "isHidden", required = false,
                                     defaultValue = "false") Boolean isHidden) {
        closetService.addCloset(closetName, isHidden);
    }

    // 해당 옷장 아이템 목록 조회
    @PostMapping("/{closetId}")
    public List<ClosetItemDto> readItems(@PathVariable("closetId") Long closetId,
                                         @RequestParam(name = "category", required = false) String category) {
        // 카테고리 입력이 없을 시 전체 목록
        if (category == null) return closetService.readByCloset(closetId);
        // 있으면 카테고리 별 목록
        else return closetService.readByCategory(closetId, category);
    }
}
