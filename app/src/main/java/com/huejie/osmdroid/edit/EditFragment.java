package com.huejie.osmdroid.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.model.favorites.FavoritesLatLng;
import com.huejie.osmdroid.more.activity.ChooseMapActivity;
import com.huejie.osmdroid.more.activity.MarkSettingActivity;
import com.huejie.osmdroid.overlay.CustomIconOverlay;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.GISUtils;
import com.huejie.osmdroid.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.litepal.LitePal;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ClickablePolygon;
import org.osmdroid.views.overlay.DistancePolyline;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.IconOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.huejie.osmdroid.more.activity.MarkSettingActivity.ACTION_ADD;
import static com.huejie.osmdroid.more.activity.MarkSettingActivity.ACTION_UPDATE;
import static com.huejie.osmdroid.more.activity.MarkSettingActivity.FROM_ACTION;

/**
 * 底图切换类
 *
 * @author guc
 */

public class EditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_scale)
    TextView tv_scale;
    @BindView(R.id.iv_overlay)
    ImageView iv_overlay;

    @BindView(R.id.iv_logo)
    ImageView iv_map_logo;

    @BindView(R.id.ctv_point)
    CheckedTextView ctv_point;
    @BindView(R.id.ctv_line)
    CheckedTextView ctv_line;
    @BindView(R.id.ctv_area)
    CheckedTextView ctv_area;
    @BindView(R.id.ctv_circle)
    CheckedTextView ctv_circle;
    private ArrayList<GeoPoint> pointsList = new ArrayList<>();

    @OnClick(R.id.location)
    public void getLocation() {
        MapViewManager.getInstance().toLocationPosition();
    }


    public EditFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.tv_clean)
    public void clean() {
        MapViewManager.getInstance().clean();
        pointsList.clear();
        mapView.getOverlays().add(folderOverlay);
    }

    @OnClick(R.id.iv_overlay)
    public void overlay() {
        startActivity(new Intent(getActivity(), ChooseMapActivity.class));

    }

    private Favorites favority;


    @OnClick(R.id.ctv_point)
    public void point() {
        if (type != 0) {
            ctv_point.setChecked(true);
            ctv_line.setChecked(false);
            ctv_area.setChecked(false);
            ctv_circle.setChecked(false);
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = 0;
            favority.type = 0;
        } else {
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = -1;
            favority.type = -1;
            ctv_point.setChecked(false);
        }
        mapView.getOverlays().add(folderOverlay);
    }

    @OnClick(R.id.ctv_line)
    public void line() {
        if (type != 1) {
            ctv_point.setChecked(false);
            ctv_line.setChecked(true);
            ctv_area.setChecked(false);
            ctv_circle.setChecked(false);
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = 1;
            favority.type = 1;
        } else {
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = -1;
            favority.type = -1;
            ctv_line.setChecked(false);
        }
        mapView.getOverlays().add(folderOverlay);
    }

    @OnClick(R.id.ctv_area)
    public void area() {
        if (type != 2) {
            ctv_point.setChecked(false);
            ctv_line.setChecked(false);
            ctv_area.setChecked(true);
            ctv_circle.setChecked(false);
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = 2;
            favority.type = 2;
        } else {
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = -1;
            favority.type = -1;
            ctv_area.setChecked(false);
        }
        mapView.getOverlays().add(folderOverlay);
    }

    @OnClick(R.id.ctv_circle)
    public void circle() {
        if (type != 3) {
            ctv_point.setChecked(false);
            ctv_line.setChecked(false);
            ctv_area.setChecked(false);
            ctv_circle.setChecked(true);
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = 3;
            favority.type = 3;
        } else {
            MapViewManager.getInstance().clean();
            pointsList.clear();
            type = -1;
            favority.type = -1;
            ctv_area.setChecked(false);
        }
        mapView.getOverlays().add(folderOverlay);
    }

    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    private FolderOverlay folderOverlay;

    private void loadFavorites() {
        LitePal.useDefault();
        List<Favorites> favoritys = LitePal.where("fileType = ? and fileStatus = ?", "0", "0").find(Favorites.class);
        for (int i = 0; i < favoritys.size(); i++) {
            final Favorites fav = favoritys.get(i);
            fav.accessoriesList = LitePal.where("favoritesName = ? and parentName = ?", fav.name, fav.parentName).find(FavoritesAccessories.class);
            fav.points = LitePal.where("favoritesName = ? and parentName = ?", fav.name, fav.parentName).findFirst(FavoritesLatLng.class);
            if (fav.type == 0) {
                //点
                CustomIconOverlay iconOverlay = new CustomIconOverlay(null);
                iconOverlay.set(Util.string2geoPoint(fav.points.coordinates), getResources().getDrawable(fav.icon));
                iconOverlay.setMarkLongClickListener(new CustomIconOverlay.MarkLongClickListener() {
                    @Override
                    public boolean onMarkerLongClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                        startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", fav).putExtra(FROM_ACTION, ACTION_UPDATE), 100);
                        return true;
                    }
                });
                folderOverlay.add(iconOverlay);

            } else if (fav.type == 1) {
                DistancePolyline polyline = new DistancePolyline(mapView);
                polyline.setColor(ColorUtils.setAlphaComponent(fav.color, fav.alpha));
                polyline.setWidth(fav.width);
                polyline.setTextSize(SizeUtils.dp2px(12));
                polyline.setOnlongClickListener(new DistancePolyline.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(DistancePolyline polyline, MapView mapView, GeoPoint eventPos) {
                        startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", fav).putExtra(FROM_ACTION, ACTION_UPDATE), 100);
                        return true;
                    }
                });
                List<GeoPoint> points = Util.string2geoPoints(fav.points.coordinates);

                polyline.setPoints(points);
                folderOverlay.add(polyline);

            } else if (fav.type == 2 || fav.type == 3) {
                ClickablePolygon polygon = new ClickablePolygon();
                polygon.setStrokeColor(fav.color);
                polygon.setStrokeWidth(fav.width);
                polygon.setFillColor(ColorUtils.setAlphaComponent(fav.fillColor, fav.alpha));
                List<GeoPoint> points = Util.string2geoPoints(fav.points.coordinates);
                polygon.setPoints(points);
                polygon.setOnlongClickListener(new ClickablePolygon.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(ClickablePolygon polyline, MapView mapView, GeoPoint eventPos) {
                        startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", fav).putExtra(FROM_ACTION, ACTION_UPDATE), 100);
                        return true;
                    }
                });
                folderOverlay.add(polygon);

            }

        }
        mapView.getOverlays().add(folderOverlay);

    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //首先把所有的收藏夹里面的图形都显示在地图上
        folderOverlay = new FolderOverlay();
        loadFavorites();

        favority = new Favorites();
        favority.color = Config.Favorites.DEFAULT_COLOR;
        favority.width = Config.Favorites.DEFAULT_WIDTH;
        favority.fillColor = ColorUtils.setAlphaComponent(Config.Favorites.DEFAULT_FILLCOLOR, Config.Favorites.DEFAULT_ALPHA);
        MapViewManager.getInstance().init(getActivity(), mapView);
        MapViewManager.getInstance().addScaleBar().location().toLocationPosition();

        iv_map_logo.setImageResource(Util.getLogoByMapType(AppContext.sp.getString(Config.CURRENT_MAP)));
        initListener();
        tv_scale.setText(Util.m1(mapView.getZoomLevel()));
        mapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                tv_scale.setText(Util.m1(event.getZoomLevel()));
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            folderOverlay.getItems().clear();
            mapView.getOverlays().remove(folderOverlay);
            loadFavorites();
        }
    }

    private int type = -1;

    private void initListener() {

        MapViewManager.getInstance().setMapEventListener(new MapViewManager.MapEventListener() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                switch (type) {
                    case 0:
                        pointsList.clear();
                        pointsList.add(p);
                        MapViewManager.getInstance().clean();
                        mapView.getOverlays().add(folderOverlay);
                        CustomIconOverlay iconOverlay = new CustomIconOverlay(null);
                        iconOverlay.set(p, getResources().getDrawable(R.mipmap.marker_icon_1));
                        mapView.getOverlays().add(iconOverlay);
                        iconOverlay.setMarkLongClickListener(new CustomIconOverlay.MarkLongClickListener() {
                            @Override
                            public boolean onMarkerLongClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                                GeoPoint point = new GeoPoint(makerPosition.getLatitude(), makerPosition.getLongitude());
                                FavoritesLatLng latLng = new FavoritesLatLng();
                                JSONArray array = new JSONArray();
                                try {
                                    array.put(point.getLongitude());
                                    array.put(point.getLatitude());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                latLng.coordinates = array.toString();
                                favority.points = latLng;
                                startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", favority).putExtra(FROM_ACTION, ACTION_ADD), 100);
                                return false;
                            }
                        });
                        break;
                    case 1:
                        //绘制线段
                        MapViewManager.getInstance().clean();
                        mapView.getOverlays().add(folderOverlay);
                        pointsList.add(p);
                        for (int i = 0; i < pointsList.size(); i++) {
                            CustomIconOverlay lineOverlay = new CustomIconOverlay(null);
                            lineOverlay.set(pointsList.get(i), getResources().getDrawable(R.mipmap.circle));
                            mapView.getOverlays().add(lineOverlay);
                        }
                        if (pointsList.size() >= 2) {
                            double totalDist = 0;
                            //开始绘制线
                            for (int i = 0; i < pointsList.size() - 1; i++) {
                                GeoPoint g0 = pointsList.get(i);
                                GeoPoint g1 = pointsList.get(i + 1);
                                double dist = GISUtils.getDistance(g0, g1);
                                totalDist += dist;
                            }
                            DistancePolyline polyline = new DistancePolyline(mapView);
                            polyline.setColor(favority.color);
                            polyline.setWidth(favority.width);
                            polyline.setTextSize(SizeUtils.dp2px(12));
                            polyline.setOnlongClickListener(new DistancePolyline.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(DistancePolyline polyline, MapView mapView, GeoPoint eventPos) {
                                    JSONArray arrays = new JSONArray();
                                    FavoritesLatLng points = new FavoritesLatLng();
                                    for (int i = 0; i < polyline.getPoints().size(); i++) {
                                        JSONArray array = new JSONArray();
                                        try {
                                            array.put(polyline.getPoints().get(i).getLongitude());
                                            array.put(polyline.getPoints().get(i).getLatitude());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        arrays.put(array);
                                    }
                                    points.coordinates = arrays.toString();
                                    favority.points = points;
                                    startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", favority).putExtra(FROM_ACTION, ACTION_ADD), 100);
                                    return false;
                                }
                            });
                            polyline.setPoints(pointsList);
                            mapView.getOverlays().add(polyline);
                            ToastUtils.showShortToast("总长度为" + (int) totalDist + "米");
                        }

                        break;
                    case 2:
                        pointsList.add(p);
                        if (pointsList.size() < 2) {
                            mapView.getOverlays().add(new IconOverlay(p, getResources().getDrawable(R.mipmap.circle)));
                        } else {
                            //首先删除掉前面的两个标注点
                            MapViewManager.getInstance().clean();
                            mapView.getOverlays().add(folderOverlay);
                            final ClickablePolygon polygon = new ClickablePolygon();
                            if (pointsList.size() == 2) {
                                ArrayList<GeoPoint> rectList = ClickablePolygon.pointsAsRect(BoundingBox.fromGeoPoints(pointsList));
                                polygon.setPoints(rectList);
                            } else {
                                polygon.setPoints(pointsList);
                            }
                            polygon.setStrokeColor(favority.color);
                            polygon.setStrokeWidth(favority.width);
                            polygon.setFillColor(favority.fillColor);
                            polygon.setOnlongClickListener(new ClickablePolygon.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(ClickablePolygon polyline, MapView mapView, GeoPoint eventPos) {
                                    JSONArray arrays = new JSONArray();
                                    FavoritesLatLng points = new FavoritesLatLng();
                                    for (int i = 0; i < polygon.getPoints().size(); i++) {
                                        JSONArray array = new JSONArray();
                                        try {
                                            array.put(polygon.getPoints().get(i).getLongitude());
                                            array.put(polygon.getPoints().get(i).getLatitude());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        arrays.put(array);
                                    }
                                    points.coordinates = arrays.toString();
                                    favority.points = points;
                                    startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", favority).putExtra(FROM_ACTION, ACTION_ADD), 100);
                                    return false;
                                }
                            });
                            mapView.getOverlays().add(polygon);
                            ToastUtils.showShortToast(GISUtils.calcArea(pointsList, "DEGREES"));
                        }
                        break;

                    case 3:
                        pointsList.add(p);
                        if (pointsList.size() < 2) {
                            mapView.getOverlays().add(new IconOverlay(p, getResources().getDrawable(R.mipmap.circle)));
                        } else if (pointsList.size() == 2) {
                            MapViewManager.getInstance().clean();
                            mapView.getOverlays().add(folderOverlay);
                            final ClickablePolygon polygon = new ClickablePolygon();
                            final double radius = GISUtils.getDistance(pointsList.get(0), pointsList.get(1));
                            ArrayList<GeoPoint> circleList = ClickablePolygon.pointsAsCircle(pointsList.get(0), radius);
                            polygon.setPoints(circleList);
                            polygon.setStrokeColor(favority.color);
                            polygon.setStrokeWidth(favority.width);
                            polygon.setFillColor(favority.fillColor);
                            polygon.setOnlongClickListener(new ClickablePolygon.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(ClickablePolygon polyline, MapView mapView, GeoPoint eventPos) {
                                    JSONArray arrays = new JSONArray();
                                    FavoritesLatLng points = new FavoritesLatLng();
                                    for (int i = 0; i < polygon.getPoints().size(); i++) {
                                        JSONArray array = new JSONArray();
                                        try {
                                            array.put(polygon.getPoints().get(i).getLongitude());
                                            array.put(polygon.getPoints().get(i).getLatitude());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        arrays.put(array);
                                    }
                                    points.coordinates = arrays.toString();
                                    favority.points = points;
                                    startActivityForResult(new Intent(getActivity(), MarkSettingActivity.class).putExtra("data", favority).putExtra(FROM_ACTION, ACTION_ADD), 100);
                                    return false;
                                }
                            });
                            mapView.getOverlays().add(polygon);

                        }

                        break;
                }
                mapView.invalidate();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
