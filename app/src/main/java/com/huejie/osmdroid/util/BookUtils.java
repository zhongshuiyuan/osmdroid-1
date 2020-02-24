package com.huejie.osmdroid.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.AlongTheLineConditionsOfMaterialRecordBook;
import com.huejie.osmdroid.model.books.AlongTheLineDrainageSystemRecordBook;
import com.huejie.osmdroid.model.books.AlongTheLineOverallSituationRecordBook;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.CuttingAndSubgradeRecordBook;
import com.huejie.osmdroid.model.books.ExistingBridgeCulvertRecordBook;
import com.huejie.osmdroid.model.books.InterchangeRecordBook;
import com.huejie.osmdroid.model.books.LargeMediumBridgeRecordBook;
import com.huejie.osmdroid.model.books.OtherEngineeringRecordBook;
import com.huejie.osmdroid.model.books.RetainingWallRecordBook;
import com.huejie.osmdroid.model.books.SeparateTypeBridgeRecordBook;
import com.huejie.osmdroid.model.books.SmallBridgeAndCulvertRecordBook;
import com.huejie.osmdroid.model.books.TakeOrDiscardSoilRecordBook;
import com.huejie.osmdroid.model.books.TunnelEngineeringRecordBook;
import com.huejie.osmdroid.project.recordbook.bookfragment.AlongTheLineConditionsOfMaterialRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.AlongTheLineDrainageSystemRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.AlongTheLineOverallSituationRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.BookBaseFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.CuttingAndSubgradeRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.ExistingBridgeAndCulvertRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.InterchangeRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.LargeMediumBridgeRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.OtherEngineeringRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.RetainingWallRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.SeparateTypeBridgeRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.SmallBridgeAndCulvertRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.TakeOrDiscardSoilRecordBookFragment;
import com.huejie.osmdroid.project.recordbook.bookfragment.TunnelEngineeringRecordBookFragment;

import org.litepal.LitePal;

public class BookUtils {

    public static BookSimple newBookByType(String type) {

        switch (type) {
            case "RetainingWallRecordBook":
                return new RetainingWallRecordBook();
            case "TakeOrDiscardSoilRecordBook":
                return new TakeOrDiscardSoilRecordBook();
            case "CuttingAndSubgradeRecordBook":
                return new CuttingAndSubgradeRecordBook();
            case "AlongTheLineDrainageSystemRecordBook":
                return new AlongTheLineDrainageSystemRecordBook();
            case "AlongTheLineConditionsOfMaterialRecordBook":
                return new AlongTheLineConditionsOfMaterialRecordBook();
            case "AlongTheLineOverallSituationRecordBook":
                return new AlongTheLineOverallSituationRecordBook();
            case "OtherEngineeringRecordBook":
                return new OtherEngineeringRecordBook();
            case "InterchangeRecordBook":
                return new InterchangeRecordBook();
            case "SeparateTypeBridgeRecordBook":
                return new SeparateTypeBridgeRecordBook();
            case "LargeMediumBridgeRecordBook":
                return new LargeMediumBridgeRecordBook();
            case "SmallBridgeAndCulvertRecordBook":
                return new SmallBridgeAndCulvertRecordBook();
            case "ExistingBridgeAndCulvertRecordBook":
                return new ExistingBridgeCulvertRecordBook();
            case "TunnelEngineeringRecordBook":
                return new TunnelEngineeringRecordBook();
            default:
                return new BookSimple();
        }
    }

    public static Class getClassByType(long type) {
        switch ((int) type) {
            case 1:
                return RetainingWallRecordBook.class;
            case 2:
                return TakeOrDiscardSoilRecordBook.class;
            case 3:
                return CuttingAndSubgradeRecordBook.class;
            case 4:
                return AlongTheLineDrainageSystemRecordBook.class;
            case 5:
                return AlongTheLineConditionsOfMaterialRecordBook.class;
            case 6:
                return AlongTheLineOverallSituationRecordBook.class;
            case 7:
                return OtherEngineeringRecordBook.class;
            case 8:
                return InterchangeRecordBook.class;
            case 9:
                return SeparateTypeBridgeRecordBook.class;
            case 10:
                return LargeMediumBridgeRecordBook.class;
            case 11:
                return SmallBridgeAndCulvertRecordBook.class;
            case 12:
                return ExistingBridgeCulvertRecordBook.class;
            case 13:
                return TunnelEngineeringRecordBook.class;
            default:
                return null;
        }
    }

