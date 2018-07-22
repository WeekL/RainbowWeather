package weekl.weatherdemo.base;

public abstract class Presenter<T extends IView> {
    private T mView;

    protected T getView(){
        return mView;
    }

    public void attachView(T view){
        mView = view;
    }

    public void detachView(){
        mView = null;
    }

    protected boolean isViewAttached(){
        return mView != null;
    }
}
