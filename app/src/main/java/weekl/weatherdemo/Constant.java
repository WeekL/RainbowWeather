package weekl.weatherdemo;

import java.util.ArrayList;
import java.util.List;

public class Constant {

    public static final int[] ITEM_BLUE = {R.color.blueItem, R.color.blueItemDark};
    public static final int[] ITEM_YELLOW = {R.color.yellowItem, R.color.yellowItemDark};
    public static final int[] ITEM_PURPLE = {R.color.purpleItem, R.color.purpleItemDark};
    public static final int[] ITEM_TEAL = {R.color.tealItem, R.color.tealItemDark};
    public static final int[] ITEM_RED = {R.color.redItem, R.color.redItemDark};

    public static List<int[]> getColors(){
        List<int[]> colors = new ArrayList<>();
        colors.add(Constant.ITEM_BLUE);
        colors.add(Constant.ITEM_PURPLE);
        colors.add(Constant.ITEM_RED);
        colors.add(Constant.ITEM_TEAL);
        colors.add(Constant.ITEM_YELLOW);
        return colors;
    }
}