    public static void saveBook(BookSimple workPoint) {
        switch ((int) workPoint.recordBookTypeId) {
            case 1:
                ((RetainingWallRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 2:
                ((TakeOrDiscardSoilRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 3:
                ((CuttingAndSubgradeRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 4:
                ((AlongTheLineDrainageSystemRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 5:
                ((AlongTheLineConditionsOfMaterialRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 6:
                ((AlongTheLineOverallSituationRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 7:
                ((OtherEngineeringRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 8:
                ((InterchangeRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 9:
                ((SeparateTypeBridgeRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 10:
                ((LargeMediumBridgeRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 11:
                ((SmallBridgeAndCulvertRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 12:
                ((ExistingBridgeCulvertRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
            case 13:
                ((TunnelEngineeringRecordBook) workPoint).saveOrUpdate("id = ?", workPoint.id + "");
                break;
        }
    }

    public static BookBaseFragment getFragment(Context context, String projectName, BookSimple book) {
        DBUtil.useExtraDbByProjectName(context, projectName);
        if (book instanceof RetainingWallRecordBook) {
            return RetainingWallRecordBookFragment.newInstance(LitePal.find(RetainingWallRecordBook.class, book.id), projectName);
        } else if (book instanceof InterchangeRecordBook) {
            return InterchangeRecordBookFragment.newInstance(LitePal.find(InterchangeRecordBook.class, book.id), projectName);
        } else if (book instanceof CuttingAndSubgradeRecordBook) {
            return CuttingAndSubgradeRecordBookFragment.newInstance(LitePal.find(CuttingAndSubgradeRecordBook.class, book.id), projectName);
        } else if (book instanceof AlongTheLineDrainageSystemRecordBook) {
            return AlongTheLineDrainageSystemRecordBookFragment.newInstance(LitePal.find(AlongTheLineDrainageSystemRecordBook.class, book.id), projectName);
        } else if (book instanceof AlongTheLineConditionsOfMaterialRecordBook) {
            return AlongTheLineConditionsOfMaterialRecordBookFragment.newInstance(LitePal.find(AlongTheLineConditionsOfMaterialRecordBook.class, book.id), projectName);
        } else if (book instanceof AlongTheLineOverallSituationRecordBook) {
            return AlongTheLineOverallSituationRecordBookFragment.newInstance(LitePal.find(AlongTheLineOverallSituationRecordBook.class, book.id), projectName);
        } else if (book instanceof OtherEngineeringRecordBook) {
            return OtherEngineeringRecordBookFragment.newInstance(LitePal.find(OtherEngineeringRecordBook.class, book.id), projectName);
        } else if (book instanceof TakeOrDiscardSoilRecordBook) {
            return TakeOrDiscardSoilRecordBookFragment.newInstance(LitePal.find(TakeOrDiscardSoilRecordBook.class, book.id), projectName);
        } else if (book instanceof SeparateTypeBridgeRecordBook) {
            return SeparateTypeBridgeRecordBookFragment.newInstance(LitePal.find(SeparateTypeBridgeRecordBook.class, book.id), projectName);
        } else if (book instanceof LargeMediumBridgeRecordBook) {
            return LargeMediumBridgeRecordBookFragment.newInstance(LitePal.find(LargeMediumBridgeRecordBook.class, book.id), projectName);
        } else if (book instanceof SmallBridgeAndCulvertRecordBook) {
            return SmallBridgeAndCulvertRecordBookFragment.newInstance(LitePal.find(SmallBridgeAndCulvertRecordBook.class, book.id), projectName);
        } else if (book instanceof ExistingBridgeCulvertRecordBook) {
            return ExistingBridgeAndCulvertRecordBookFragment.newInstance(LitePal.find(ExistingBridgeCulvertRecordBook.class, book.id), projectName);
        } else if (book instanceof TunnelEngineeringRecordBook) {
            return TunnelEngineeringRecordBookFragment.newInstance(LitePal.find(TunnelEngineeringRecordBook.class, book.id), projectName);
        } else {
            return null;
        }
    }

    public static Drawable getDrawableByRecordBookTypeId(Context context, long id) {
        switch ((int) id) {
            case 1:
                return context.getResources().getDrawable(R.mipmap.retaining_wall);
            case 2:
                return context.getResources().getDrawable(R.mipmap.take_or_discard_soil);
            case 3:
                return context.getResources().getDrawable(R.mipmap.cutting_and_subgrade);
            case 4:
                return context.getResources().getDrawable(R.mipmap.along_the_line_drainage_system);
            case 5:
                return context.getResources().getDrawable(R.mipmap.along_the_line_conditions_of_material);
            case 6:
                return context.getResources().getDrawable(R.mipmap.along_the_line_overall_situation);
            case 7:
                return context.getResources().getDrawable(R.mipmap.other_engineering);
            case 8:
                return context.getResources().getDrawable(R.mipmap.interchange);
            case 9:
                return context.getResources().getDrawable(R.mipmap.separate_type_bridge);
            case 10:
                return context.getResources().getDrawable(R.mipmap.large_medium_bridge);
            case 11:
                return context.getResources().getDrawable(R.mipmap.small_bridge_and_culvert);
            case 12:
                return context.getResources().getDrawable(R.mipmap.existing_bridge_and_culvert);
            case 13:
                return context.getResources().getDrawable(R.mipmap.tunnel_engineering);
            default:
                return null;
        }

    }
}
