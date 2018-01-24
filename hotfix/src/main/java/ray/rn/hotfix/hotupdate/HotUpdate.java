package ray.rn.hotfix.hotupdate;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

import ray.rn.hotfix.constants.AppConstant;
import ray.rn.hotfix.constants.FileConstant;

public class HotUpdate {

    public interface HandleCallback {
        void handle();
    }

    // 查看本地是否有zip
    public static void checkTempZip(Context context) {
        File bundleFile = new File(FileConstant.TEMP_ZIP_PATH);
        ACache.get(context).put(AppConstant.FORCE_UPDATE, bundleFile.exists());
    }

    public static void handleZIP(final String path) {

        // 开启单独线程，解压，合并。
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 临时文件解析
                String tempFolder = path + FileConstant.TEMP_FOLDER;
                FileUtils.decompression(path, tempFolder + FileConstant.ZIP_NAME);
//                String assetsBundle;
//                if (checkLocalBundle()) {
//                    assetsBundle = FileUtils.getJsBundleFromSDCard();
//                } else {
//                    assetsBundle = FileUtils.getJsBundleFromAssets(context);
//                }
//                String patchStr = FileUtils.getStringFromPat(FileConstant.TEMP_JS_BUNDLE);
//                merge(patchStr, assetsBundle);
//                FileUtils.copyPatchImgs(FileConstant.TEMP_DRAWABLE, FileConstant.LOCAL_DRAWABLE);
                FileUtils.traversalFile(tempFolder);
            }
        }).start();
    }

    private static boolean checkLocalBundle() {
        File bundleFile = new File(FileConstant.LOCAL_JS_BUNDLE);
        return bundleFile.exists();
    }

    /**
     * 合并,生成新的bundle文件
     */
    private static void merge(String newBundle, String oldBundle) {

        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(newBundle, oldBundle);
        for (diff_match_patch.Diff diff : diffs) {
            Log.d("merge", diff.toString());
        }
        LinkedList<diff_match_patch.Patch> patches = dmp.patch_make(diffs);
        String patchesStr = dmp.patch_toText(patches);
        LinkedList<diff_match_patch.Patch> patchBundle = (LinkedList<diff_match_patch.Patch>) dmp.patch_fromText(patchesStr);
        Object[] bundleArray = dmp.patch_apply(patchBundle, oldBundle);
        try {
            Writer writer = new FileWriter(FileConstant.LOCAL_JS_BUNDLE);
            String localBundle = (String) bundleArray[0];
            writer.write(localBundle);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
