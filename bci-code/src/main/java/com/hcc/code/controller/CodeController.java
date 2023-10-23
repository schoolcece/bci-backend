package com.hcc.code.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hcc.code.entity.CodeEntity;
import com.hcc.common.exception.RRException;
import com.hcc.code.service.CodeService;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.utils.R;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/code")
public class CodeController {
    private CodeService codeService;

    private StringRedisTemplate redisTemplate;
    @Autowired
    public CodeController(CodeService codeService, StringRedisTemplate redisTemplate){
        this.codeService = codeService;
        this.redisTemplate = redisTemplate;
    }

    /**
     *  上传接口 post请求
     *  功能： 上传代码
     *  参数： 代码范式（int）, 上传文件（file）
     *  返回结果： 上传代码存入数据库的id
     */
//    @CacheEvict(value = "code",key = "'list'")
    @PreAuthorize("@JER.hasJoin()")
    @RefreshScope
    @PostMapping("/upload")
    public R upload(@RequestParam int type, @RequestParam MultipartFile file){
        codeService.upload(type, file);
        return R.ok();
    }

    /**
     * 获取用户已上传代码信息 get请求
     * @param type
     * @param current
     * @return
     */
//    @Cacheable(value = {"code"},key = "#root.methodName")
    @PreAuthorize("@JER.hasJoin()")
    @GetMapping("/list")
    public R list(@RequestParam int competition,
                  @RequestParam int type,
                  @RequestParam(value = "current", defaultValue = "1") Integer current){
        UserInfo userInfo = UserUtils.getUser();
        String taskingKey = "teamId" + userInfo.getTeamId()[competition] + "type" + type + "on";
        Page<CodeEntity> page = codeService.query()
                .eq("user_id", userInfo.getUserId()).eq("type", type).orderByDesc("create_time").page(new Page<>(current, 5));
        return R.ok().put("data", page.getRecords()).put("total", page.getTotal()).put("running", redisTemplate.hasKey(taskingKey));
    }

//    @PreAuthorize("@JER.hasJoin()")
//    @RequestMapping("/uploadOnline")
//    public R uploadOnline(@RequestParam int type, @RequestParam MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new RRException(BizCodeEnume.FILE_EMPTY.getCode(), BizCodeEnume.FILE_EMPTY.getMsg());
//        }
//        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        // todo 保存文件和插入数据库信息可并行
//        // 保存代码文件
//        String uuid = UUID.randomUUID().toString().replace("-","");
//        String savePath = path + "/"+ uuid;
//        try{
//            File filePath = new File(savePath, file.getOriginalFilename());
//            filePath.mkdirs();
//            file.transferTo(filePath);
//        }catch (Exception e){
//            throw new RRException(BizCodeEnume.FILE_SAVE_FAILED.getCode(), BizCodeEnume.FILE_SAVE_FAILED.getMsg());
//        }
//
//        // 插入数据库信息
//        CodeEntity code = new CodeEntity();
//        code.setType(type+10);
//        code.setUrl(savePath+"/"+file.getOriginalFilename());
//        code.setUserId(userInfo.getUserId());
//        code.setFileName(file.getOriginalFilename());
//        code.setStatus(0);
//        code.setCreateTime(new Timestamp(new Date().getTime()));
//        try {
//            codeService.save(code);
//        }catch (Exception e){
//            throw new RRException(BizCodeEnume.FILE_SAVE_FAILED.getCode(), BizCodeEnume.FILE_SAVE_FAILED.getMsg());
//        }
//        return R.ok().put("id",code.getCodeId());
//    }
//
//    @RequestMapping("/listOnline")
//    public R listOline(@RequestParam int type){
//        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        QueryWrapper<CodeEntity> queryWrapper = new QueryWrapper<CodeEntity>()
//                .eq("type", type + 10)
//                .eq("user_id", userInfo.getUserId());
//        List<CodeEntity> codeEntities = codeMapper.selectList(queryWrapper);
//        return R.ok().put("data",codeEntities);
//    }

    /**
     * 代码删除接口 post请求
     * @param codeId
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestParam int codeId){
        UserInfo userInfo = UserUtils.getUser();
        CodeEntity codeEntity = codeService.getById(codeId);
        if (codeEntity == null){
            throw new RRException(BizCodeEnum.CODE_NOT_EXIST.getCode(), BizCodeEnum.CODE_NOT_EXIST.getMsg());
        }
        if (!codeEntity.getUserId().equals(userInfo.getUserId())){
            throw new RRException(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
        }
        codeService.deleteById(codeId);
        return R.ok();
    }


    //内部调用方法
    @PostMapping("/updateStatusReturnCode")
    public CodeEntity updateStatusReturnCode(@RequestParam int codeId, @RequestParam int status){
        CodeEntity codeEntity = new CodeEntity();
        codeEntity.setCodeId(codeId);
        codeEntity.setStatus(status);
        codeService.updateById(codeEntity);
        CodeEntity code = codeService.getById(codeId);
        return code;
    }

    //内部调用方法
    @Transactional
    @PostMapping("/updateCode")
    public R updateCode(@RequestBody CodeEntity codeEntity){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String info = (String) authentication.getPrincipal();
//        if (!info.equals("innerVisit")){
//            throw new RRException(BizCodeEnum.INNER_REJECT.getCode(), BizCodeEnum.INNER_REJECT.getMsg());
//        }
        codeService.updateById(codeEntity);
        return R.ok();
    };

    @GetMapping("/getCodeById")
    public CodeEntity getCodeById(@RequestParam int codeId){
        return codeService.getById(codeId);
    }

    /**
     * 退队或注销队伍调用删除历史提交调用接口 post请求
     */
    @PostMapping("/deleteByUserId")
    public R deleteByUserId(@RequestParam int userId){
        codeService.deleteByUserId(userId);
        return R.ok();
    }
}
