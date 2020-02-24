package org.osmdroid.views.overlay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import java.util.List;

/**
 * A {@link FolderOverlay} is just a group of other {@link org.osmdroid.views.overlay.Overlay}s.
 *
 * <img alt="Class diagram around Marker class" width="686" height="413" src='./doc-files/marker-classes.png' />
 * 
 * @author M.Kergall
 */
public class FolderOverlay extends Overlay {

	protected OverlayManager mOverlayManager;
	protected String mName, mDescription;

	/** Use {@link #FolderOverlay()} instead */
	@Deprecated
	public FolderOverlay(Context ctx) {
		this();
	}

	public FolderOverlay() {
		super();
		mOverlayManager = new DefaultOverlayManager(null);
		mName = "";
		mDescription = "";
	}
	
	public void setName(String name){
		mName = name;
	}
	
	public String getName(){
		return mName;
	}
	
	public void setDescription(String description){
		mDescription = description;
	}
	
	public String getDescription(){
		return mDescription;
	}
	
	/**
	 * @return the list of components of this folder. 
	 * Doesn't provide a copy, but the actual list. 
	 */
	public List<Overlay> getItems(){
		return mOverlayManager;
	}
	
	public boolean add(Overlay item){
		return mOverlayManager.add(item);
	}
	
	public boolean remove(Overlay item){
		return mOverlayManager.remove(item);
	}

	@SuppressLint("WrongCall")
	@Override public void draw(final Canvas pCanvas, final Projection pProjection) {
		mOverlayManager.onDraw(pCanvas, pProjection);
	}

	@SuppressLint("WrongCall")
	@Override public void draw(final Canvas pCanvas, final MapView pMapView, final boolean pShadow) {
		if (pShadow) {
			return;
		}
		mOverlayManager.onDraw(pCanvas, pMapView);
	}

	@Override public boolean onSingleTapUp(MotionEvent e, MapView mapView){
		if (isEnabled()) {
            return mOverlayManager.onSingleTapUp(e, mapView);
        } else {
            return false;
        }
	}
	
	@Override public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView){
		if (isEnabled()) {
            return mOverlayManager.onSingleTapConfirmed(e, mapView);
        } else {
            return false;
        }
	}
	
	@Override public boolean onLongPress(MotionEvent e, MapView mapView){
		if (isEnabled()) {
            return mOverlayManager.onLongPress(e, mapView);
        } else {
            return false;
        }
	}

	@Override public boolean onTouchEvent(MotionEvent e, MapView mapView){
		if (isEnabled()) {
            return mOverlayManager.onTouchEvent(e, mapView);
        } else {
            return false;
        }
	}
	
	//TODO: implement other events... 
	
	/**
	 * Close all opened InfoWindows of overlays it contains. 
	 * This only operates on overlays that inherit from OverlayWithIW. 
	 */
	public void closeAllInfoWindows(){
		for (Overlay overlay:mOverlayManager){
			if (overlay instanceof FolderOverlay){
				((FolderOverlay)overlay).closeAllInfoWindows();
			} else if (overlay instanceof OverlayWithIW){
				((OverlayWithIW)overlay).closeInfoWindow();
			}
		}
	}

	@Override
	public void onDetach(MapView mapView){
		if (mOverlayManager!=null) {
            mOverlayManager.onDetach(mapView);
        }
		mOverlayManager=null;
	}

}
