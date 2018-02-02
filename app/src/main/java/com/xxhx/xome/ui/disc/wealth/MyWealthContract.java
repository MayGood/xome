package com.xxhx.xome.ui.disc.wealth;

import com.xxhx.xome.ui.BasePresenter;
import com.xxhx.xome.ui.disc.wealth.data.CombinedAccount;
import java.util.List;

/**
 * Created by xxhx on 2017/4/5.
 */

public interface MyWealthContract {
    interface View {
        void showWealthAccounts(List<CombinedAccount> combinedAccounts);
    }

    interface Presenter extends BasePresenter {

    }
}
