package com.example.tree_solution_proyect.Adaptadores;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Holders.Holder_Chats;

import org.jetbrains.annotations.NotNull;

public class RecyclerChatRemoveListener extends ItemTouchHelper.SimpleCallback {
    private IRecyclerChatRemoveListener iRecyclerChatRemoveListener;


    public RecyclerChatRemoveListener(int dragDirs, int swipeDirs,IRecyclerChatRemoveListener iRecyclerChatRemoveListener) {
        super(dragDirs, swipeDirs);
        this.iRecyclerChatRemoveListener=iRecyclerChatRemoveListener;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {

        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable @org.jetbrains.annotations.Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View view=((Holder_Chats)viewHolder).cardView;
            getDefaultUIUtil().onSelected(view);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View view=((Holder_Chats)viewHolder).cardView;
        getDefaultUIUtil().onDrawOver(c,recyclerView,view,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        View view=((Holder_Chats)viewHolder).cardView;
        getDefaultUIUtil().clearView(view);
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view=((Holder_Chats)viewHolder).cardView;
        getDefaultUIUtil().onDraw(c,recyclerView,view,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        iRecyclerChatRemoveListener.onSwipe(viewHolder,direction, viewHolder.getBindingAdapterPosition());
    }
    public interface IRecyclerChatRemoveListener{
        void onSwipe(RecyclerView.ViewHolder viewHolder,int dirrection,int position);
    }
}
