package com.huejie.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.DatabaseFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;

import java.util.concurrent.atomic.AtomicReference;


public class MyMapTileSqlCacheProvider extends MapTileFileStorageProviderBase {

    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Fields
    // ===========================================================

    private final AtomicReference<ITileSource> mTileSource = new AtomicReference<ITileSource>();
    private MySqlTileWriter mWriter;
    private static final String[] columns = {DatabaseFileArchive.COLUMN_TILE, MySqlTileWriter.COLUMN_EXPIRES};

    // ===========================================================
    // Constructors
    // ===========================================================

    @Deprecated
    public MyMapTileSqlCacheProvider(final IRegisterReceiver pRegisterReceiver,
                                     final ITileSource pTileSource, final long pMaximumCachedFileAge) {
        this(pRegisterReceiver, pTileSource);
    }


    public MyMapTileSqlCacheProvider(final IRegisterReceiver pRegisterReceiver,
                                     final ITileSource pTileSource) {
        super(pRegisterReceiver,
                Configuration.getInstance().getTileFileSystemThreads(),
                Configuration.getInstance().getTileFileSystemMaxQueueSize());

        setTileSource(pTileSource);
        mWriter = new MySqlTileWriter();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean getUsesDataConnection() {
        return false;
    }

    @Override
    protected String getName() {
        return "SQL Cache Archive Provider";
    }

    @Override
    protected String getThreadGroupName() {
        return "sqlcache";
    }

    @Override
    public MyMapTileSqlCacheProvider.TileLoader getTileLoader() {
        return new MyMapTileSqlCacheProvider.TileLoader();
    }

    @Override
    public int getMinimumZoomLevel() {
        ITileSource tileSource = mTileSource.get();
        return tileSource != null ? tileSource.getMinimumZoomLevel() : OpenStreetMapTileProviderConstants.MINIMUM_ZOOMLEVEL;
    }

    @Override
    public int getMaximumZoomLevel() {
        ITileSource tileSource = mTileSource.get();
        return tileSource != null ? tileSource.getMaximumZoomLevel()
                : org.osmdroid.util.TileSystem.getMaximumZoomLevel();
    }

    @Override
    protected void onMediaMounted() {

    }

    @Override
    protected void onMediaUnmounted() {
        if (mWriter != null) {
            mWriter.onDetach();
        }
        mWriter = new MySqlTileWriter();
    }

    @Override
    public void setTileSource(final ITileSource pTileSource) {
        mTileSource.set(pTileSource);
    }

    @Override
    public void detach() {

        if (mWriter != null) {
            mWriter.onDetach();
        }
        mWriter = null;
        super.detach();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * returns true if the given tile for the current map source exists in the cache db
     */
    public boolean hasTile(final long pMapTileIndex) {
        ITileSource tileSource = mTileSource.get();
        if (tileSource == null) {
            return false;
        }
        return mWriter.getExpirationTimestamp(tileSource, pMapTileIndex) != null;
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {

        @Override
        public Drawable loadTile(final long pMapTileIndex) throws CantContinueException {

            ITileSource tileSource = mTileSource.get();
            if (tileSource == null) {
                return null;
            }


            if (mWriter != null) {
                try {
                    final Drawable result = mWriter.loadTile(tileSource, pMapTileIndex);
                    if (result == null) {
                        Counters.fileCacheMiss++;
                    } else {
                        Counters.fileCacheHit++;
                    }
                    return result;
                } catch (final BitmapTileSourceBase.LowMemoryException e) {
                    // low memory so empty the queue
                    Log.w(IMapView.LOGTAG, "LowMemoryException downloading MapTile: " + MapTileIndex.toString(pMapTileIndex) + " : " + e);
                    Counters.fileCacheOOM++;
                    throw new CantContinueException(e);
                } catch (final Throwable e) {
                    Log.e(IMapView.LOGTAG, "Error loading tile", e);
                    return null;
                }
            } else {
                Log.d(IMapView.LOGTAG, "TileLoader failed to load tile due to mWriter being null (map shutdown?)");
            }
            return null;
        }
    }
}
