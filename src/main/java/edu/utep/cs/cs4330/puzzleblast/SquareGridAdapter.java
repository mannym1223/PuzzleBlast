package edu.utep.cs.cs4330.puzzleblast;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class SquareGridAdapter extends ArrayAdapter{

    private Activity context;
    private GameGrid grid;

    SquareGridAdapter(Context context) {
        super(context, R.layout.square_layout, GameGrid.getInstance().getSquares());
        this.context = (Activity) context;
        grid = GameGrid.getInstance();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View squareView = inflater.inflate(R.layout.square_layout, parent, false);

        List<Square> squares = grid.getSquares();
        if(squares == null) {
            return squareView;
        }

        ImageView img = squareView.findViewById(R.id.squareImage);
        Animation anim = squares.get(position).getAnim();

        if(squares.size() != 0) {
            img.setImageDrawable(squares.get(position).getImage());
            if(anim != null){
                img.startAnimation(anim);
                squares.get(position).removeAnim();
            }
        }
        else {
            img.setImageDrawable(context.getDrawable(R.drawable.green_square));
        }

        Log.d("adapter", "finished with view");
        return squareView;
    }
}
