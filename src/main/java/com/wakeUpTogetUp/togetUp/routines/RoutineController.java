package com.wakeUpTogetUp.togetUp.routines;

import com.wakeUpTogetUp.togetUp.common.dto.BaseResponse;
import com.wakeUpTogetUp.togetUp.common.ResponseStatus;
import com.wakeUpTogetUp.togetUp.exception.BaseException;
import com.wakeUpTogetUp.togetUp.routines.dto.request.DeleteRoutineReq;
import com.wakeUpTogetUp.togetUp.routines.dto.request.PatchRoutineReq;
import com.wakeUpTogetUp.togetUp.routines.dto.response.RoutineRes;
import com.wakeUpTogetUp.togetUp.routines.dto.request.PostRoutineReq;
import com.wakeUpTogetUp.togetUp.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/app/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineProvider routineProvider;
    private final JwtService jwtService;

    /**
     * 루틴 1개 가져오기
     * @param routineId
     * @return
     */
    @GetMapping("/{routineId}")
    public BaseResponse<RoutineRes> getRoutine(
            @PathVariable("routineId") Integer routineId
    ) {
        RoutineRes routineRes = routineProvider.getRoutine(routineId);

        return new BaseResponse<>(ResponseStatus.SUCCESS, routineRes);
    }

    /**
     * 루틴 목록 가져오기
     * @param userId
     * @return
     */
    @GetMapping("")
    public BaseResponse<List<RoutineRes>> getRoutinesByUserId(@RequestParam("userId") Integer userId) {
        List<RoutineRes> routineResList = routineProvider.getRoutinesByUserId(userId);

        return new BaseResponse<>(ResponseStatus.SUCCESS, routineResList);
    }

    /**
     * 루틴 생성
     * @param postRoutineReq
     * @return
     */
    @PostMapping("")
    public BaseResponse createRoutine(
            @RequestBody @Valid PostRoutineReq postRoutineReq
    ) {
        Integer userId = postRoutineReq.getUserId();

        if(jwtService.validateByUserId(userId)) {
            Integer createdRoutineId = routineService.createRoutine(userId, postRoutineReq);

            return new BaseResponse(ResponseStatus.SUCCESS, createdRoutineId);
        } else
            throw new BaseException(ResponseStatus.JWT_MISMATCH);
    }

    /**
     * 루틴 수정
     * @param routineId
     * @param patchRoutineReq
     * @return
     */
    @PatchMapping("/{routineId}")
    public BaseResponse<RoutineRes> updateRoutine(
            @PathVariable Integer routineId,
            @RequestBody @Valid PatchRoutineReq patchRoutineReq
    ) {
        Integer userId = patchRoutineReq.getUserId();

        if(jwtService.validateByUserId(userId)) {
            RoutineRes patchRoutineRes = routineService.updateRoutine(routineId, patchRoutineReq);

            return new BaseResponse<>(ResponseStatus.SUCCESS, patchRoutineRes);
        } else
            throw new BaseException(ResponseStatus.JWT_MISMATCH);
    }


    /**
     * 루틴 삭제
     * @param deleteRoutineReq
     * @param routineId
     * @return
     */
    @DeleteMapping("/{routineId}")
    public BaseResponse<Integer> deleteAlarm(
            @RequestBody @Valid DeleteRoutineReq deleteRoutineReq,
            @PathVariable Integer routineId
    ) {
        Integer userId = deleteRoutineReq.getUserId();

        if(jwtService.validateByUserId(userId)) {
            routineService.deleteRoutine(routineId);

            return new BaseResponse<>(ResponseStatus.SUCCESS);
        } else
            throw new BaseException(ResponseStatus.JWT_MISMATCH);
    }

    //test
    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
