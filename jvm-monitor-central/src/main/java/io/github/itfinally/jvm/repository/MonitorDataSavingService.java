package io.github.itfinally.jvm.repository;

import io.github.itfinally.http.HttpCode;
import io.github.itfinally.jvm.entity.JvmArgumentsEntity;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.entity.JvmThreadInfoEntity;
import io.github.itfinally.jvm.entity.JvmThreadStackEntity;
import io.github.itfinally.jvm.repository.mapper.*;
import io.github.itfinally.jvm.vo.GcInfoVo;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import io.github.itfinally.jvm.vo.ThreadInfoVo;
import io.github.itfinally.vo.SingleResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MonitorDataSavingService {
  private static final Logger logger = LoggerFactory.getLogger( MonitorDataSavingService.class );

  @Resource
  private TransactionTemplate transactionTemplate;

  @Resource
  private JvmStatusMapper jvmStatusMapper;

  @Resource
  private JvmArgumentsMapper jvmArgumentsMapper;

  @Resource
  private JvmThreadMapper jvmThreadMapper;

  @Resource
  private JvmThreadInfoMapper jvmThreadInfoMapper;

  @Resource
  private JvmThreadStackMapper jvmThreadStackMapper;

  @Resource
  private JvmMemoryMapper jvmMemoryMapper;

  @Resource
  private JvmGcMapper jvmGcMapper;

  @Async( "taskElasticExecutorPool" )
  public void register( JvmRegisterVo jvmRegisterVo, DeferredResult<SingleResponseVo<Boolean>> response ) {
    transactionTemplate.execute( new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult( TransactionStatus status ) {
        try {
          jvmStatusMapper.save( jvmRegisterVo.getJvmStatusEntity() );

          List<JvmArgumentsEntity> jvmArgumentsEntities = jvmRegisterVo.getJvmArgumentsEntities();
          if ( jvmArgumentsEntities != null && !jvmArgumentsEntities.isEmpty() ) {
            jvmArgumentsMapper.saveAll( jvmRegisterVo.getJvmArgumentsEntities() );
          }

          response.setResult( new SingleResponseVo<>( HttpCode.OK, true ) );

        } catch ( RuntimeException e ) {
          status.setRollbackOnly();

          logger.error( "Failure to saving thread data to database", e );

          response.setErrorResult( new SingleResponseVo<>( HttpCode.SERVER_ERROR, false ) );
        }
      }
    } );
  }

  @Async( "taskElasticExecutorPool" )
  public void saveThreadInfos( ThreadInfoVo threadInfoVo ) {
    transactionTemplate.execute( new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult( TransactionStatus status ) {
        try {
          jvmThreadMapper.save( threadInfoVo.getJvmThreadEntity() );

          List<JvmThreadInfoEntity> jvmThreadInfoEntities = threadInfoVo.getJvmThreadInfoEntities();
          if ( jvmThreadInfoEntities != null && !jvmThreadInfoEntities.isEmpty() ) {
            jvmThreadInfoMapper.saveAll( jvmThreadInfoEntities );
          }

          List<JvmThreadStackEntity> jvmThreadStackEntities = threadInfoVo.getJvmThreadStackEntities();
          if ( jvmThreadStackEntities != null && !jvmThreadStackEntities.isEmpty() ) {
            jvmThreadStackMapper.saveAll( jvmThreadStackEntities );
          }

        } catch ( RuntimeException e ) {
          status.setRollbackOnly();

          logger.error( "Failure to saving memory data to database", e );
        }
      }
    } );
  }

  @Async( "taskElasticExecutorPool" )
  public void saveMemoryInfos( List<JvmMemoryEntity> jvmMemoryEntities ) {
    transactionTemplate.execute( new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult( TransactionStatus status ) {
        try {
          jvmMemoryMapper.saveAll( jvmMemoryEntities );

        } catch ( RuntimeException e ) {
          status.setRollbackOnly();

          logger.error( "Failure to saving memory data to database", e );
        }
      }
    } );
  }

  @Async( "taskElasticExecutorPool" )
  public void saveGcInfos( GcInfoVo gcInfoVo ) {
    transactionTemplate.execute( new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult( TransactionStatus status ) {
        try {
          jvmGcMapper.save( gcInfoVo.getJvmGcEntity() );

          List<JvmMemoryEntity> beforeGcMemoryEntities = gcInfoVo.getBeforeGcMemoryEntities();
          if ( beforeGcMemoryEntities != null && !beforeGcMemoryEntities.isEmpty() ) {
            jvmMemoryMapper.saveAll( beforeGcMemoryEntities );
          }

          List<JvmMemoryEntity> afterGcMemoryEntities = gcInfoVo.getAfterGcMemoryEntities();
          if ( afterGcMemoryEntities != null && !afterGcMemoryEntities.isEmpty() ) {
            jvmMemoryMapper.saveAll( afterGcMemoryEntities );
          }

        } catch ( RuntimeException e ) {
          status.setRollbackOnly();

          logger.error( "Failure to saving gc data to database", e );
        }
      }
    } );
  }
}
