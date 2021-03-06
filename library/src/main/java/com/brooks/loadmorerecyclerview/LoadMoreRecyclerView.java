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

import com.tt.whorlviewlibrary.WhorlView;

public class LoadMoreRecyclerView extends RecyclerView {

    /**
     * 加载类型-滑到底部时自动加载更多
     */
    public static final int LOAD_TYPE_AUTO = 1;

    /**
     * 加载类型-滑到底部时手动点击加载更多
     */
    public static final int LOAD_TYPE_MANUAL = 2;

    /**
     * item类型-询问加载更多的LoadMoreView
     */
    private final static int ITEM_VIEW_TYPE_FOOTER = 2;

    /**
     * item类型-普通ListViewItem
     */
    private final static int ITEM_VIEW_TYPE_LIST = 3;

    /**
     * 是否允许加载更多
     */
    private boolean mEnableAutoLoadMore;

    /**
     * 加载类型-默认滑动到底部自动加载更多
     */
    private int mLoadType = LOAD_TYPE_AUTO;

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

    /**
     * 是否使用图片动画作为进度条
     */
    private boolean mPictureAnim;

    /**
     * 加载更多提示文本
     */
    private TextView mHintText;

    /**
     * 加载更多图案
     */
    private WhorlView mWhorlView;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
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
                if (mEnableAutoLoadMore && !mIsLoadingMore && dy >= 0 && mListener != null) {
                    int lastVisPos = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisPos + 1 == mAutoLoadAdapter.getItemCount() && mLoadType == LOAD_TYPE_AUTO) {
                        mIsLoadingMore = true;
                        mLastPosition = lastVisPos;
                        mListener.onLoadMore();
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
        mEnableAutoLoadMore = autoLoadMoreEnable;
    }

    /**
     * 设置将加载方式，是自动加载还是手动加载
     */
    public void setLoadType(int loadType) {
        mLoadType = loadType;
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
     * 设置加载更多的监听
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 通知更多数据已经加载完毕
     * @param hasMore 是否还有更多
     */
    public void notifyMoreFinish(boolean hasMore) {
        setLoadMoreEnable(hasMore);
        if (mLoadType == LOAD_TYPE_AUTO) {
            getAdapter().notifyItemRemoved(mLastPosition);
        } else {
            getAdapter().notifyItemChanged(mLastPosition + 1);
            // 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，而不是用notifyDataSetChanged来刷新列表
            // getAdapter().notifyDataSetChanged();
        }
        mIsLoadingMore = false;
    }

    /**
     * 设置是否使用图片动画
     * @param usePictureAnim
     */
    public void setUsePictureAnim(boolean usePictureAnim) {
        this.mPictureAnim = usePictureAnim;
    }

    /**
     * 处理回调，使loadmore显示和whorlView隐藏
     */
    public void handleCallback() {
        mWhorlView.setVisibility(GONE);
        mHintText.setVisibility(VISIBLE);
    }

    /**
     * 加载更多监听器
     */
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     * 自动加载的适配器
     */
    public class AutoLoadAdapter extends Adapter<ViewHolder> {

        private Adapter mInternalAdapter;

        public AutoLoadAdapter(Adapter adapter) {
            mInternalAdapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_FOOTER) {
                if (mPictureAnim) {
                    return new FooterViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.loadmore_recycler_footer_usepic, parent, false));
                } else if (mLoadType == LOAD_TYPE_AUTO)
                    return new FooterViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.loadmore_recycler_footer_auto, parent, false));
                else {
                    return new FooterViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.loadmore_recycler_footer_manual, parent, false));
                }
            } else {
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public int getItemCount() {
            int count = mInternalAdapter.getItemCount();
            return mEnableAutoLoadMore ? ++count : count;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != ITEM_VIEW_TYPE_FOOTER) {
                mInternalAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            int footerPosition = getItemCount() - 1;
            if (footerPosition == position && mEnableAutoLoadMore) {
                return ITEM_VIEW_TYPE_FOOTER;
            } else {
                return ITEM_VIEW_TYPE_LIST;
            }
        }

        /**
         * 自动加载更多布局holder
         */
        class FooterViewHolder extends ViewHolder {

            private FooterViewHolder(View itemView) {
                super(itemView);
                if (mPictureAnim) {
                    // 使用图片动画
                    ImageView image = (ImageView) itemView.findViewById(R.id.loadmore_recycler_footer_image);
                    image.setBackgroundResource(R.drawable.loadmore_recycler_footer_anim_list);
                    AnimationDrawable animationDrawable = (AnimationDrawable) image.getBackground();
                    animationDrawable.start();
                } else {
                    if (mLoadType == LOAD_TYPE_AUTO) {
                        // 不使用图片动画自动加载更多
                        WhorlView whorlView = (WhorlView) itemView.findViewById(R.id.loadmore_recycler_footer_whorl);
                        whorlView.start();
                    } else {
                        // 不使用图片动画手动加载更多
                        mWhorlView = (WhorlView) itemView.findViewById(R.id.loadmore_recycler_footer_whorl);
                        mWhorlView.start();
                        mHintText = (TextView) itemView.findViewById(R.id.loadmore_recycler_footer_hint);
                        mHintText.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!mIsLoadingMore) {
                                    mIsLoadingMore = true;
                                    mWhorlView.setVisibility(VISIBLE);
                                    mHintText.setVisibility(GONE);
                                    mListener.onLoadMore();
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
