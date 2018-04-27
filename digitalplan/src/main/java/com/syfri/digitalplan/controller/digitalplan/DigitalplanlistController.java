package com.syfri.digitalplan.controller.digitalplan;

import com.syfri.baseapi.model.ResultVO;
import com.syfri.baseapi.utils.EConstants;
import com.syfri.digitalplan.model.digitalplan.DigitalplanlistVO;
import com.syfri.digitalplan.service.digitalplan.DigitalplanlistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.syfri.baseapi.controller.BaseController;

import java.util.List;
/*
 * 获取路径Controller
 * by dongbo 2018/03/26
 */
@Api(value = "预案管理",tags = "预案管理API",description = "预案管理")
@RestController
@RequestMapping("digitalplanlist")
public class DigitalplanlistController  extends BaseController<DigitalplanlistVO>{

	private static final Logger logger  = LoggerFactory.getLogger(DigitalplanlistController.class);

	@Autowired
	protected Environment environment;

	@Autowired
	private DigitalplanlistService digitalplanlistService;

	@Override
	public DigitalplanlistService getBaseService() {
		return this.digitalplanlistService;
	}

	@ModelAttribute
	public void Model(Model model){
		if (environment.containsProperty("server.context-path")) {
			model.addAttribute("contextPath", environment.getProperty("server.context-path"));
		}else{
			model.addAttribute("contextPath", "/");
		}
	}

	@GetMapping("")
	public String getDigitalplanlist(Model model, @RequestParam(value="index") String index){
		model.addAttribute("index", index);
		return "digitalplan/digitalplan_list";
	}

	/**
	 * 根据条件获取预案信息
	 */
	@ApiOperation(value="根据条件获取预案信息",notes="列表信息")
	@ApiImplicitParam(name="vo",value="预案信息对象")
	@PostMapping("/findByVO")
	public @ResponseBody ResultVO findByVO(@RequestBody DigitalplanlistVO digitalplanlistVO){
		ResultVO resultVO = ResultVO.build();
		try{
			List<DigitalplanlistVO> result = digitalplanlistService.doFindlist(digitalplanlistVO);
			resultVO.setResult(result);
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 根据id获取预案信息
	 */
	@ApiOperation(value="根据id获取预案信息",notes="列表信息")
	@GetMapping("/doFindById/{pkid}")
	public @ResponseBody ResultVO getDetail(@PathVariable String pkid){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(digitalplanlistService.doFindById(pkid));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/*
	* 预案审批
	* by yuahch 20180426
	*/
	@ApiOperation(value="预案审批",notes="修改")
	@ApiImplicitParam(name="vo",value="预案")
	@PostMapping("/approveByVO")
	public @ResponseBody ResultVO updateByVO(@RequestBody DigitalplanlistVO digitalplanlistVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(digitalplanlistService.doApproveUpdate(digitalplanlistVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}
}
