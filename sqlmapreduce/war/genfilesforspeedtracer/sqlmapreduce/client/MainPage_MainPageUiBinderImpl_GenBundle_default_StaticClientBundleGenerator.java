package sqlmapreduce.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class MainPage_MainPageUiBinderImpl_GenBundle_default_StaticClientBundleGenerator implements sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenBundle {
  public sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenCss_style style() {
    return style;
  }
  private void _init0() {
    style = new sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenCss_style() {
    private boolean injected;
    public boolean ensureInjected() {
      if (!injected) {
        injected = true;
        com.google.gwt.dom.client.StyleInjector.inject(getText());
        return true;
      }
      return false;
    }
    public String getName() {
      return "style";
    }
    public String getText() {
      return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".error{font-weight:" + ("bold")  + ";color:" + ("red")  + ";white-space:" + ("pre")  + ";}.results{font-family:" + ("monospace")  + ";white-space:" + ("pre")  + ";}.GB-DVFGDLF{width:" + ("30em")  + ";height:" + ("5em")  + ";white-space:" + ("normal")  + ";}.GB-DVFGDKF{border:" + ("1px"+ " " +"solid"+ " " +"black")  + ";width:" + ("100%")  + ";height:") + (("500px")  + ";}")) : ((".error{font-weight:" + ("bold")  + ";color:" + ("red")  + ";white-space:" + ("pre")  + ";}.results{font-family:" + ("monospace")  + ";white-space:" + ("pre")  + ";}.GB-DVFGDLF{width:" + ("30em")  + ";height:" + ("5em")  + ";white-space:" + ("normal")  + ";}.GB-DVFGDKF{border:" + ("1px"+ " " +"solid"+ " " +"black")  + ";width:" + ("100%")  + ";height:") + (("500px")  + ";}"));
    }
    public java.lang.String error(){
      return "error";
    }
    public java.lang.String results(){
      return "results";
    }
    public java.lang.String sp(){
      return "GB-DVFGDKF";
    }
    public java.lang.String sql(){
      return "GB-DVFGDLF";
    }
  }
  ;
  }
  
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenCss_style style;
  
  static {
    new MainPage_MainPageUiBinderImpl_GenBundle_default_StaticClientBundleGenerator()._init0();
  }
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      style(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("style", style());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'style': return this.@sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenBundle::style()();
    }
    return null;
  }-*/;
}
