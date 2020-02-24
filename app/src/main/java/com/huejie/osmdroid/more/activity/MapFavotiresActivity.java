package com.huejie.osmdroid.more.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.model.favorites.FavoritesLatLng;
import com.huejie.osmdroid.overlay.CustomIconOverlay;
import com.huejie.osmdroid.util.Util;

import org.litepal.LitePal;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ClickablePolygon;
import org.osmdroid.views.overlay.DistancePolyline;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huejie.osmdroid.more.activity.MarkSettingActivity.ACTION_UPDATE;
import static com.huejie.osmdroid.more.activity.MarkSettingActivity.FROM_ACTION;

public class MapFavotiresActivity extends AppCompatActivity {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void clean() {
        MapViewManager.getInstance().clean();
        pointsList.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_favotires);
        ButterKnife.bind(this);
        initView();
    }

    private List<GeoPoint> pointsList = new ArrayList<>();


    private Favorites favority;
    private String action;

    private void initView() {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("清除");
        MapViewManager.getInstance().init(this, mapView);
        MapViewManager.getInstance().location().toLocationPosition();
        action = getIntent().getStringExtra(FROM_ACTION);

        if (action.equals(ACTION_UPDATE)) {
            tv_title.setVisibility(View.VISIBLE);
            favority = (Favorites) getIntent().getSerializableExtra("data");
            tv_title.setText(favority.name);
            favority.points = LitePal.where("favoritesName = ? and parentName = ?", favority.name, favority.parentName).findFirst(FavoritesLatLng.class);
            favority.accessoriesList = LitePal.where("favoritesName = ? and parentName = ?", favority.name, favority.parentName).find(FavoritesAccessories.class);
            switch (favority.type) {
                case 0:
                    //点
                    CustomIconOverlay iconOverlay = new CustomIconOverlay(null);
                    pointsList.add(Util.string2geoPoint(favority.points.coordinates));
                    iconOverlay.set(pointsList.get(0), getResources().getDrawable(favority.icon));
                    mapView.getOverlays().add(iconOverlay);
                    iconOverlay.setMarkLongClickListener(new CustomIconOverlay.MarkLongClickListener() {
                        @Override
                        public boolean onMarkerLongClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                            startActivityForResult(new Intent(MapFavotiresActivity.this, MarkSettingActivity.class).putExtra(FROM_ACTION, ACTION_UPDATE).putExtra("data", favority), 100);
                            return false;
                        }
                    });
                    mapView.zoomToBoundingBox(BoundingBox.fromGeoPoints(pointsList), true, 50);
                    break;
                case 1:
                    //线
                    pointsList.addAll(Util.string2geoPoints(favority.points.coordinates));
                    DistancePolyline polyline = new DistancePolyline(mapView);
                    polyline.setColor(ColorUtils.setAlphaComponent(favority.color, favority.alpha));
                    polyline.setWidth(favority.width);
                    polyline.setTextSize(SizeUtils.dp2px(12));
                    polyline.setOnlongClickListener(new DistancePolyline.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(DistancePolyline polyline, MapView mapView, GeoPoint eventPos) {
                            startActivityForResult(new Intent(MapFavotiresActivity.this, MarkSettingActivity.class).putExtra(FROM_ACTION, ACTION_UPDATE).putExtra("data", favority), 100);
                            return false;
                        }
                    });
                    polyline.setPoints(pointsList);
                    mapView.getOverlays().add(polyline);
                    mapView.zoomToBoundingBox(BoundingBox.fromGeoPoints(pointsList), true, 50);
                    break;
                case 2:
                case 3:
                    //面
                    pointsList.addAll(Util.string2geoPoints(favority.points.coordinates));
                    ClickablePolygon polygon = new ClickablePolygon();
                    polygon.setPoints(pointsList);
                    polygon.setStrokeColor(favority.color);
                    polygon.setStrokeWidth(favority.width);
                    polygon.setFillColor(ColorUtils.setAlphaComponent(favority.fillColor, favority.alpha));

                    polygon.setOnlongClickListener(new ClickablePolygon.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(ClickablePolygon polyline, MapView mapView, GeoPoint eventPos) {
                            startActivityForResult(new Intent(MapFavotiresActivity.this, MarkSettingActivity.class).putExtra(FROM_ACTION, ACTION_UPDATE).putExtra("data", favority), 100);
                            return false;
                        }
                    });
                    mapView.getOverlays().add(polygon);
                    mapView.zoomToBoundingBox(BoundingBox.fromGeoPoints(pointsList), true, 50);
                    break;
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
