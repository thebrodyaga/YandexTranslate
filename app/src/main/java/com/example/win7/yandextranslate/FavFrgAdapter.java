package com.example.win7.yandextranslate;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win7.yandextranslate.DB.DBHelper;
import com.example.win7.yandextranslate.FavoritesFragment.OnListFragmentInteractionListener;
import com.example.win7.yandextranslate.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavFrgAdapter extends RecyclerView.Adapter<FavFrgAdapter.ViewHolder> {

    private List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FavFrgAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.inTextList.setText(mValues.get(position).inText);
        holder.outTextList.setText(mValues.get(position).outText);
        holder.codeList.setText(mValues.get(position).code);
        if ((mValues.get(position).isFavorite) == 0)
            holder.isFavoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
        else holder.isFavoriteImage.setImageResource(R.drawable.ic_star_black_36dp);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageButton isFavoriteImage;
        public final TextView inTextList;
        public final TextView outTextList;
        public final TextView codeList;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            isFavoriteImage = (ImageButton) view.findViewById(R.id.isFavoriteImage);
            inTextList = (TextView) view.findViewById(R.id.inTextList);
            outTextList = (TextView) view.findViewById(R.id.outTextList);
            codeList = (TextView) view.findViewById(R.id.codeList);

            isFavoriteImage.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + outTextList.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.isFavoriteImage:
                    Log.d("MyLog", "кликает");
                    String code = codeList.getText().toString();
                    if (BaseFragment.checkImageResource(isFavoriteImage.getContext(), isFavoriteImage, R.drawable.ic_star_border_black_36dp)) {
                        isFavoriteImage.setImageResource(R.drawable.ic_star_black_36dp);
                        if (DBHelper.isFavoriteImage(inTextList.getText().toString(), code))
                            Toast.makeText(isFavoriteImage.getContext(), "Уже в избранном", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(isFavoriteImage.getContext(), "В избранное", Toast.LENGTH_SHORT).show();
                            DBHelper.addFavoriteImage(inTextList.getText().toString(), code);
                        }
                    }
                    else {
                        isFavoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
                        Toast.makeText(isFavoriteImage.getContext(), "Изъять из избранного", Toast.LENGTH_SHORT).show();
                        DBHelper.removeFavoriteImage(inTextList.getText().toString(), code);
                    }
                    break;
            }
        }
    }
}
