package cn.staitech.anno.controller;

import cn.staitech.anno.domain.question.in.ConfirmSelectionIn;
import cn.staitech.anno.domain.question.in.CreateBySlideIn;
import cn.staitech.anno.domain.question.in.CreateQuestionIn;
import cn.staitech.anno.domain.question.in.GetQuestionListIn;
import cn.staitech.anno.domain.question.in.GetQuestionsIn;
import cn.staitech.anno.domain.question.in.SettingCompletedIn;
import cn.staitech.anno.domain.question.out.GetProjectBoxOut;
import cn.staitech.anno.domain.question.out.GetQuestionListOut;
import cn.staitech.anno.domain.question.out.GetQuestionsOut;
import cn.staitech.anno.service.IQuestionBankService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/9/26 9:58
 * @desc 题库
 */
@Slf4j
@Api(tags = "考核标注-题库")
@RestController
@RequestMapping("/questionBank")
public class QuestionBankController {
    @Autowired
    private IQuestionBankService iQuestionBankService;

    @ApiOperation(value = "生成考题-根据项目")
    @PostMapping("/createQuestion")
    public R createQuestion(@Validated @RequestBody CreateQuestionIn req) {

        return iQuestionBankService.createQuestion(req);

    }

    @ApiOperation(value = "生成考题-根据切片")
    @PostMapping("/createBySlide")
    public R createBySlide(@Validated @RequestBody CreateBySlideIn req) {

        return iQuestionBankService.createBySlide(req);
    }

    @ApiOperation(value = "分页查询考题列表")
    @PostMapping("/getQuestionList")
    public R<PageResponse<GetQuestionListOut>> getQuestionList(@Validated @RequestBody GetQuestionListIn req) {

        PageResponse<GetQuestionListOut> resp = iQuestionBankService.getQuestionList(req);
        return R.ok(resp);
    }

    @ApiOperation(value = "考核选片-项目下拉框")
    @GetMapping("/getProjectBox")
    public R<List<GetProjectBoxOut>> getProjectBox() {
        return iQuestionBankService.getProjectBox();

    }

    @ApiOperation(value = "考核选片-不分页查询考题列表")
    @PostMapping("/getQuestionListExt")

    public R<List<GetQuestionListOut>> getQuestionListExt(@Validated @RequestBody GetQuestionsIn req) {

        List<GetQuestionListOut> resp = iQuestionBankService.getQuestionListExt(req);
        return R.ok(resp);
    }

    @ApiOperation(value = "考核设置-项目下考题列表")
    @GetMapping("/getQuestionByProject")
    public R<GetQuestionsOut> getQuestionByProject(@RequestParam(value = "projectId", required = false)
                                                            @NotNull(message = "项目id不能为空！") @ApiParam(name = "projectId", value = "项目id") Long projectId) {

        GetQuestionsOut resp = iQuestionBankService.getQuestionByProject(projectId);
        return R.ok(resp);
    }

    @ApiOperation(value = "考核选片-确认选择")
    @PostMapping("/confirmSelection")
    public R confirmSelection(@Validated @RequestBody ConfirmSelectionIn req) {

        return iQuestionBankService.confirmSelection(req);

    }

    @ApiOperation(value = "考核设置-设置完成")
    @PostMapping("/settingCompleted")
    public R settingCompleted(@Validated @RequestBody SettingCompletedIn req) {

        return iQuestionBankService.settingCompleted(req);

    }

    @ApiOperation(value = "考核设置-删除")
    @PostMapping("/removeQuestion")
    public R removeQuestion(@Validated @RequestBody SettingCompletedIn req) {

        return iQuestionBankService.removeQuestion(req);

    }
}
