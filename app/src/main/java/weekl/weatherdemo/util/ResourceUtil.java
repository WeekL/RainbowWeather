package weekl.weatherdemo.util;

import android.content.Context;
import android.content.res.Resources;

public class ResourceUtil {

    public static int getResIdFromString(Context context, String res){
        Resources r = context.getResources();
        int id = r.getIdentifier(res, "drawable", context.getPackageName());
        return id;
    }
}
