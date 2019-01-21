package io.github.itfinally.jvm.requests;

import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.vo.GcInfoVo;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import io.github.itfinally.jvm.vo.ThreadInfoVo;
import io.github.itfinally.vo.BasicResponseVo;
import io.github.itfinally.vo.SingleResponseVo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface VManagerClient {

  @POST( "/vm/register" )
  Call<SingleResponseVo<Boolean>> vmRegister( @Body JvmRegisterVo jvmRegisterVo );

  @POST( "/vm/saveThreadInfos" )
  Call<BasicResponseVo.Default> saveThreadInfos( @Body ThreadInfoVo threadInfoVo );

  @POST( "/vm/saveMemoryInfos" )
  Call<BasicResponseVo.Default> saveMemoryInfos( @Body List<JvmMemoryEntity> jvmMemoryEntities );

  @POST( "/vm/saveGcInfos" )
  Call<BasicResponseVo.Default> saveGcInfos( @Body GcInfoVo gcInfoVo );
}