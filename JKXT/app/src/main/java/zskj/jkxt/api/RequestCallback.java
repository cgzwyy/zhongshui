package zskj.jkxt.api;

/**
 * Created by WYY on 2017/2/24.
 */

public interface RequestCallback {
    void onSuccess(String result);
    void onFail(String errorMsg);
}
