package com.syfri.digitalplan.controller.planobject;

import com.syfri.baseapi.model.ResultVO;
import com.syfri.baseapi.utils.EConstants;
import com.syfri.digitalplan.utils.Base64ImageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.syfri.digitalplan.model.planobject.OtherobjectsVO;
import com.syfri.digitalplan.service.planobject.OtherobjectsService;
import com.syfri.baseapi.controller.BaseController;

@RestController
@RequestMapping("otherobjects")
public class OtherobjectsController  extends BaseController<OtherobjectsVO>{

	@Autowired
	private OtherobjectsService otherobjectsService;

	@Override
	public OtherobjectsService getBaseService() {
		return this.otherobjectsService;
	}

	@ApiOperation(value="根据id获取其他对象信息",notes="列表信息")
	@GetMapping("/doFindById/{ID}")
	public @ResponseBody
	ResultVO getDetail(@PathVariable String ID){
		ResultVO resultVO = ResultVO.build();
		try{
			OtherobjectsVO otherobjectsVO = otherobjectsService.doFindById(ID);
			//将二进制转为Base64格式字符串
			String photo64 = Base64ImageUtil.byteArr2String(otherobjectsVO.getPhoto());
			otherobjectsVO.setPhoto64(photo64);
			resultVO.setResult(otherobjectsVO);
		}catch (Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

}
