package cn.zndroid.bili.adapter.helper;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public abstract class AbsRecyclerViewAdapter extends RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {

    private Context context;
    protected RecyclerView recyclerView;
    private List<RecyclerView.OnScrollListener> mlisteners=new ArrayList<>();

    public AbsRecyclerViewAdapter(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                for (RecyclerView.OnScrollListener listener:mlisteners){
                    listener.onScrollStateChanged(recyclerView,newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                for (RecyclerView.OnScrollListener listener:mlisteners){
                    listener.onScrolled(recyclerView,dx,dy);
                }
            }
        });
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        mlisteners.add(listener);
    }


    public interface OnItemClickListener{
        void onItemClick(int position,ClickableViewHolder holder);
    }

    private OnItemClickListener itemClickListener;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position,ClickableViewHolder holder);
    }

    private OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener=listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }
    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null){
                    itemClickListener.onItemClick(position,holder);
                }
            }
        });
        holder.getParentView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemLongClickListener!=null){
                    itemLongClickListener.onItemLongClick(position,holder);
                    return true;
                }
                return false;
            }
        });
    }

    public void bindContext(Context context){
        this.context=context;
    }
    public Context getContext(){return this.context;}

    public static class ClickableViewHolder extends RecyclerView.ViewHolder{

        private View parentView;
        public ClickableViewHolder(View itemView) {
            super(itemView);
            parentView=itemView;
        }
        View getParentView(){
            return parentView;
        }
        @SuppressWarnings("unchecked")
        public <T extends View> T $(@IdRes int id) {
            return (T) parentView.findViewById(id);
        }
    }
}
