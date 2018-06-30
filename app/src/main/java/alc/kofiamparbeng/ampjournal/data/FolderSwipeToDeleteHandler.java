package alc.kofiamparbeng.ampjournal.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class FolderSwipeToDeleteHandler extends ItemTouchHelper.SimpleCallback {
private final Context context;
private final Drawable background;
private final Drawable xMark;
private final int xMargin;

public FolderSwipeToDeleteHandler(Context context) {
    super(0, ItemTouchHelper.LEFT);
    this.context = context;
    background = new ColorDrawable(Color.RED);
    xMark = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_close_clear_cancel);
    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    xMargin = 24;//context.resources.getDimension(R.dimen.ic_clear_margin).toInt();
}


@Override
public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
    return false;
}

@Override
public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    FolderAdapter.ViewHolder listItemViewHolder =  (FolderAdapter.ViewHolder)viewHolder;
    if(listItemViewHolder != null){
        listItemViewHolder.onDelete();
    }
}

@Override
public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    if(viewHolder != null){
        if (viewHolder.getAdapterPosition() < 0) return;

        View view = viewHolder.itemView;
        background.setBounds(view.getRight() + (int)dX, view.getTop(), view.getRight(), view.getBottom());
        background.draw(c);

        int xt = view.getTop() + (view.getBottom() - view.getTop() - xMark.getIntrinsicHeight()) / 2;
        xMark.setBounds(
                view.getRight() - xMargin - xMark.getIntrinsicWidth(),
                xt,
                view.getRight() - xMargin,
                xt + xMark.getIntrinsicHeight()
        );
        xMark.draw(c);
    }
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
}
}
