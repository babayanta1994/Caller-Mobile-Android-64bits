package ru.true_ip.trueip.app.messages_screen.comments.comment_details;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityCommentDetailsBinding;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentDetailsActivity extends BaseActivity<CommentDetailsContract, CommentDetailsPresenter, ActivityCommentDetailsBinding> implements CommentDetailsContract {

    private RecyclerView.OnScrollListener onScrollChangeListener;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommentDetailsActivity.class);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            presenter.setContext(getApplicationContext());
            presenter.setExtras(getIntent().getExtras());
            initRecyclerView();
            presenter.showMessages();
            binding.homeButton.setOnClickListener(view -> homeButtonClicked());
        }
        processHomeButtonClick = true;
    }

    @Override
    public ActivityCommentDetailsBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_comment_details, null, false);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.messagesList.setLayoutManager(linearLayoutManager);
        binding.messagesList.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            RecyclerView.Adapter adapter = binding.messagesList.getAdapter();
            binding.messagesList.scrollToPosition(adapter.getItemCount() - 1);
            /*if (getLastVisibleMessagePosition() == adapter.getItemCount() - 1) {
                scrollDialog(adapter.getItemCount() - 1);
            }*/
        });

        if (onScrollChangeListener == null) {
            onScrollChangeListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(-1)) {
                        presenter.onScrolledToTop();
                    }
                }
            };

            binding.messagesList.addOnScrollListener(onScrollChangeListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.cancelTimer();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showPreloader() { }
    @Override
    public void hidePreloader() { }
    @Override
    public BaseRouter getRouter() { return router; }
    @Override
    public CommentDetailsContract getContract() { return this; }
    @Override
    public CommentDetailsPresenter createPresenter() { return new CommentDetailsPresenter(); }
    @Override
    public BaseRouter createRouter() { return new CommentDetailsRouter(this); }

    @Override
    public void scrollDialog(int position) {
        if (binding != null) {
            RecyclerView.LayoutManager layoutManager = binding.messagesList.getLayoutManager();
            layoutManager.scrollToPosition(position);
        }
    }

    @Override
    public int getLastVisibleMessagePosition() {
        if (binding != null) {
            RecyclerView.LayoutManager layoutManager = binding.messagesList.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                return linearLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        }

        return -1;
    }
}
