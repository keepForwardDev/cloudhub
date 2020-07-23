package com.cloud.hub.controller;

import com.cloud.hub.bean.DictionaryBean;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("rootList")
    public ResponseResult rootList() {
        return dictionaryService.getRootDictionaryList();
    }

    @RequestMapping("list")
    public ResponseResult pageList(CommonPage page, DictionaryBean dictionaryBean) {
        return dictionaryService.pageList(page, dictionaryService.defaultOrderQueryCondition(dictionaryBean));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseResult delete(@RequestBody ArrayList<Long> idList, @RequestParam(name="parentId") Long parentId) {
        dictionaryService.deleteByIds(idList, parentId);
        return ResponseResult.getSuccessInstance();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseResult save(@RequestBody DictionaryBean dictionaryBean, BindingResult result) {
        ResponseResult responseResult = formValidateResult(result);
        return responseResult == null ? dictionaryService.saveDictionary(dictionaryBean) : responseResult;
    }

    @RequestMapping(value= "enabled/{id}")
    public ResponseResult enabled(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean enable) {
        dictionaryService.enableDictionary(id, enable);
        return ResponseResult.getSuccessInstance();
    }
}
