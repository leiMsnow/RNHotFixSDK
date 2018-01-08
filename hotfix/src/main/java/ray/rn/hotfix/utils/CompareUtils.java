package ray.rn.hotfix.utils;

import android.text.TextUtils;

/**
 * Created by wecash on 2018/1/4.
 */

public class CompareUtils {

    public static int compareVersion(String oldVersion, String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return 0;
        }
        if (oldVersion.equals(newVersion)) {
            return 0;
        }

        String[] oldVersionArray = oldVersion.split("\\.");
        String[] newVersionArray = newVersion.split("\\.");

        int index = 0;
        int minLen = Math.min(oldVersionArray.length, newVersionArray.length);
        int diff = 0;

        while (index < minLen && (diff = Integer.parseInt(oldVersionArray[index]) - Integer.parseInt(newVersionArray[index])) == 0) {
            index++;
        }

        if (diff == 0) {
            for (int i = index; i < oldVersionArray.length; i++) {
                if (Integer.parseInt(oldVersionArray[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < newVersionArray.length; i++) {
                if (Integer.parseInt(newVersionArray[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}
