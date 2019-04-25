package tech.liujin.edgeeffectlib;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import tech.liujin.edge.BaseEdgeEffect;
import tech.liujin.edge.BottomEffect;
import tech.liujin.edge.EdgeEffectUtil;
import tech.liujin.edge.LeftEffect;
import tech.liujin.edge.RightEffect;
import tech.liujin.edge.TopEffect;

/**
 * @author Liujin 2018-10-11:9:56
 */
public class EdgeFrameLayout extends FrameLayout {

      private EdgeEffectUtil mEffectUtil;
      private BaseEdgeEffect mLeftEffect;
      private BaseEdgeEffect mTopEffect;
      private BaseEdgeEffect mRightEffect;
      private BaseEdgeEffect mBottomEffect;

      private float mDownX;
      private float mDownY;

      public EdgeFrameLayout ( @NonNull Context context ) {

            this( context, null, 0 );
      }

      public EdgeFrameLayout (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public EdgeFrameLayout (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
      }

      @Override
      protected void onSizeChanged ( int w, int h, int oldw, int oldh ) {

            super.onSizeChanged( w, h, oldw, oldh );
            mEffectUtil = new EdgeEffectUtil( this );
            mEffectUtil.setSize( w, h );

            mLeftEffect = new LeftEffect( this );
            mLeftEffect.setSize( w, h );

            mTopEffect = new TopEffect( this );
            mTopEffect.setSize( w, h );

            mRightEffect = new RightEffect( this );
            mRightEffect.setSize( w, h );

            mBottomEffect = new BottomEffect( this );
            mBottomEffect.setSize( w, h );
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            super.onDraw( canvas );
            /* 绘制效果 */
            mEffectUtil.onDraw( canvas );
            mLeftEffect.onDraw( canvas );
            mTopEffect.onDraw( canvas );
            mRightEffect.onDraw( canvas );
            mBottomEffect.onDraw( canvas );
      }

      @Override
      public boolean onTouchEvent ( MotionEvent event ) {

            switch( event.getAction() ) {

                  case MotionEvent.ACTION_DOWN:
                        mDownX = event.getX();
                        mDownY = event.getY();
                        break;
                  case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - mDownX;
                        float dy = event.getY() - mDownY;

                        /* 在移动的时候,触发pull */
                        float deltaDistanceX = dx / ( getWidth() );
                        float deltaDistanceY = dy / ( getHeight() );

                        if( Math.abs( dx ) >= Math.abs( dy ) ) {

                              if( deltaDistanceX > 0 ) {
                                    //mEffectUtil.pullLeft( deltaDistanceX );
                                    mLeftEffect.pull( deltaDistanceX );
                              } else {
                                    //mEffectUtil.pullRight( deltaDistanceX );
                                    mRightEffect.pull( deltaDistanceX );
                              }
                        } else {
                              if( deltaDistanceY > 0 ) {

                                    //mEffectUtil.pullTop( deltaDistanceY );
                                    mTopEffect.pull( deltaDistanceY );
                              } else {
                                    //mEffectUtil.pullBottom( deltaDistanceY );
                                    mBottomEffect.pull( deltaDistanceY );
                              }
                        }

                        break;
                  default:
                        /* 及时释放回弹 */
                        //mEffectUtil.releaseAllEdge();
                        mLeftEffect.release();
                        mTopEffect.release();
                        mRightEffect.release();
                        mBottomEffect.release();
                        break;
            }

            super.onTouchEvent( event );
            return true;
      }
}
