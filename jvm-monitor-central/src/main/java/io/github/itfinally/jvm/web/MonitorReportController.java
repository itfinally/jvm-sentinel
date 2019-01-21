package io.github.itfinally.jvm.web;

import io.github.itfinally.http.HttpCode;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.repository.MonitorDataSavingService;
import io.github.itfinally.jvm.vo.GcInfoVo;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import io.github.itfinally.jvm.vo.ThreadInfoVo;
import io.github.itfinally.vo.BasicResponseVo;
import io.github.itfinally.vo.SingleResponseVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ResponseBody
@RestController
@RequestMapping( "/vm" )
public class MonitorReportController {

  @Resource
  private MonitorDataSavingService monitorDataSavingService;

  @PostMapping( "/register" )
  public DeferredResult<SingleResponseVo<Boolean>> register( @RequestBody JvmRegisterVo jvmRegisterVo ) {
    DeferredResult<SingleResponseVo<Boolean>> response = buildDeferredResult( new SingleResponseVo<>() );

    monitorDataSavingService.register( jvmRegisterVo, response );

    return response;
  }

  @PostMapping( "/saveThreadInfos" )
  public BasicResponseVo.Default saveThreadInfos( @RequestBody ThreadInfoVo threadInfoVo ) {
    monitorDataSavingService.saveThreadInfos( threadInfoVo );

    return new BasicResponseVo.Default( HttpCode.OK );
  }

  @PostMapping( "/saveMemoryInfos" )
  public BasicResponseVo.Default saveMemoryInfos( @RequestBody List<JvmMemoryEntity> jvmMemoryEntities ) {
    monitorDataSavingService.saveMemoryInfos( jvmMemoryEntities );

    return new BasicResponseVo.Default( HttpCode.OK );
  }

  @PostMapping( "/saveGcInfos" )
  public BasicResponseVo.Default saveGcInfos( @RequestBody GcInfoVo gcInfoVo ) {
    monitorDataSavingService.saveGcInfos( gcInfoVo );

    return new BasicResponseVo.Default( HttpCode.OK );
  }

  private <T> DeferredResult<T> buildDeferredResult( BasicResponseVo<?> responseVo ) {
    return new DeferredResult<>( TimeUnit.SECONDS.toMillis( 4000 ), responseVo
        .setStatus( HttpCode.TIMEOUT.getCode() ).setMessage( HttpCode.TIMEOUT.getMessage() ) );
  }
}
