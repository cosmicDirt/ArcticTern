package com.mirrordust.telecomlocate.interf;

/**
 * Created by LiaoShanhe on 2017/07/27/027.
 */

public interface CollectionContract {

    interface View extends BaseView<Presenter> {
        
        void showNoNewSample();

        void showNewSample();
    }

    interface Presenter extends BasePresenter {

    }


}
