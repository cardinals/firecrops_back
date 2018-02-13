package com.syfri.userservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.syfri.baseapi.model.ResultVO;
import com.syfri.baseapi.utils.EConstants;
import com.syfri.userservice.model.CodelistDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.syfri.userservice.model.CodelistVO;
import com.syfri.userservice.service.CodelistService;
import com.syfri.baseapi.controller.BaseController;

import java.util.List;

@Api(value = "代码集",tags = "代码集API",description = "代码集")
@Controller
@RequestMapping("codelist")
public class CodelistController  extends BaseController<CodelistVO>{

	@Autowired
	private CodelistService codelistService;

	@Autowired
	protected Environment environment;

	@Override
	public CodelistService getBaseService() {
		return this.codelistService;
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
	public String getCodelist(Model model, @RequestParam(value="index") String index){
		model.addAttribute("index", index);
		return "system/codelist_list";
	}

	/**
	 * 查询代码集
	 */
	@ApiOperation(value="根据代码集查询代码集",notes="列表信息")
	@ApiImplicitParam(name="codelistVO",value="代码集对象")
	@RequiresPermissions("codelist:list")
	@PostMapping("/findByVO")
	public @ResponseBody ResultVO findByVO(@RequestBody CodelistVO codelistVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doFindCodelist(codelistVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 新增代码集
	 */
	@ApiOperation(value="根据代码集新增代码集",notes="新增")
	@ApiImplicitParam(name="codelistVO",value="代码集对象")
	@RequiresPermissions("codelist:add")
	@PostMapping("/insertByVO")
	public @ResponseBody ResultVO insertByVO(@RequestBody CodelistVO codelistVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doInsertCodelist(codelistVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 修改代码集
	 */
	@ApiOperation(value="根据代码集修改代码集",notes="修改")
	@ApiImplicitParam(name="codelistVO",value="代码集对象")
	@RequiresPermissions("codelist:update")
	@PostMapping("/updateByVO")
	public @ResponseBody ResultVO updateByVO(@RequestBody CodelistVO codelistVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doUpdateCodelist(codelistVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 删除代码集
	 */
	@ApiOperation(value="根据主键删除代码集代码集",notes="删除")
	@ApiImplicitParam(name="id",value="代码集主键")
	@RequiresPermissions("codelist:delete")
	@PostMapping("/deleteByIds")
	public @ResponseBody ResultVO deleteByIds(@RequestBody String id){
		JSONObject jsonObject = JSON.parseObject(id);
		JSONArray ids = jsonObject.getJSONArray("ids");
		ResultVO resultVO = ResultVO.build();
		try{
			for(int i=0;i<ids.size();i++){
				String codelistid = (String)ids.get(i);
				codelistService.doDeleteCodelist(codelistid);
			}
			resultVO.setMsg("删除成功");
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	@ApiOperation(value="根据代码集类型查询代码集数量",notes="查询")
	@ApiImplicitParam(name="codetype",value="权限名")
	@GetMapping("/getNum/{codetype}")
	public @ResponseBody ResultVO getNum(@PathVariable String codetype){
		ResultVO resultVO = ResultVO.build();
		try{
			CodelistVO codelistVO = new CodelistVO();
			codelistVO.setCodetype(codetype);
			if(codelistService.doFindByVO(codelistVO) == null){
				resultVO.setResult(0);
			}else{
				resultVO.setResult(1);
			}
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 跳转到代码集详情页
	 */
	@ApiOperation(value="跳转到代码集详情页",notes="页面跳转")
	@GetMapping("/detail/{codeid}")
	public String getDetailPage(Model model, @PathVariable String codeid,  @RequestParam(value="index") String index){
		model.addAttribute("index", index);
		model.addAttribute("codeid", codeid);
		return "system/codelist_detail_list";
	}

	/**
	 * 获取所有的代码集类型
	 */
	@ApiOperation(value="获取所有的代码集类型",notes="列表信息")
	@RequiresPermissions("codelist:list")
	@GetMapping("/detail/doFindById/{codeid}")
	public @ResponseBody ResultVO getDetail(@PathVariable String codeid){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doFindDetail(codeid));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 根据代码集获取资源信息
	 */
	@ApiOperation(value="根据代码值、代码名称查询代码值",notes="列表信息")
	@ApiImplicitParam(name="codelistDetailVO",value="代码集详细对象")
	@RequiresPermissions("codelist:list")
	@PostMapping("/detail/findByVO")
	public @ResponseBody ResultVO findByVO(@RequestBody CodelistDetailVO codelistDetailVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doFindCodelistDetail(codelistDetailVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 新增代码值
	 */
	@ApiOperation(value="根据代码集新增代码值",notes="新增")
	@ApiImplicitParam(name="codelistDetailVO",value="代码值对象")
	@RequiresPermissions("codelist:add")
	@PostMapping("/detail/insertByVO")
	public @ResponseBody ResultVO insertByVO(@RequestBody CodelistDetailVO codelistDetailVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doInsertCodelistDetail(codelistDetailVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 修改代码值
	 */
	@ApiOperation(value="根据代码集修改代码值",notes="修改")
	@ApiImplicitParam(name="codelistVO",value="代码值对象")
	@RequiresPermissions("codelist:update")
	@PostMapping("/detail/updateByVO")
	public @ResponseBody ResultVO updateByVO(@RequestBody CodelistDetailVO codelistDetailVO){
		ResultVO resultVO = ResultVO.build();
		try{
			resultVO.setResult(codelistService.doUpdateCodelistDetail(codelistDetailVO));
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	/**
	 * 删除代码值
	 */
	@ApiOperation(value="根据主键删除代码集代码值",notes="删除")
	@ApiImplicitParam(name="id",value="代码值主键")
	@RequiresPermissions("codelist:delete")
	@PostMapping("/detail/deleteByIds")
	public @ResponseBody ResultVO deleteDetailByIds(@RequestBody String id){
		JSONObject jsonObject = JSON.parseObject(id);
		JSONArray ids = jsonObject.getJSONArray("ids");
		ResultVO resultVO = ResultVO.build();
		try{
			for(int i=0;i<ids.size();i++){
				String pkid = (String)ids.get(i);
				codelistService.doDeleteCodelistDetail(pkid);
			}
			resultVO.setMsg("删除成功");
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

	@ApiOperation(value="根据代码集类型查询代码集数量",notes="查询")
	@ApiImplicitParam(name="codeValue",value="权限名")
	@GetMapping("/detail/getNum/{codevalue}")
	public @ResponseBody ResultVO getDetailNum(@PathVariable String codevalue){
		ResultVO resultVO = ResultVO.build();
		try{
			CodelistDetailVO codelistDetailVO = new CodelistDetailVO();
			codelistDetailVO.setCodeValue(codevalue);
				if(codelistService.doFindCodelistDetail(codelistDetailVO).size() == 0){
				resultVO.setResult(0);
			}else{
				resultVO.setResult(1);
			}
		}catch(Exception e){
			logger.error("{}",e.getMessage());
			resultVO.setCode(EConstants.CODE.FAILURE);
		}
		return resultVO;
	}

}
