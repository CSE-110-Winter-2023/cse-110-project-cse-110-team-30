package com.example.team30.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.util.Pair;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team30.R;

import java.util.Collections;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<Friend> friends = Collections.emptyList();
    private Pair<Double, Double> myLocation;
    private List<Integer> Circle;

    private float orientation;
    private int circleRadius;
    private int centerX;
    private int centerY;
    private int numCircle;
    private static final int TYPE_CENTER_TRIANGLE = 0;
    private static final int TYPE_CIRCLE = 1;
    private static final int TYPE_DOT = 2;

//    public class CenterTriangleViewHolder extends RecyclerView.ViewHolder {
//        public final TextView centerTriangleLabelTextView;
//
//        public CenterTriangleViewHolder(View view) {
//            super(view);
//            centerTriangleLabelTextView = view.findViewById(R.id.triangle);
//        }
//    }
//
//    public class CircleViewHolder extends RecyclerView.ViewHolder {
//        public final TextView circleLabelTextView;
//
//        public CircleViewHolder(View view) {
//            super(view);
//            circleLabelTextView = view.findViewById(R.id.outerCircle);
//        }
//        public void bind(int size) {
//            circleLabelTextView.setLayoutParams(new ConstraintLayout.LayoutParams(size, size))
//        }
//    }
    /**
     * This time around, the ViewHolder is much simpler, just data.
     * This is closer to "modern" Kotlin Android conventions.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final TextView label;
        public final TextView dot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.label = itemView.findViewById(R.id.friend_label);
            this.dot = itemView.findViewById(R.id.friend_dot);
        }

        public void bind(Friend friend) {
            label.setText(friend.UID);
//            float longitude = friend.longitude;
//            float latitude = friend.latitude;

            Pair<Float,Float> pointData = calculateAngles(friend, orientation);

            float x = centerX + pointData.second * (float) Math.cos(Math.toRadians(pointData.first));
            float y = centerY + pointData.second  * (float) Math.sin(Math.toRadians(pointData.first));

            label.setX(x - label.getWidth() / 2);
            label.setY(y - label.getHeight() / 2);

            dot.setX(x - dot.getWidth() / 2);
            dot.setY(y - dot.getHeight() / 2);

//            ConstraintLayout.LayoutParams layoutText = (ConstraintLayout.LayoutParams) label.getLayoutParams();
//            layoutText.circleAngle = angle;
//            label.setLayoutParams(layoutText);
//
//            ConstraintLayout.LayoutParams layoutdot = (ConstraintLayout.LayoutParams) dot.getLayoutParams();
//            layoutdot.circleAngle = angle;
//            dot.setLayoutParams(layoutdot);
            //TODO: Set the radius (layout_constraintCircleRadius)
        }
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public void setMyLocation(Pair<Double, Double> mylocation) {
        this.myLocation = mylocation;
        notifyDataSetChanged();
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
        notifyDataSetChanged();
    }

    public void setOtherData(int circleRadius, int centerX, int centerY, int numCircle) {
        this.circleRadius = circleRadius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.numCircle = numCircle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        var view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.friend_item, parent, false);
//        return new ViewHolder(view);

        View itemView;
//        if (viewType == TYPE_CENTER_TRIANGLE) {
//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.center_item, parent, false);
//            return new CenterTriangleViewHolder(itemView);
//        } else if (viewType == TYPE_CIRCLE) {
//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.circle_item, parent, false);
//            return new CircleViewHolder(itemView);
//        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_item, parent, false);
            return new ViewHolder(itemView);
//        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        MyItem item = itemList.get(position);
//        if (holder.getItemViewType() == TYPE_CENTER_TRIANGLE) {
//            // set the centerTriangleImageView and centerTriangleLabelTextView
//            // based on the data in the MyItem object
//        } else if (holder.getItemViewType() == TYPE_CIRCLE) {
//            // set the circleImageView and circleLabelTextView
//            // based on the data in the MyItem object
//            var circle = Circle.get(position);
//            holder.bind(circle);
//        } else {
            // set the dotView and dotLabelTextView
            // based on the data in the MyItem object
            var friend = friends.get(position - numCircle);
            holder.bind(friend);
//        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public long getItemId(int position) {
        return friends.get(position).UID.hashCode();
    }

    public Pair<Float, Float> calculateAngles(Friend friend, float orientation){
        float longti = friend.longitude;
        float lati = friend.latitude;

        float y = (float) (longti - myLocation.second);
        float x = (float) (lati - myLocation.first);

        double angle = Math.atan(y/x) * 180/Math.PI;
        if(x < 0){
            angle = angle + 180;
        }
        if(x > 0 && y < 0){
            angle = angle + 360;
        }
        float newangle = (float)(angle - orientation);
        float newRadius = (float) Math.sqrt(x*x + y*y)/circleRadius;
        Pair<Float, Float> newPair = new Pair<>(newangle, newRadius);
        return newPair;
//
//            position.put(UID, newPair);
    }
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CENTER_TRIANGLE;
        } else if (position <= numCircle) {
            return TYPE_CIRCLE;
        } else {
            return TYPE_DOT;
        }
    }
}


