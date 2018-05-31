package com.brooks.loadmorerecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 滑动到底部自动加载更多
 */
public class McLoadMoreRecyclerView extends RecyclerView {

    /**
     * 是否允许加载更多
     */
    private boolean mHasMore;

    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore;

    /**
     * 自定义实现了头部和底部加载更多的adapter
     */
    private AutoLoadAdapter mAutoLoadAdapter;

    /**
     * 上一次加载更多的位置
     */
    private int mLastPosition;

    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private LoadMoreListener mListener;

    public McLoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public McLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public McLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHasMore && !mIsLoadingMore && dy >= 0) {
                    int lastVisPos = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisPos + 1 == mAutoLoadAdapter.getItemCount()) {
                        mIsLoadingMore = true;
                        mLastPosition = lastVisPos;
                        if (mListener != null) {
                            mListener.onLoadMore(mLastPosition - 1);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置是否支持自动加载更多
     * @param autoLoadMoreEnable
     */
    public void setLoadMoreEnable(boolean autoLoadMoreEnable) {
        mHasMore = autoLoadMoreEnable;
    }

    /**
     * 设置正在加载更多
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
            adapter.registerAdapterDataObserver(new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    mAutoLoadAdapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    mAutoLoadAdapter.notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    mAutoLoadAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mAutoLoadAdapter.notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    mAutoLoadAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    mAutoLoadAdapter.notifyItemRangeChanged(fromPosition, toPosition, itemCount);
                }
            });
        }
        super.swapAdapter(mAutoLoadAdapter, true);
    }

    /**
     * 通知更多数据已经加载完毕
     * @param hasMore 是否还有更多
     */
    public void notifyMoreFinish(boolean hasMore) {
        setLoadMoreEnable(hasMore);
        getAdapter().notifyItemRemoved(mLastPosition);
        mIsLoadingMore = false;
    }

    /**
     * 设置加载更多的监听
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 加载更多监听器
     */
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore(int lastPosition);
    }

    /**
     * 自动加载的适配器
     */
    public class AutoLoadAdapter extends Adapter<ViewHolder> {

        /**
         * item类型-询问加载更多的LoadMoreView
         */
        private final static int ITEM_VIEW_TYPE_FOOTER = 2;

        /**
         * item类型-普通ListViewItem
         */
        private final static int ITEM_VIEW_TYPE_LIST = 3;

        private Adapter mInternalAdapter;

        public AutoLoadAdapter(Adapter adapter) {
            mInternalAdapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_FOOTER) {
                return new FooterViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mc_loadmore_recycler_footer, parent, false));
            } else {
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public int getItemCount() {
            int count = mInternalAdapter.getItemCount();
            return mHasMore ? ++count : ++count;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != ITEM_VIEW_TYPE_FOOTER) {
                mInternalAdapter.onBindViewHolder(holder, position);
            } else {
                FooterViewHolder mHolder = (FooterViewHolder) holder;
                mHolder.showHasNoMoreHint();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == (getItemCount() - 1)) {
                return ITEM_VIEW_TYPE_FOOTER;
            } else {
                return ITEM_VIEW_TYPE_LIST;
            }
        }

        /**
         * 自动加载更多布局holder
         */
        class FooterViewHolder extends ViewHolder {

            private ImageView mLoadingImage;
            private TextView mHintText;

            private FooterViewHolder(View itemView) {
                super(itemView);
                mHintText = (TextView) itemView.findViewById(R.id.loadmore_recycler_footer_text);
                mLoadingImage = (ImageView) itemView.findViewById(R.id.loadmore_recycler_footer_image);
                showHasNoMoreHint();
            }

            private void showHasNoMoreHint() {
                if (mHasMore) {
                    mLoadingImage.setVisibility(VISIBLE);
                    AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getBackground();
                    if (animationDrawable != null && !animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                    mHintText.setVisibility(GONE);
                } else {
                    AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getBackground();
                    if (animationDrawable != null && animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    mLoadingImage.setVisibility(GONE);
                    mHintText.setVisibility(VISIBLE);
                }
            }
        }
    }
}
